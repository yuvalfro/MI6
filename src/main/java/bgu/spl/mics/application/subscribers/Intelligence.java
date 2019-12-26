package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	//------------start edit - 20/12 --------------------**/
	private static int intel_count=1;
	private HashMap<Integer, MissionInfo> missionInfoHashMap;
	private List<MissionInfo> leftovers;
	//------------end edit - 20/12----------------------**/

	//------------start edit - 20/12 --------------------**/
	public Intelligence(List<MissionInfo> missionInfoList){
		super("Intelligence"+intel_count);	// Intelligence number #
		intel_count++;
		missionInfoHashMap = new HashMap<>();
		leftovers = new LinkedList<>();
		for(MissionInfo m: missionInfoList){				//Hashmap for missions by the time issued
			if(missionInfoHashMap.containsKey(m.getTimeIssued()))	// double mission at the same time
				leftovers.add(m);		//double timed missions
			else
				missionInfoHashMap.put(m.getTimeIssued() , m);
		} /** @FIXED@ we have the same key*/
	}
	//------------end edit - 20/12----------------------**/

	@Override
	protected void initialize() {
		//------------start edit - 20/12 --------------------**/
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast e) ->
				{
					int curr_tick =  e.getCurrTick();
					if(missionInfoHashMap.containsKey( curr_tick )) {    // if we have mission that starts in that current tick
						MissionReceivedEvent new_mission = new MissionReceivedEvent( missionInfoHashMap.remove( curr_tick ), curr_tick );
						Future<String> future_missionEvent = getSimplePublisher().sendEvent(new_mission);

						for(MissionInfo curr : leftovers){			// HANLED the event when 2 missions or more are at the same time
							if(curr.getTimeIssued()==curr_tick){
								MissionReceivedEvent new_mission2 = new MissionReceivedEvent( curr, curr_tick );
								Future<String> future_missionEvent2 = getSimplePublisher().sendEvent(new_mission2);

							}
						}
						// no need to complete - M will do the complete event
					}
				});

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast e) ->
				{
					this.terminate();
				});
		//------------end edit - 20/12----------------------**/
	}
}
