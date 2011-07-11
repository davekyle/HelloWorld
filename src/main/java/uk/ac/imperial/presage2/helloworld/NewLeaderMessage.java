/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import java.util.List;

import dws04.utils.presage2.contactCards.AgentIDTriple;

import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.MulticastMessage;
import uk.ac.imperial.presage2.core.network.NetworkAddress;

/**
 * @author dws04
 *
 */
public class NewLeaderMessage extends MulticastMessage {
	
	AgentIDTriple leader;

	public NewLeaderMessage(Performative performative, NetworkAddress from,
			List<NetworkAddress> to, Time timestamp, AgentIDTriple leader) {
		super(performative, from, to, timestamp);
		this.leader = leader;
	}

	public AgentIDTriple getLeader() {
		return this.leader;
	}
	
	public String toString(){
		return super.toString() + " leader: " + leader; 
	}

}
