package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	//------------start edit - 19/12 --------------------**/
	private int time_tick;
	private int tick_delay;
	private int total_tick;

	// for signleton - thread safe
	private static class SingletonHolder {
		private static TimeService ts_instance;
	} // get instance like TIRGUL 8
	//------------end edit - 19/12----------------------**/

	public TimeService(int tick_delay, int total_tick) {
		//------------start edit - 19/12 --------------------**/
		super("TimeService");
		this.tick_delay=tick_delay;
		this.total_tick=total_tick;
		this.time_tick=0;
		SingletonHolder.ts_instance = this;
		/** after the first build of the TimeService, we will insert it to the singleton holder*/
		//------------end edit - 19/12----------------------**/
	}

	@Override
	protected void initialize() {
		// TODO Implement this
	}

	@Override
	public void run() {
		// TODO Implement this
	}

	//------------start edit - 19/12 --------------------**/
	//timer service tick getter
	public int getTime_tick() {
		return time_tick;
	}

	//timer service TOTAL timer getter
	public int getTotal_tick(){
		return total_tick;
	}

	//Time service singleton getter
	public static TimeService getInstance(){
		return SingletonHolder.ts_instance;
	}
	//------------end edit - 19/12----------------------**/

}
