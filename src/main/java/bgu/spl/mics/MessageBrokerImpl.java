package bgu.spl.mics;

import javafx.util.Pair;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	//------------start edit - 17/12 --------------------**/
	MessageBrokerImpl mb_instance = null;
	private ConcurrentHashMap< Subscriber, BlockingDeque<Message>> subscriber_q_map;
			//each subscriber will go into map and have it's own Q
	private ConcurrentHashMap < Class<? extends Event> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> topics_events_map;
			//each topic has its own events
	private ConcurrentHashMap < Class<? extends Broadcast> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> topics_broadcast_map;
			//each broadcast has its own events
	private ConcurrentHashMap <Event,Future> future_event_map;
			//each event has it's own future object

	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static MessageBroker mb_instance = new MessageBrokerImpl();
	} // get instance like TIRGUL 8

	/** Constructor */
	private MessageBrokerImpl(){
		subscriber_q_map = new ConcurrentHashMap<>();
		topics_broadcast_map = new ConcurrentHashMap<>();
		topics_events_map = new ConcurrentHashMap<>();
		future_event_map = new ConcurrentHashMap<>();
	}
	//------------end edit - 17/12----------------------**/
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		//TODO: Implement this
		//------------start edit - 17/12 --------------------**/
		return SingletonHolder.mb_instance;
		//------------end edit - 17/12----------------------**/
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub
		//------------start edit - 17/12 --------------------**/

		//------------end edit - 17/12----------------------**/
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub
		//------------start edit - 17/12 --------------------**/

		//------------end edit - 17/12----------------------**/
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
		//------------start edit - 17/12 --------------------**/

		//------------end edit - 17/12----------------------**/
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		//------------start edit - 17/12 --------------------**/

		//------------end edit - 17/12----------------------**/
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	//------------start edit - 17/12 --------------------**/

	//------------end edit - 17/12----------------------**/
	

}
