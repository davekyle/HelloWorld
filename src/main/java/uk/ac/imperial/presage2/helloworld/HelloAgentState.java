/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import dws04.utils.presage2.fsm.IsFSMState;
import uk.ac.imperial.presage2.core.messaging.Input;

/**
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
			// TODO Auto-generated method stub
			return null;
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
	};
	
	abstract public HelloAgentState next(Input input);
	
	public boolean canHandle(Input in){
		return false;
	}
}
