package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	//TODO : make sure that there are PERMITS on the number of INSTANCES! fix it!
	//------------start edit - 19/12 --------------------**/
	private static int mp_count=1;
	//------------end edit - 19/12----------------------**/

	public Moneypenny() {
		// TODO Implement this
		//------------start edit - 19/12 --------------------**/
		super("Moneypenny"+mp_count);	// Moneypenny number #
		mp_count++;
		//------------end edit - 19/12----------------------**/
	}

	@Override
	protected void initialize() {
		//TODO: watch for specified moneypenny to handle SENDAGENTS and RELEASEDAGENTS
		//------------start edit - 19/12 --------------------**/
		this.subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) ->
		{
			complete(e , Squad.getInstance().getAgents(e.getNeededAgents()));
			//getting the gadget for the mission, and adding result to this event
			// e.getNeededAgents is the function of the class AgentsAvailableEvent
		} );

		this.subscribeEvent(SendAgentsEvent.class, (SendAgentsEvent e) ->{
			Squad.getInstance().sendAgents(e.sendAgentsInfo() , e.getTimeForMission());
			complete(e , true);
					//sending agents for the mission, and adding result to this event
			// e.sendAgentsInfo and e.getTimeForMission is the function of the class SendAgents
		});

		this.subscribeEvent(ReleasedAgentsEvent.class, (ReleasedAgentsEvent e) -> {
			//TODO: impllement this
				});
		//------------end edit - 19/12----------------------**/
	}

}
