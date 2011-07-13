/**
 * 
 */
package uk.ac.imperial.presage2.helloworld.messaging;

import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import dws04.utils.presage2.fsm.IsFSMState;

/**
 * @author dws04
 *
 */
public enum AgentInfoRequestState implements IsFSMState {
	IDLE {
		@Override
		public IsFSMState next(Input in) {
			if ((in instanceof AgentInfoMessage) && (((AgentInfoMessage)(in)).getPerformative().equals(Performative.REQUEST))) {
				return SEND_INFO;
			}
			else {
				return IDLE;
			}
		}

		@Override
		public boolean canHandle(Input in) {
			if ((in instanceof AgentInfoMessage) && (((AgentInfoMessage)(in)).getPerformative().equals(Performative.REQUEST))) return true;
			else return false;
		}
	},
	SEND_INFO {
		@Override
		public IsFSMState next(Input in) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean canHandle(Input in) {
			// TODO Auto-generated method stub
			return false;
		}
	},
	WAIT_FOR_INFO {

		@Override
		public IsFSMState next(Input in) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean canHandle(Input in) {
			// TODO Auto-generated method stub
			return false;
		}
	},
	END {
		@Override
		public IsFSMState next(Input in) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean canHandle(Input in) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	abstract public IsFSMState next(Input in);

	abstract public boolean canHandle(Input in);
	
}
