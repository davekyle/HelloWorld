/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import uk.ac.imperial.presage2.core.messaging.Input;
import dws04.utils.presage2.contactCards.AgentIDTriple;
import dws04.utils.presage2.fsm.AbstractFSM;
import dws04.utils.presage2.fsm.IsFSMState;

/**
 * @author dws04
 *
 */
public class HelloAgentFSM extends AbstractFSM {
	
	private AgentIDTriple leader;
	private AgentIDTriple myIDTriple;

	public HelloAgentFSM(AgentIDTriple myIDTriple) {
		super(new HelloAgentStateMOVERAND(myIDTriple));
		this.setLeader(myIDTriple);
		this.setMyIDTriple(myIDTriple);
		//Dont need to do this // ((HelloAgentState)this.getState()).setMyAgentIDTriple(myIDTriple);
	}

	/* (non-Javadoc)
	 * @see dws04.utils.presage2.fsm.AbstractFSM#onEnterState(dws04.utils.presage2.fsm.IsFSMState)
	 */
	@Override
	public void onEnterState(IsFSMState state, Input in) {
		if (in instanceof NewLeaderMessage) {
			// Propagate this to other states. FIXME Must be a better way (make states classes, not enum...?)
			// shouldnt be needed anymore //((HelloAgentState)this.getState()).setMyAgentIDTriple(myIDTriple);
			if (isFollowTheLeader(state)) {
				this.setLeader(((NewLeaderMessage)in).getLeader());
				logger.info("I should follow " + this.getLeader());
			}
			else if (isBeTheLeader(state)){
				logger.info("I'm now the leader !");
				this.setLeader(getMyIDTriple());
			}
			else if (isMoveRand(state)){
				logger.info("I'm moving randomly !");
				this.setLeader(null);
			}
		}
	}

	/**
	 * @param state
	 * @return
	 */
	public static boolean isMoveRand(IsFSMState state) {
		return (state instanceof HelloAgentStateMOVERAND);
	}

	/**
	 * @param state
	 * @return
	 */
	public static boolean isBeTheLeader(IsFSMState state) {
		return (state instanceof HelloAgentStateBETHELEADER);
	}

	/**
	 * @param state
	 * @return
	 */
	public static boolean isFollowTheLeader(IsFSMState state) {
		return (state instanceof HelloAgentStateFOLLOWTHELEADER);
	}

	/**
	 * @param leader the leader to set
	 */
	private void setLeader(AgentIDTriple leader) {
		this.leader = leader;
	}

	/**
	 * @return the leader
	 */
	public AgentIDTriple getLeader() {
		return leader;
	}

	/**
	 * @param myIDTriple the myIDTriple to set
	 */
	private void setMyIDTriple(AgentIDTriple myIDTriple) {
		this.myIDTriple = myIDTriple;
	}

	/**
	 * @return the myIDTriple
	 */
	private AgentIDTriple getMyIDTriple() {
		return myIDTriple;
	}

}
