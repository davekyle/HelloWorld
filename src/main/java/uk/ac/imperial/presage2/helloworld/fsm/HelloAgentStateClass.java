package uk.ac.imperial.presage2.helloworld.fsm;


import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.helloworld.messaging.NewLeaderMessage;
import dws04.utils.presage2.fsm.IsFSMState;

/**
 * 
 */

/**
 * @author dws04
 *
 */
public abstract class HelloAgentStateClass implements IsFSMState {

	/* (non-Javadoc)
	 * @see dws04.utils.presage2.fsm.IsFSMState#next(uk.ac.imperial.presage2.core.messaging.Input)
	 */
	public abstract IsFSMState next(Input in);

	/* (non-Javadoc)
	 * @see dws04.utils.presage2.fsm.IsFSMState#canHandle(uk.ac.imperial.presage2.core.messaging.Input)
	 */
	public boolean canHandle(Input in) {
		if (in instanceof NewLeaderMessage) return true;
		else return false;
	}

}
