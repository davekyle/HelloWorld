/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import dws04.utils.presage2.AgentIDTriple;
import dws04.utils.presage2.fsm.IsFSMState;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;

/**
 * Should really be done as a bunch of classes but ohwell
 * 
 * @author dws04
 *
 */
public enum HelloAgentState implements IsFSMState {
	MOVE_RAND {
		/**
		 * If you become the leader, switch to BE_THE_LEADER.
		 * If you find a leader, switch to FOLLOW_THE_LEADER.
		 */
		@Override
		public HelloAgentState next(Input input) {
			if (input instanceof NewLeaderMessage) {
				if (((NewLeaderMessage)input).getPerformative().equals(Performative.REQUEST)) {
					// TODO decide on new leader
				}
				else if (((NewLeaderMessage)input).getPerformative().equals(Performative.INFORM)) {
					if (((NewLeaderMessage)input).getLeader().equals(getMyAgentIDTriple())) {
						return BE_THE_LEADER;
					}
					else {
						return FOLLOW_THE_LEADER;
					}
				}
			}
			return null;
		}

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
		@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean canHandle(Input in) {
			// TODO Auto-generated method stub
			return false;
		}
	},
	FOLLOW_THE_LEADER {
		
		/**
		 * If you become the leader, switch to BE_THE_LEADER
		 */
		@Override
		public HelloAgentState next(Input input) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean canHandle(Input in) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private AgentIDTriple myAgentIDTriple;
	
	abstract public HelloAgentState next(Input input);

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
