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
		super(HelloAgentState.MOVE_RAND);
		this.setLeader(myIDTriple);
		this.setMyIDTriple(myIDTriple);
		((HelloAgentState)this.getState()).setMyAgentIDTriple(myIDTriple);
	}

	/* (non-Javadoc)
	 * @see dws04.utils.presage2.fsm.AbstractFSM#onEnterState(dws04.utils.presage2.fsm.IsFSMState)
	 */
	@Override
	public void onEnterState(IsFSMState state, Input in) {
		if (in instanceof NewLeaderMessage) {
			if (state.equals(HelloAgentState.FOLLOW_THE_LEADER)) {
				this.setLeader(((NewLeaderMessage)in).getLeader());
				logger.info("I'm following " + this.getLeader().getName());
			}
			else if (state.equals(HelloAgentState.BE_THE_LEADER)){
				logger.info("I'm now the leader !");
				this.setLeader(getMyIDTriple());
			}
			else if (state.equals(HelloAgentState.MOVE_RAND)){
				logger.info("I'm moving randomly !");
				this.setLeader(null);
			}
		}
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
