package bgu.spl.mics;

import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.subscribers.M;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	//------------start edit - 21/12 --------------------**/
		//private ConcurrentHashMap<Subscriber,Semaphore> subscriber_semaphore_map;
		//		//each subscriber has its own semaphore. need to catch it to delete for example
	private ConcurrentHashMap< Subscriber, ConcurrentLinkedQueue<Message> > subscriber_msg_type_map;
			//each subscriber will go into map and have it's own Queue
			//  Semaphore is for fairness!!!
	private ConcurrentHashMap < Class<? extends Event> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> events_q_map;
			//each topic has its own events
	private ConcurrentHashMap < Class<? extends Broadcast> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> broadcast_q_map;
			//each broadcast has its own events
	private ConcurrentHashMap <Event,Future> future_event_map;
			//each event has it's own future object
	private boolean terminate_received;			//for checking termination msg

	private String M_Q;//TODO: DELETE THIS TEST

	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static MessageBroker mb_instance = new MessageBrokerImpl();
	} // get instance like TIRGUL 8
	//------------end edit - 21/12----------------------**/

	//------------start edit - 17/12 --------------------**/
	/** Constructor */
	private MessageBrokerImpl(){
		//subscriber_semaphore_map = new ConcurrentHashMap<>();
		subscriber_msg_type_map = new ConcurrentHashMap<>();
		broadcast_q_map = new ConcurrentHashMap<>();
		events_q_map = new ConcurrentHashMap<>();
		future_event_map = new ConcurrentHashMap<>();
		terminate_received = false;
		M_Q="";	//TODO: DELETE THIS TEST
	}
	//------------end edit - 17/12----------------------**/
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		//------------start edit - 17/12 --------------------**/
		return SingletonHolder.mb_instance;
		//------------end edit - 17/12----------------------**/
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		//------------start edit - 18/12 --------------------**/
		synchronized (m){	// Lock the subscriber
			if(subscriber_msg_type_map.containsKey(m)) {

				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair =
						new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>()); 	//created new pair - if no such exists will get inside the map
				events_q_map.putIfAbsent(type, tmp_pair);		//if this pair already exists - returns the value of the pair,
				                                                        // else, returns null and creates it
				events_q_map.get(type).getValue().add(m);				//adds m to the EVENT Q
				/**CANCELED*/ //subscriber_msg_type_map.get(m).getValue().add(type);	// added to class database
				                                                        //adding event q the subscriber m
			}
		}
		//------------end edit - 18/12----------------------**/
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		//------------start edit - 18/12 --------------------**/
		synchronized (m) {    // Lock the subscriber
			if (subscriber_msg_type_map.containsKey(m)) {
				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair =
						new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>());		//created new pair - if no such exists will get inside the map
				broadcast_q_map.putIfAbsent(type, tmp_pair);			//if this pair already exists - returns the value of the pair,
																		//else, returns null and creates it
				broadcast_q_map.get(type).getValue().add(m);			//adds m to the BROADCAST Q
				/**CANCELED*/ //subscriber_msg_type_map.get(m).getValue().add(type);    // added to class database
																		//adding event q the subscriber m
			}
		}
			//------------end edit - 18/12----------------------**/
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		//------------start edit - 18/12 --------------------**/
		future_event_map.get(e).resolve(result);            //this hashmap is already thread safe
		future_event_map.remove(e);                            //removing the resolved event
		//------------end edit - 18/12----------------------**/
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//------------start edit - 18/12 --------------------**/
		if(b.getClass().isInstance(TerminateBroadcast.class))		//changing the flag for the terminate broadcast!
			terminate_received = true;

		if(broadcast_q_map.containsKey(b.getClass())){							//checks if broadcast still in the topic map
			Pair <Semaphore,ConcurrentLinkedQueue<Subscriber>> broadcast_pair = broadcast_q_map.get(b.getClass());		//for iterator on broadcast_pair queue of subscribers
			for (Subscriber sub: broadcast_pair.getValue()) {
				synchronized (sub) { 											// synchronized THE OBJECT SUBSCRIBER

					if (subscriber_msg_type_map.containsKey(sub)) {                //checks if subscriber is in UNREGISTER proccess
						//if (sub.getClass() == M.class)    //TODO: DELETE THIS TEST
						//	M_Q=M_Q+"\n"+b.toString().substring(34);//TODO: DELETE THIS TEST
						subscriber_msg_type_map.get(sub).add(b);        // add the message b to sub queue
						sub.notifyAll();                                            // awake for the AWAIT MESSAGE
					}
				}
			}
		}
		//------------end edit - 18/12----------------------**/
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		//------------start edit - 21/12 --------------------**/
		//if(e.getClass()==MissionReceivedEvent.class) {                //TODO: DELETE THIS TEST
		//	System.out.println("Mission " + (((MissionReceivedEvent) e).getMissionInfo().getMissionName() + " , has arrived to ....... "));    //TODO: DELETE THIS TEST
		//	System.out.println("events_q_map has the key of the class "+e.getClass().toString().substring(34)+"? "+ events_q_map.containsKey(e.getClass())+" \n"+
		//			"in events_q_map, the queue of event "+e.getClass().toString().substring(34)+" is empty? "+events_q_map.get(e.getClass()).getValue().isEmpty()); //TODO: DELETE THIS TEST
		//}
		if ((!events_q_map.containsKey(e.getClass())) || (events_q_map.get(e.getClass()).getValue().isEmpty())) {    // no such event, or no subscriber's queue
			return null;
		} else {
			future_event_map.put(e, new Future<T>());                            // adding new <event,future> to the map
			Future<T> future_event = future_event_map.get(e);
			try{
				Subscriber sub = events_q_map.get(e.getClass()).getValue().peek();
				synchronized ( sub ) {
					/**Sync cause: watch for unregistering**/
					Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> event_pair = events_q_map.get(e.getClass());
					try {
						event_pair.getKey().acquire();									 // catching the semaphore for fairness in deQ and enQ

						Subscriber first = event_pair.getValue().poll();                 //dequeue first subscriber
						event_pair.getValue().add(first);                                //enqueue first to be the last

						//if(sub.getClass()==M.class)									//TODO: DELETE THIS TEST
						//	M_Q=M_Q+"\n"+e.toString().substring(34);				//TODO: DELETE THIS TEST
						if(e.getClass()==MissionReceivedEvent.class)				//TODO: DELETE THIS TEST
							System.out.println("Mission "+(((MissionReceivedEvent) e).getMissionInfo().getMissionName()+ " , has arrived to "+sub.getName()));	//TODO: DELETE THIS TEST

						subscriber_msg_type_map.get(sub).add(e);                         // add the message e to sub queue

						sub.notifyAll();                                                    // awake for the AWAIT MESSAGE

						event_pair.getKey().release();
					} catch (InterruptedException ex) {
						//ex.printStackTrace();
					}
				}
				//future_event.get();
				return future_event;

			}catch (NullPointerException er){
				return null;
			}
		}
	}
		//------------end edit - 21/12----------------------**/


	@Override
	public void register(Subscriber m) {
		//------------start edit - 19/12 --------------------**/
		synchronized (m){						// each registration will lock the subscriber in case of unregistering
			subscriber_msg_type_map.put(m, new ConcurrentLinkedQueue<>());	// creating new subscriber
		}
		//------------end edit - 19/12----------------------**/
	}

	@Override
	public void unregister(Subscriber m) {
		//------------start edit - 19/12 --------------------**/
		synchronized (m){
			for(Message msg : subscriber_msg_type_map.get(m) ){	// releasing all future objects that never been answered by 'm'
				try {
					future_event_map.get(msg).resolve(null);            // this is the future of the event/broadcast in the m queue
				}catch(NullPointerException er) { }
			}
			subscriber_msg_type_map.remove(m);
			for(Map.Entry< Class<? extends Event> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> entry_event : events_q_map.entrySet()) {    //running on the Pairs <Semaphore, Q>
				if(entry_event.getValue().getValue().contains(m))
					entry_event.getValue().getValue().remove(m);
			}//removing subscriber m from X_event q
			for(Map.Entry< Class<? extends Broadcast> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> entry_broadcast : broadcast_q_map.entrySet()) {    //running on the Pairs <Semaphore, Q>
				if(entry_broadcast.getValue().getValue().contains(m))
					entry_broadcast.getValue().getValue().remove(m);       //removing subscriber m from X_broadcast q
			}
		}
		//------------end edit - 19/12----------------------**/
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		//------------start edit - 21/12 --------------------**/
		synchronized (m){
			if(!subscriber_msg_type_map.containsKey(m))					//if the subscriber is NOT in the hash table
				throw new InterruptedException();
			while(subscriber_msg_type_map.get(m).isEmpty()) {            //the q is empty, so wait for a message
				m.wait();												// wait loop...
			}
		Message msg = subscriber_msg_type_map.get(m).poll();        // pool your message
		return msg;        // send your message
		}
		//------------end edit - 21/12----------------------**/
	}

	//---------------TODO: DELETE THIS-------------------
	public void clear() {

		subscriber_msg_type_map = new ConcurrentHashMap<>();
		broadcast_q_map = new ConcurrentHashMap<>();
		events_q_map = new ConcurrentHashMap<>();
		future_event_map = new ConcurrentHashMap<>();
		terminate_received = false;
		M_Q = "";    //TODO: DELETE THIS TEST
	}
	//---------------TODO: DELETE THIS-------------------
}
