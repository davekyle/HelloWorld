/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

import dws04.utils.presage2.contactCards.AgentIDTriple;
import dws04.utils.presage2.contactCards.AgentIDTripleListToNetworkAddressList;

import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.EnvironmentConnector;
import uk.ac.imperial.presage2.core.environment.ParticipantSharedState;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.NetworkAdaptor;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.helloworld.HelloAgent.EnvironmentState;
import uk.ac.imperial.presage2.util.environment.CommunicationRangeService;
import uk.ac.imperial.presage2.util.location.HasLocation;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.Move2D;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;

/**
 * @author dws04
 *
 */
public class CoordinatorAgent extends HelloAgent {

	class DataStore {
		/**
		 * LinkedHashMap to keep track of all the agents known.
		 * Keyed by NetworkAddress.getId()
		 */
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
	
	protected CoordinatorAgent(UUID id, String name, Location loc, double perceptionRange, double communicationRange) {
		super(id, name, loc, communicationRange, communicationRange);
		dataStore.knownAgents = new LinkedList<AgentIDTriple>();
//		dataStore.agentIt = dataStore.knownAgents.iterator();
		dataStore.myAgentIDTriple = new AgentIDTriple(id, name, null);
		this.counter = 0;
	}

	/* (non-Javadoc)
	 * @see uk.ac.imperial.presage2.util.participant.AbstractParticipant#processInput(uk.ac.imperial.presage2.core.messaging.Input)
	 */
	@Override
	protected void processInput(Input in) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void initialise() {
		super.initialise();
	}

	@Override
	public void execute() {
		// Messages are processed in this, so all that is done first !
		super.execute();
		getConnectedNodes();
		chooseLeader();
	}
	
	protected void getConnectedNodes(){
		// convert the result of this.network.getConnectedNodes(); to a linkedlist
	}
	
	/**
	 * Make a move action, will always be random
	 */
	@Override
	protected void doMove(){
		// random movement
		Move2D<Integer> move = new Move2D<Integer>(Random.randomInt(10)-5, Random.randomInt(10)-5);
		
		logger.info("Attempting random move: "+ move);
		
		try {
			environment.act(move, getID(), authkey);
		} catch (ActionHandlingException e) {
			logger.warn(e);
		}
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

}
