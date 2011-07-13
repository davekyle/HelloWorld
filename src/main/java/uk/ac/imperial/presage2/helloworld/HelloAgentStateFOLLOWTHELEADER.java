/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import dws04.utils.presage2.contactCards.AgentIDTriple;
import dws04.utils.presage2.fsm.IsFSMState;

/**
 * @author dws04
 *
 */
public class HelloAgentStateFOLLOWTHELEADER extends HelloAgentStateClass {
	
	private AgentIDTriple myAgentIDTriple;

	/**
	 * @param myAgentIDTriple
	 */
	protected HelloAgentStateFOLLOWTHELEADER(AgentIDTriple myAgentIDTriple) {
		super();
		this.myAgentIDTriple = myAgentIDTriple;
	}

	/* (non-Javadoc)
	 * @see uk.ac.imperial.presage2.helloworld.HelloAgentStateClass#next(uk.ac.imperial.presage2.core.messaging.Input)
	 */
	@Override
	public HelloAgentStateClass next(Input input) {
		if (input instanceof NewLeaderMessage) {
			if (((NewLeaderMessage)input).getPerformative().equals(Performative.REQUEST)) {
				System.out.println("REQUEST - My id : " + getMyAgentIDTriple());
				// TODO not implemented atm. You don't get to decide.
				return new HelloAgentStateMOVERAND(getMyAgentIDTriple());
			}
			else if (((NewLeaderMessage)input).getPerformative().equals(Performative.INFORM)) {
				System.out.println("INFORM - My id : " + getMyAgentIDTriple());
				if (((NewLeaderMessage)input).getLeader().getAddr().equals(getMyAgentIDTriple().getAddr())) {
					System.out.println("I'm already in BE_THE_LEADER - My id : " + getMyAgentIDTriple());
					return new HelloAgentStateBETHELEADER(getMyAgentIDTriple());
				}
				else if (((NewLeaderMessage)input).getLeader() == null) {
					System.out.println("MOVE_RAND - My id : " + getMyAgentIDTriple());
					return new HelloAgentStateMOVERAND(getMyAgentIDTriple());
				}
				else {
					System.out.println("FOLLOW_THE_LEADER - My id : " + getMyAgentIDTriple());
					return new HelloAgentStateFOLLOWTHELEADER(getMyAgentIDTriple());
				}
			}
		}
		// If you get here...
		return null;
	}

	/**
	 * @return myAgentIDTriple
	 */
	private AgentIDTriple getMyAgentIDTriple() {
		return myAgentIDTriple;
	}

	/**
	 * @param myAgentIDTriple the myAgentIDTriple to set
	 */
	public void setMyAgentIDTriple(AgentIDTriple myAgentIDTriple) {
		this.myAgentIDTriple = myAgentIDTriple;
	}

}
