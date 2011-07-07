/**
 * 
 */
package uk.ac.imperial.presage2.helloworld;

import dws04.utils.presage2.AgentIDTriple;
import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.core.network.UnicastMessage;

/**
 * Message regarding agent information. 
 * 
 * @author dws04
 *
 */
public class AgentInfoMessage extends UnicastMessage {
	
	AgentIDTriple content;
	
	public AgentInfoMessage(Performative performative, NetworkAddress from, NetworkAddress to, Time timestamp, AgentIDTriple content) {
		super(performative, from, to, timestamp);
		this.content = content;
	}
	
	public AgentIDTriple getContent(){
		return this.content;
	}

}
