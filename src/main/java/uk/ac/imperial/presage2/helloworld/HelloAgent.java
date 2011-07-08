/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import dws04.utils.presage2.contactCards.AgentIDTriple;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.environment.CommunicationRangeService;
import uk.ac.imperial.presage2.util.location.Discrete2DLocation;
import uk.ac.imperial.presage2.util.location.HasLocation;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.Move2D;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;
import uk.ac.imperial.presage2.util.participant.HasCommunicationRange;
import uk.ac.imperial.presage2.util.participant.HasPerceptionRange;

/**
 * @author Sam Macbeth, dws04
 *
 */
public class HelloAgent extends AbstractParticipant implements HasLocation, HasPerceptionRange, HasCommunicationRange {
	
	class DataStore {
		/**
		 * LinkedHashMap to keep track of all the agents known.
		 * Keyed by NetworkAddress.getId()
		 */
		LinkedHashMap<UUID,AgentIDTriple> knownAgents;
		
		/**
		 * Let the agent keep a track of it's own "business card"
		 * TODO make a listener or something for this ?
		 * So that the agent knows when it should update other people if it changes.
		 */
		AgentIDTriple myAgentIDTriple;
		
		/**
		 * Agent id info for the agent that this agent is following.
		 * Should be set to myAgentIDTriple if this agent is the leader.
		 */
		AgentIDTriple leader;
		
		HelloAgentFSM fsm;
	}
	
	class EnvironmentState {
		
		Location loc;
		
		double perceptionRange;
		
		double communicationRange;
		
	}
	
	protected DataStore dataStore = new DataStore();
	
	private EnvironmentState environmentState = new EnvironmentState();
	
	private ParticipantLocationService locationService;
	
	public HelloAgent(UUID id, String name, Location loc, double perceptionRange, double communicationRange) {
		super(id, name);
		environmentState.loc = loc;
		environmentState.perceptionRange = perceptionRange;
		environmentState.communicationRange = communicationRange;
		dataStore.knownAgents = new LinkedHashMap<UUID,AgentIDTriple>();
		// Don't yet know our NetworkAddress, but can fill in the rest
		dataStore.myAgentIDTriple = new AgentIDTriple(id, name, null);
	}
	
	@Override
	public void initialise() {
		super.initialise();
		try {
			this.locationService = this.getEnvironmentService(ParticipantLocationService.class);
		} catch (UnavailableServiceException e) {
			logger.warn(e);
			this.locationService = null;
		}
		// Agent should now know its NetworkAddress
		dataStore.myAgentIDTriple.setAddr(this.network.getAddress());
		dataStore.fsm = new HelloAgentFSM(dataStore.myAgentIDTriple);
	}

	public double getCommunicationRange() {
		return environmentState.communicationRange;
	}

	public double getPerceptionRange() {
		return environmentState.perceptionRange;
	}

	public Location getLocation() {
		return environmentState.loc;
	}

	public void setLocation(Location l) {
		this.environmentState.loc = l;
	}

	@Override
	protected void processInput(Input in) {
		if(in instanceof HelloMessage) {
			handleHelloMessage((HelloMessage)in);
		}
		else if (in instanceof AgentInfoMessage) {
			handleAgentInfoMessage((AgentInfoMessage)in);
		}
		else if (in instanceof NewLeaderMessage) {
			handleNewLeaderMessage((NewLeaderMessage)in);
		}
	}

	@Override
	protected Set<ParticipantSharedState<?>> getSharedState() {
		Set<ParticipantSharedState<?>> ss = super.getSharedState();
		// shared environmentState for ParticipantLocationService
		ss.add(ParticipantLocationService.createSharedState(this.getID(), this));
		// shared environmentState for network communication range
		ss.add(CommunicationRangeService.createSharedState(getID(), this));
		
		return ss;
	}

	@Override
	public void execute() {
		// Messages are processed in this, so all that is done first !
		super.execute();
		
		logger.info("My location is: "+ this.getLocation());
		//logger.info("My IDs are : " + this.dataStore.myAgentIDTriple);
		
		observeNearbyAgents();
		
		broadcastHello();
		
		outputKnownAgents();
		
		doMove();
		
	}

