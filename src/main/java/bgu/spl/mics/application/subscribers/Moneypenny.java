package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	//------------start edit - 20/12 --------------------**/
	private static int mp_count=1;
	private static int mp_total=0;
	private int curr_mp_count;
	private int curr_tick;
	//------------end edit - 20/12----------------------**/

	public Moneypenny(int total) {
		//------------start edit - 20/12 --------------------**/
		super("Moneypenny"+mp_count);	// Moneypenny number #
		this.curr_mp_count = mp_count;
		mp_count++;
		if(mp_total==0)					//to know how many mp we have - the purpose of this is to asign few mp to send and release agents event ONLY
			mp_total = total;
		//------------end edit - 20/12----------------------**/
	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			this.curr_tick = b.getCurrTick();
		});	//this broadcast is a TickBroadcast which informs us of an update on the time ticks
		/**assumption: watch for specified moneypenny to handle SENDAGENTS and RELEASEDAGENTS **/
		//------------start edit - 21/12 --------------------**/
		if(curr_mp_count <= Math.ceil(mp_total / 2.5)){				//only the 25% of the mp will be SEND & RELEASE
			this.subscribeEvent(SendAgentsEvent.class, (SendAgentsEvent e) ->{
				Squad.getInstance().sendAgents(e.sendAgentsInfo() , e.getTimeForMission());

				ArrayList<Object> tmp_arraylist = new ArrayList<>();
				tmp_arraylist.add(this.curr_mp_count);											//moneypenny number
				tmp_arraylist.add(Squad.getInstance().getAgentsNames( e.sendAgentsInfo()));		// agents name
/**	System.out.println("MP "+curr_mp_count+" at tick time: " +curr_tick);	//TODO: REMOVE THIS TEST **/
				complete(e , tmp_arraylist );	// the future is MP# and AgentsName for the diary
				//sending agents for the mission, and adding result to this event
				// e.sendAgentsInfo and e.getTimeForMission is the function of the class SendAgentsEvent
			});

			this.subscribeEvent(ReleasedAgentsEvent.class, (ReleasedAgentsEvent e) -> {
				Squad.getInstance().releaseAgents(e.getReleasedAgents());
				complete(e,true);
				// released agents of the mission, the mission was aborted
				//e.getReleasedAgents is the function of the class ReleasedAgentsEvent
			});
		}
		else{														//The upper 66% will be AgentsAvailableEvent
			this.subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) ->
			{
				boolean answer = Squad.getInstance().getAgents(e.getNeededAgents());
				complete(e ,answer ); /**BEWARE: may stuck there when terminated!!! */
				//getting the gadget for the mission, and adding result to this event
				// e.getNeededAgents is the function of the class AgentsAvailableEvent
			} );
		}

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast e) ->
		{
			Squad.getInstance().terminate();		    //SETTER - change the field to true in the Squad and woke up every mp
			LinkedList<String> agentsNames = new LinkedList<>();
			for(Map.Entry<String, Agent> entry : Squad.getInstance().getAgentsMap().entrySet()) 	//running on the map <string, agent> - GETTER
				agentsNames.add(entry.getKey());
			Squad.getInstance().releaseAgents( agentsNames );			//releasing all agents - breaking the wait
			this.terminate();
		});
		//------------end edit - 21/12----------------------**/
	}

}
