/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import dws04.utils.presage2.contactCards.AgentIDTriple;
import dws04.utils.presage2.contactCards.AgentIDTripleListToNetworkAddressList;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.core.participant.Participant;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.location.Move2D;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;

/**
 * @author dws04
 *
 */
public class CoordinatorAgent extends AbstractParticipant {

	class DataStore {

		LinkedList<AgentIDTriple> knownAgents;
//		Iterator<AgentIDTriple> agentIt;
		AgentIDTriple myAgentIDTriple;
		AgentIDTriple currentLeader;
	}
	protected DataStore dataStore = new DataStore();
	
/*	class EnvironmentState {
		
		Location loc;
		
		double perceptionRange;
		
		double communicationRange;
		
	}
	
	private EnvironmentState environmentState = new EnvironmentState();
	private ParticipantLocationService locationService;*/

	private int counter;
	
	protected CoordinatorAgent(UUID id, String name) {
		super(id, name);
		dataStore.knownAgents = new LinkedList<AgentIDTriple>();
		dataStore.currentLeader = new AgentIDTriple();
		dataStore.myAgentIDTriple = new AgentIDTriple(id, name, null);
		this.counter = 0;
		logger.info("My ID is : " + id);
	}

	/* (non-Javadoc)
	 * @see uk.ac.imperial.presage2.util.participant.AbstractParticipant#processInput(uk.ac.imperial.presage2.core.messaging.Input)
	 */
	@Override
	protected void processInput(Input in) {
		// TODO Auto-generated method stub
		// don't process inputs like a HelloAgent
	}

	
	@Override
	public void initialise() {
		super.initialise();
	}

	/* (non-Javadoc)
	 * @see uk.ac.imperial.presage2.helloworld.HelloAgent#getSharedState()
	 */
	@Override
	protected Set<ParticipantSharedState<?>> getSharedState() {
		// Override this because we don't need a communication range (can talk to everyone) and don't need a location (don't exist anywhere)
		return Collections.emptySet();
	}

	@Override
	public void execute() {
		// Messages are processed in this, so all that is done first !
		super.execute();
		
		findConnectedNodes();
		chooseLeader();
	}
	
	protected void outputKnownAgents() {
		logger.info("I know the following agents because I'm awesome:");
		for (AgentIDTriple agent : this.dataStore.knownAgents ) {
			logger.info("\t " + agent);
		}
	}
	
	protected void findConnectedNodes(){
		for (NetworkAddress addr : this.network.getConnectedNodes()) {
			if (!knows(addr)) {
				this.dataStore.knownAgents.add(new AgentIDTriple(null,null,addr));
			}

		}
		/*for (Participant agent : this.scenario.getParticipants()) {	
			if (!knows(agent.getID())) {
				this.dataStore.knownAgents.add(new AgentIDTriple(agent.getID(), null, )));
			}
		}*/
	}
	
	private void chooseLeader() {
		
		// Every x cycles, pick an agent to make the leader
		if (counter==5) {
			
			if (dataStore.knownAgents.isEmpty()) {
				logger.info("I don't know anyone, so there is no leader ! :(");
				// don't change the counter, so that if they don't know anyone it will be changed asap
			}
			else {
				// Store is not empty, but there's no leader atm, so pick the first.
				if (dataStore.currentLeader == null){
					dataStore.currentLeader = dataStore.knownAgents.getFirst();
				}
				// If the current leader is not the last one in the list, get the next leader
				else if ( (dataStore.knownAgents.indexOf(dataStore.currentLeader)) != (dataStore.knownAgents.indexOf(dataStore.knownAgents.getLast())) ) {
					dataStore.currentLeader = dataStore.knownAgents.get(dataStore.knownAgents.indexOf(dataStore.currentLeader)+1);
				}
				else {
					// If they are the last one, reset
					dataStore.currentLeader = dataStore.knownAgents.getFirst();
				}
				logger.info("I'm choosing " + dataStore.currentLeader + " to be the new leader");
				sendLeaderMessage(dataStore.currentLeader);
				counter = 0;
			}
		}
		else {
			if (dataStore.currentLeader == null) {
				logger.info("There is no leader !");
			}
			else {
				logger.info("The leader is " + dataStore.currentLeader);
			}
			counter++;
		}
	}

	private void sendLeaderMessage(AgentIDTriple newLeader) {
		NewLeaderMessage msg = new NewLeaderMessage(Performative.INFORM, this.network.getAddress(), AgentIDTripleListToNetworkAddressList.make(dataStore.knownAgents), getTime(), newLeader);
		this.network.sendMessage(msg);
	}
	
	protected boolean knows(NetworkAddress addr) {
		boolean result = false;
		for (AgentIDTriple agent : this.dataStore.knownAgents) {
			if (agent.getAddr().equals(addr)) {
				result = true;
				break;
			}
			else {
				// do nothing
			}
		}
		return result;
	}
	
	private boolean knows(UUID id) {
		boolean result = false;
		for (AgentIDTriple agent : this.dataStore.knownAgents) {
			if (agent.getAddr().getId().equals(id)) {
				result = true;
				break;
			}
			else {
				// do nothing
			}
		}
		return result;
	}

}