	/**
	 * Attempt to make a move action. Randomness depends on state of agent
	 */
	protected void doMove(){
		if (this.dataStore.fsm.getState().equals(HelloAgentState.MOVE_RAND)) {
			// random movement
			Move2D<Integer> move = new Move2D<Integer>(Random.randomInt(10)-5, Random.randomInt(10)-5);
			
			logger.info("Attempting random move: "+ move);
			
			try {
				environment.act(move, getID(), authkey);
			} catch (ActionHandlingException e) {
				logger.warn(e);
			}
		}
		else if (this.dataStore.fsm.getState().equals(HelloAgentState.BE_THE_LEADER)) {
			// TODO random movement for the moment
			Move2D<Integer> move = new Move2D<Integer>(Random.randomInt(10)-5, Random.randomInt(10)-5);
			
			logger.info("Attempting leader move: "+ move);
			
			try {
				environment.act(move, getID(), authkey);
			} catch (ActionHandlingException e) {
				logger.warn(e);
			}
		}
		else if (this.dataStore.fsm.getState().equals(HelloAgentState.FOLLOW_THE_LEADER)) {
			//Discrete2DLocation myLoc = (Discrete2DLocation) this.getLocation();
			//Discrete2DLocation leaderLoc = (Discrete2DLocation) this.locationService.getAgentLocation(this.dataStore.leader.getUuid());
			// TODO random movement for the moment
			Move2D<Integer> move = new Move2D<Integer>(Random.randomInt(10)-5, Random.randomInt(10)-5);
			
			logger.info("Attempting follow move: "+ move);
			
			try {
				environment.act(move, getID(), authkey);
			} catch (ActionHandlingException e) {
				logger.warn(e);
			}
		}
	}
	
	/**
	 * Send a broadcasted HelloMessage to all agents in communication range
	 */
	protected void broadcastHello(){
		this.network.sendMessage(new HelloMessage(this.network.getAddress(), getTime()));
	}

	/**
	 * Send a message to an agent asking for their UUID and human-readable name
	 * @param targetAddress NetworkAddress of the target agent
	 */
	private void sendAgentInfoMsgRequest(NetworkAddress targetAddress) {
		this.network.sendMessage(new AgentInfoMessage(Performative.REQUEST, this.network.getAddress(), targetAddress, getTime(), dataStore.myAgentIDTriple ));
	}
	
	/**
	 * Send a response to a request for agent info
	 * @param targetAddress
	 */
	private void sendAgentInfoMsgInform(NetworkAddress targetAddress){
		this.network.sendMessage(new AgentInfoMessage(Performative.INFORM, this.network.getAddress(), targetAddress, getTime(), dataStore.myAgentIDTriple ));
	}
	
	/**
	 * Log the presence of nearby agents
	 */
	protected void observeNearbyAgents() {
		for(Map.Entry<UUID, Location> agent : this.locationService.getNearbyAgents().entrySet()) {
			logger.info("I see agent: "+agent.getKey()+" at location: "+agent.getValue());
		}
	}
	
	protected void outputKnownAgents() {
		logger.info("I know the following agents:");
		for (Entry<UUID, AgentIDTriple> entry : this.dataStore.knownAgents.entrySet() ) {
			logger.info("\t " + entry.getValue().toString());
		}
	}

	protected void handleHelloMessage(HelloMessage msg) {
		NetworkAddress originator = msg.getFrom();
		String string = originator.getId() + " sent me a HelloMessage, ";
		if (dataStore.knownAgents.containsKey(originator.getId())) {
			string = string + "and I already know them.";
		}
		else {
			string = string + "and I haven't met them before, so I'll add them to my list and ask them for more info.";
			dataStore.knownAgents.put(originator.getId(), new AgentIDTriple(null, null, originator));
			sendAgentInfoMsgRequest(originator);
		}
		logger.info(string);
	}

	private void handleAgentInfoMessage(AgentInfoMessage msg) {
		Performative perf = msg.getPerformative();
		NetworkAddress originator = msg.getFrom();
		if (perf.equals(Performative.REQUEST)) {
			logger.info(originator.getId() + " sent me a request for AgentInfo, so I will add them to my list and send my info back.");
			sendAgentInfoMsgInform(originator);
			addAgentInfo(msg.getContent());
		}
		else if (perf.equals(Performative.INFORM)) {
			logger.info(originator.getId() + " replied to my request for AgentInfo, so I will add them to my list.");
			addAgentInfo(msg.getContent());
		}
	}

	private void handleNewLeaderMessage(NewLeaderMessage msg) {
		this.dataStore.fsm.next(msg);
	}
	
	private void addAgentInfo(AgentIDTriple info){
		if (!this.dataStore.knownAgents.containsKey(info.getUuid())) {
			dataStore.knownAgents.put(info.getUuid(), new AgentIDTriple(info.getUuid(), info.getName(), info.getAddr()));
		}
		else {
			logger.info("Current info: " + dataStore.knownAgents.get(info.getUuid()));
			dataStore.knownAgents.get(info.getUuid()).update(info.getUuid(), info.getName(), info.getAddr());
			logger.info("Updated info: " + dataStore.knownAgents.get(info.getUuid()));
		}
	}
	
	private void addAgentInfo(UUID uuid, String name, NetworkAddress addr) {
		if (!this.dataStore.knownAgents.containsKey(uuid)) {
			dataStore.knownAgents.put(uuid, new AgentIDTriple(uuid, name, addr));
		}
		else {
			dataStore.knownAgents.get(uuid).update(uuid, name, addr);
		}
	}
	
}
