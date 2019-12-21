package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link //Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	//------------start edit - 20/12 --------------------**/
	private int time_tick;
	private int TICK_DELAY;
	private int total_tick;

	// for signleton - thread safe
	private static class SingletonHolder {
		private static TimeService ts_instance;
	} // get instance like TIRGUL 8
	//------------end edit - 20/12----------------------**/

	public TimeService(int total_tick) {
		//------------start edit - 20/12 --------------------**/
		super("TimeService");
		this.TICK_DELAY=100;			//100 milisec
		this.total_tick=total_tick;
		this.time_tick=0;
		SingletonHolder.ts_instance = this;
		/** after the first build of the TimeService, we will insert it to the singleton holder*/
		//------------end edit - 20/12----------------------**/
	}

	@Override
	protected void initialize() {
		//------------start edit - 20/12 --------------------**/
		// TODO Implement this
		/**  we do not need to subscribe it to anything */
		//------------end edit - 20/12----------------------**/
	}

	@Override
	public void run() {
		//------------start edit - 20/12 --------------------**/
		while(time_tick < total_tick){
			try {
				TimeUnit.MILLISECONDS.sleep(TICK_DELAY);
			} catch (InterruptedException e) {	}
			time_tick++;
			getSimplePublisher().sendBroadcast(new TickBroadcast(time_tick));	//sending a tick broadcast each tick
		}
		try {
			TimeUnit.MILLISECONDS.sleep(TICK_DELAY);		// last tick is a terminate broadcast
		} catch (InterruptedException e) {	}
		getSimplePublisher().sendBroadcast(new TerminateBroadcast());
		//------------end edit - 20/12----------------------**/
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
