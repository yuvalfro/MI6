package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.publishers.TimeService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	//------------start edit - 19/12 --------------------**/
	private static int m_count =1;
	private int curr_tick;			//updates from the TickBroadcast
	private int curr_m_number;
	//------------end edit - 19/12----------------------**/

	public M() {
		//------------start edit - 19/12 --------------------**/
		super("M"+m_count);		//M number #
		this.curr_m_number=m_count;
		m_count++;
		//------------end edit - 19/12----------------------**/
	}

	@Override
	protected void initialize() {
		//------------start edit - 19/12 --------------------**/
		//using lambda expression
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			this.curr_tick = b.getCurrTick();
				});	//this broadcast is a TickBroadcast which informs us of an update on the time ticks

		//using lambda expression
		this.subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent e) -> {
			/** FOR REPORT: added the reported stuff to the diary*/
			//Diary update:
			synchronized (Diary.getInstance()) {
				Diary.getInstance().incrementTotal();    //increment mission received - can be messed up when multi-M instances will increment
			}
			Report curr_report = new Report();
			curr_report.setTimeIssued(e.getTimeIssued());										// Time Issued	-	MUST BE AS FAST AS POSSIBLE
			curr_report.setAgentsSerialNumbers(e.getMissionInfo().getSerialAgentsNumbers());	// Serial Number
			curr_report.setGadgetName(e.getMissionInfo().getGadget());							// Gadget
			curr_report.setMissionName(e.getMissionInfo().getMissionName());					// Mission Name
			curr_report.setM(curr_m_number);													// M number

			AgentsAvailableEvent new_agentAvailEvent = new AgentsAvailableEvent(e.getMissionInfo().getSerialAgentsNumbers());
			//creating event available agents we want for the mission
			Future<Boolean> future_agentAvail = getSimplePublisher().sendEvent(new_agentAvailEvent);
			//Future object we make for the AgentAvailEvent
			 /** sends the event by SIMPLEPUBLISHER because we are NOT allowed to connect with the MessageBroker Directly
			 * LOGIC: first get agents -> than get gadget -> than check time & update diarrhea */

			/** assumption - make AGENTS UNavailable from squad methods**/
			try {
				if (future_agentAvail.get()) { // if agents are available - we marked them as isAvailable=false
					/** __if this ^ future is true__ - all agents are available right now
					 * __else__: no such agent, return false _OR_ terminated broadcast will return false */

					/** MADE sure that SQUAD is thread safe for those actions*/
					GadgetAvailableEvent new_gadgetAvailEvent = new GadgetAvailableEvent(e.getMissionInfo().getGadget());                //creating event available gadget we want for the mission
					Future<ArrayList<Object>> future_gadgetAvail = getSimplePublisher().sendEvent(new_gadgetAvailEvent);                //Future object we make for the GadgetAvailEvent

					try {
						future_gadgetAvail.get((long) e.getMissionInfo().getDuration() - 1, TimeUnit.MILLISECONDS);    //it will wait if searching - for most the duration of the mission
						if (future_gadgetAvail != null && (boolean) future_gadgetAvail.get().get(0)) {    //if gadget is available - we took it and deleted it
							/** __if this ^ future is true__ - gadget is available
							 * __else__: no such gadget, return false.    no termination proccess because GadgetAvail is fast
							 /** MADE sure that INVENTORY is thread safe for those actions */
							int Duration = e.getMissionInfo().getDuration();
							int MissionEnd = e.getMissionInfo().getTimeExpired();

							if (curr_tick + Duration <= MissionEnd) {        // no need to check at the end of time - 25/12
								SendAgentsEvent new_sendAgentsEvent = new SendAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers(),
										e.getMissionInfo().getDuration()); // sending the agents we want to send and the time for the mission
								Future<ArrayList<Object>> future_sendAgents = getSimplePublisher().sendEvent(new_sendAgentsEvent);                        // Future object we make for the SendAgents

								//Diary update:
								curr_report.setMoneypenny((int) future_sendAgents.get().get(0));                // adding MP# to the report
								curr_report.setAgentsNames((List<String>) future_sendAgents.get().get(1));        // adding AgentsNames to the report
								curr_report.setGadgetName(future_gadgetAvail.get().get(2).toString());            // adding the gadget to the report
								curr_report.setQTime(((int) future_gadgetAvail.get().get(1)));                    // adding QTime to the report
								curr_report.setTimeCreated(curr_tick);                                            // adding TimeCreated
		/**	System.out.println("M " + curr_m_number + " at tick time: " + curr_tick);    //TODO: REMOVE THIS TEST **/
								synchronized (Diary.getInstance()) {                //insertion should be safe
									Diary.getInstance().addReport(curr_report);
								}

								/** now the agents will released after the duration of the mission!!! */
								complete(e, "Completed");
							} else {    //no time for duration
								ReleasedAgentsEvent new_releasedAgentsEvent = new ReleasedAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers()); //created new event to released occupied agents
								Future<Boolean> future_agentsReleased = getSimplePublisher().sendEvent(new_releasedAgentsEvent); // Future object we make for the ReleasedAgentsEvent
								//Gadget is gone for good....
								complete(e, "Aborted");
							}
						} else {        //no gadget available
							ReleasedAgentsEvent new_releasedAgentsEvent = new ReleasedAgentsEvent(e.getMissionInfo().getSerialAgentsNumbers()); //created new event to released occupied agents
							Future<Boolean> future_agentsReleased = getSimplePublisher().sendEvent(new_releasedAgentsEvent); // Future object we make for the ReleasedAgentsEvent
							complete(e, "Aborted");
						}
					} catch (NullPointerException er) {		//future gadget null pointer - Q unregistered
						complete(e, "Aborted");
					}
				}// if no available, will wait there until we get all the agents
				else { // for the ENDOFTIME mission should be aborted
					complete(e, "Aborted");
				}
			}catch(NullPointerException ed){			//future agent null pointer - moneyponney unregistered
				complete(e, "Aborted");
			}
		});

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast e) ->
		{
			this.terminate();
		});
		//------------end edit - 19/12----------------------**/
	}

}
