package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.publishers.TimeService;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	//TODO : make sure that there are PERMITS on the number of INSTANCES! fix it!
	//------------start edit - 19/12 --------------------**/
	private static int m_count =1;
	private int curr_tick;
	//------------end edit - 19/12----------------------**/

	public M() {
		// TODO Implement this
		//------------start edit - 19/12 --------------------**/
		super("M"+m_count);		//M number #
		m_count++;
		//------------end edit - 19/12----------------------**/
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		//------------start edit - 19/12 --------------------**/
		//using lambda expression
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			this.curr_tick = b.getCurrTick();
				});	//this broadcast is a TickBroadcast which informs us of an update on the time ticks

		//using lambda expression
		this.subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent e) -> {
			AgentsAvailableEvent new_agentAvailEvent = new AgentsAvailableEvent(e.getMissionInfo().getSerialAgentsNumbers());
			//creating event available agents we want for the mission
			Future <Boolean> future_agentAvail = getSimplePublisher().sendEvent(new_agentAvailEvent);
			//Future object we make for the AgentAvailEvent
			/** if future is true - all agents are NOT available right now
			 * sends the event by SIMPLEPUBLISHER because we are not allowed to connect with the MessageBroker Directly
			 * LOGIC: first get agents -> than get gadget
			 * */

			/** FOR REPORT: TODO: add the reported stuff to the diary*/

			//TODO: assumption - make AGENTS UNavailable from squad methods
			if(future_agentAvail.get()) { // if agents are available - we marked them as isAvailable=false //TODO: make sure that SQUAD.JAVA is thread safe for those actions
				GadgetAvailableEvent new_gadgetAvailEvent = new GadgetAvailableEvent(e.getMissionInfo().getGadget());
				//creating event available gadget we want for the mission
				Future<Boolean> future_gadgetAvail = getSimplePublisher().sendEvent(new_agentAvailEvent);
				//Future object we make for the GadgetAvailEvent
				if (future_gadgetAvail.get()) {	//if gadget is available - we took it and deleted it //TODO: make sure that INVENTORY.JAVA is thread safe for those actions
					int Duration = e.getMissionInfo().getDuration();
					int MissionEnd = e.getMissionInfo().getTimeExpired();
					int ENDOFTIME = TimeService.getInstance().getTotal_tick();
					if (curr_tick+Duration<=MissionEnd && curr_tick+Duration<=ENDOFTIME) {
						SendAgentsEvent new_sendAgentsEvent = new SendAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers(),
								e.getMissionInfo().getDuration()); // sending the agents we want to send and the time for the mission
						/** now the agents will released after the duration of the mission!!! */
						complete(e, "Completed");
					}
					else{	//no time for duration
						ReleasedAgentsEvent new_releasedAgentsEvent = new ReleasedAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers()); //created new event to released occupied agents
						Future<Boolean> future_agentsReleased = getSimplePublisher().sendEvent(new_releasedAgentsEvent); // Future object we make for the ReleasedAgentsEvent
						//Gadget is gone for good....
						complete(e, "Aborted");
					}
				}
				else{		//no gadget available
					ReleasedAgentsEvent new_releasedAgentsEvent = new ReleasedAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers()); //created new event to released occupied agents
					Future<Boolean> future_agentsReleased = getSimplePublisher().sendEvent(new_releasedAgentsEvent); // Future object we make for the ReleasedAgentsEvent
					complete(e, "Aborted");
				}
			}// if no available, will wait there until we get all the agents
			else{ // for the ENDOFTIME mission should be aborted
				complete(e, "Aborted");
			}
		});
		//------------end edit - 19/12----------------------**/
	}

}
