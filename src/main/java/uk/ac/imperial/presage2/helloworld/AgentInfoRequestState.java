/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import dws04.utils.presage2.fsm.FSMStateCannotHandleInputException;
import dws04.utils.presage2.fsm.IsFSMState;

/**
 * @author dws04
 *
 */
public enum AgentInfoRequestState implements IsFSMState {
	IDLE {
		@Override
		public IsFSMState next(Input in)
				throws FSMStateCannotHandleInputException {
			if ((in instanceof AgentInfoMessage) && (((AgentInfoMessage)(in)).getPerformative().equals(Performative.REQUEST))) {
				return SEND_INFO;
			}
			else throw new FSMStateCannotHandleInputException("IDLE can only handle a REQUEST");
		}
	},
	SEND_INFO {
		@Override
		public IsFSMState next(Input in)
				throws FSMStateCannotHandleInputException {
			// TODO Auto-generated method stub
			return null;
		}
	},
	WAIT_FOR_INFO {

		@Override
		public IsFSMState next(Input in)
				throws FSMStateCannotHandleInputException {
			// TODO Auto-generated method stub
			return null;
		}
	},
	END {
		@Override
		public IsFSMState next(Input in)
				throws FSMStateCannotHandleInputException {
			// TODO Auto-generated method stub
			return null;
		}
	};

	abstract public IsFSMState next(Input in) throws FSMStateCannotHandleInputException;

	public boolean canHandle(Input in) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
