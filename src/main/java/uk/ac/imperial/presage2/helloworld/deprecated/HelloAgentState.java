/**
 * 
 */
package uk.ac.imperial.presage2.helloworld.deprecated;

import dws04.utils.presage2.contactCards.AgentIDTriple;
import dws04.utils.presage2.fsm.IsFSMState;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.helloworld.NewLeaderMessage;

/**
 * Should really be done as a bunch of classes but ohwell
 * 
 * @author dws04
 *
 */
@Deprecated
public enum HelloAgentState implements IsFSMState {
	MOVE_RAND {
		/**
		 * If you become the leader, switch to BE_THE_LEADER.
		 * If you find a leader, switch to FOLLOW_THE_LEADER.
		 */
		/*@Override
		public HelloAgentState next(Input input) {
			if (input instanceof NewLeaderMessage) {
				if (((NewLeaderMessage)input).getPerformative().equals(Performative.REQUEST)) {
					// TODO not implemented atm. You don't get to decide.
					return MOVE_RAND;
				}
				else if (((NewLeaderMessage)input).getPerformative().equals(Performative.INFORM)) {
					if (((NewLeaderMessage)input).getLeader().equals(getMyAgentIDTriple())) {
						return BE_THE_LEADER;
					}
					else if (((NewLeaderMessage)input).getLeader().equals(null)) {
						return MOVE_RAND;
					}
					else {
						return FOLLOW_THE_LEADER;
					}
				}
			}
			return null;
		}*/

		@Override
		public boolean canHandle(Input in) {
			if (in instanceof NewLeaderMessage) return true;
			else return false;
		}
	},
/*	HANDSHAKE {
		*//**
		 * Once you have exchanged information, switch to CHOOSE_LEADER
		 *//*
		@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	CHOOSE_LEADER {
		*//**
		 * Once a leader has been chosen, switch to MOVE_AS_GROUP
		 *//*
		@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}
	},*/
	BE_THE_LEADER {
		
		/**
		 * If someone else becomes leader, switch to FOLLOW_THE_LEADER
		 */
		/*@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}*/

		@Override
		public boolean canHandle(Input in) {
			if (in instanceof NewLeaderMessage) return true;
			else return false;
		}
	},
	FOLLOW_THE_LEADER {
		
		/**
		 * If you become the leader, switch to BE_THE_LEADER
		 */
		/*@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}*/

		@Override
		public boolean canHandle(Input in) {
			if (in instanceof NewLeaderMessage) return true;
			else return false;
		}
	};

	private AgentIDTriple myAgentIDTriple;
	
	//abstract public HelloAgentState next(Input input);
	public HelloAgentState next(Input input) {
		if (input instanceof NewLeaderMessage) {
			if (((NewLeaderMessage)input).getPerformative().equals(Performative.REQUEST)) {
				System.out.println("REQUEST - My id : " + getMyAgentIDTriple());
				// TODO not implemented atm. You don't get to decide.
				return MOVE_RAND;
			}
			else if (((NewLeaderMessage)input).getPerformative().equals(Performative.INFORM)) {
				System.out.println("INFORM - My id : " + getMyAgentIDTriple());
				if (((NewLeaderMessage)input).getLeader().getAddr().equals(getMyAgentIDTriple().getAddr())) {
					System.out.println("BE_THE_LEADER - My id : " + getMyAgentIDTriple());
					return BE_THE_LEADER;
				}
				else if (((NewLeaderMessage)input).getLeader() == null) {
					System.out.println("MOVE_RAND - My id : " + getMyAgentIDTriple());
					return MOVE_RAND;
				}
				else {
					System.out.println("FOLLOW_THE_LEADER - My id : " + getMyAgentIDTriple());
					return FOLLOW_THE_LEADER;
				}
			}
		}
		// If you get here...
		return null;
	}
	
	

	abstract public boolean canHandle(Input in);

	/**
	 * @return the myAgentIDTriple
	 */
	public AgentIDTriple getMyAgentIDTriple() {
		return myAgentIDTriple;
	}

	/**
	 * @param myAgentIDTriple the myAgentIDTriple to set
	 */
	public void setMyAgentIDTriple(AgentIDTriple myAgentIDTriple) {
		this.myAgentIDTriple = myAgentIDTriple;
	}
}
