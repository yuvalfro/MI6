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

	//TODO: change pair to ARRAYLIST!#!@#!@#!@#!@#!@#!#!@#!@#!@#!@#

	MessageBrokerImpl mb_instance = null;
		//private ConcurrentHashMap<Subscriber,Semaphore> subscriber_semaphore_map;
		//		//each subscriber has its own semaphore. need to catch it to delete for example
	private ConcurrentHashMap< Subscriber, Pair<ConcurrentLinkedQueue<Message> , ConcurrentLinkedQueue<Class<? extends Message>>>> subscriber_msg_type_map;
			//each subscriber will go into map and have it's own Q   &   types of messages to find when delete
			//  Semaphore is for fairness!!!
	private ConcurrentHashMap < Class<? extends Event> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> events_q_map;
			//each topic has its own events
	private ConcurrentHashMap < Class<? extends Broadcast> , Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>>> broadcast_q_map;
			//each broadcast has its own events
	private ConcurrentHashMap <Event,Future> future_event_map;
			//each event has it's own future object

	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static MessageBroker mb_instance = new MessageBrokerImpl();
	} // get instance like TIRGUL 8
	//------------end edit - 17/12----------------------**/

	//------------start edit - 17/12 --------------------**/
	/** Constructor */
	private MessageBrokerImpl(){
		//subscriber_semaphore_map = new ConcurrentHashMap<>();
		subscriber_msg_type_map = new ConcurrentHashMap<>();
		broadcast_q_map = new ConcurrentHashMap<>();
		events_q_map = new ConcurrentHashMap<>();
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
		//------------start edit - 18/12 --------------------**/
		synchronized (m){	// Lock the subscriber
			if(subscriber_msg_type_map.containsKey(m)) {
				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair =
						new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>());
				//created new pair - if no such exists will get inside the map
				events_q_map.putIfAbsent(type, tmp_pair);				//if this pair already exists - returns the value of the pair,
				                                                        // else, returns null and creates it
				events_q_map.get(type).getValue().add(m);				//adds m to the EVENT Q
				subscriber_msg_type_map.get(m).getValue().add(type);	// added to class database
				                                                        //adding event q the subscriber m
			}
		}
	/*	try {
			subscriber_semaphore_map.get(m).acquire();
		} catch (InterruptedException e) {}


		try {
			mainLock.acquire();
			//ACQUIRED mainlock
			if(subscriber_semaphore_map.containsKey(m)) {
				subscriber_semaphore_map.get(m).acquire();
					//ACQUIRED subscriber m
				mainLock.release();
				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair = new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>());
					//created new pair - if no such exists will get inside the map
				events_q_map.putIfAbsent(type, tmp_pair);
					//if this pair already exists - returns the value of the pair,
					//else, returns null and creates it
				events_q_map.get(type).getValue().add(m);
					//adds m to the EVENT Q
				subscriber_msg_type_map.get(m).getValue().add(type);	// added to class database
					//adding event q the subscriber m
				subscriber_semaphore_map.get(m).release();
					//released
			}
			else{
				mainLock.release();
				//released
			}
		} catch (InterruptedException e) {}
		*/
		//------------end edit - 18/12----------------------**/
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub
		//------------start edit - 18/12 --------------------**/
		synchronized (m) {    // Lock the subscriber
			if (subscriber_msg_type_map.containsKey(m)) {
				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair =
						new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>());
				//created new pair - if no such exists will get inside the map
				broadcast_q_map.putIfAbsent(type, tmp_pair);
				//if this pair already exists - returns the value of the pair,
				//else, returns null and creates it
				broadcast_q_map.get(type).getValue().add(m);
				//adds m to the BROADCAST Q
				subscriber_msg_type_map.get(m).getValue().add(type);    // added to class database
				//adding event q the subscriber m
			}
		}
		/*
		try {
			mainLock.acquire();
			//ACQUIRED mainlock
			if(subscriber_semaphore_map.containsKey(m)) {
				subscriber_semaphore_map.get(m).acquire();
				//ACQUIRED subscriber m
				mainLock.release();
				Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> tmp_pair = new Pair<>(new Semaphore(1, true), new ConcurrentLinkedQueue<Subscriber>());
				//created new pair - if no such exists will get inside the map
				broadcast_q_map.putIfAbsent(type, tmp_pair);
				//if this pair already exists - returns the value of the pair,
				//else, returns null and creates it
				broadcast_q_map.get(type).getValue().add(m);
				//adds m to the BROADCAST Q
				subscriber_msg_type_map.get(m).getValue().add(type);	// added to class database
				//adding event q the subscriber m
				subscriber_semaphore_map.get(m).release();
				//released
			}
			else{
				mainLock.release();
				//released
			}
		} catch (InterruptedException e) {}
		*/
			//------------end edit - 18/12----------------------**/
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
		//------------start edit - 18/12 --------------------**/
		future_event_map.get(e).resolve(result);
			//this hashmap is already thread safe
		future_event_map.remove(e);
			//removing the resolved event
		//------------end edit - 18/12----------------------**/
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		//------------start edit - 18/12 --------------------**/
		if(broadcast_q_map.containsKey(b.getClass())){						//checks if broadcast still in the topic map
			Pair <Semaphore,ConcurrentLinkedQueue<Subscriber>> broadcast_pair = broadcast_q_map.get(b.getClass());
			//for iterator on broadcast_pair queue of subscribers
			for (Subscriber sub: broadcast_pair.getValue()) {
				synchronized (sub) { 											// synchronized THE OBJECT SUBSCRIBER
					if (subscriber_msg_type_map.containsKey(sub)) {				//checks if subscriber is in UNREGISTER proccess
						subscriber_msg_type_map.get(sub).getKey().add(b);		// add the message b to sub queue
						sub.notify();											// awake for the AWAIT MESSAGE
					}
				}
			}
		}
	/*	if(broadcast_q_map.containsKey(b.getClass())){
			//checks if broadcast still in the topic map
			Pair <Semaphore,ConcurrentLinkedQueue<Subscriber>> broadcast_pair = broadcast_q_map.get(b.getClass());
			//for iterator on broadcast_pair queue of subscribers
			//TODO: should we lock the semaphore for broadcast Q safety?????????
			for (Subscriber sub: broadcast_pair.getValue()) {
				if(subscriber_msg_type_map.containsKey(sub)){
					//the subscriber is alive! now synchronized THE OBJECT SUBSCRIBER from sub_q_map and check again
					synchronized (subscriber_msg_type_map.get(sub)){
						if(subscriber_msg_type_map.containsKey(sub)) {
							subscriber_msg_type_map.get(sub).getKey().add(b);
							// add the message b to sub queue
						}
					}
				}
			}
		}
		*/
		//------------end edit - 18/12----------------------**/
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		//------------start edit - 18/12 --------------------**/
		if( (!events_q_map.containsKey(e.getClass())) || (events_q_map.get(e.getClass()).getValue().peek()==null) ) {
			// no such event, or no subscriber's queue
			return null;
		}
		else {
			Future<T> future_event;
			future_event = null;
			Subscriber sub = events_q_map.get(e.getClass()).getValue().peek();
			Pair<Semaphore, ConcurrentLinkedQueue<Subscriber>> event_pair = events_q_map.get(e.getClass());
			while (sub != null) {
				synchronized (sub) {
					try {
						event_pair.getKey().acquire();
					} catch (InterruptedException ex) {
					}
					if ((sub == event_pair.getValue().peek()) & (subscriber_msg_type_map.containsKey(sub))) {

						Subscriber first = event_pair.getValue().poll();				//dequeue first subscriber
						event_pair.getValue().add(first);								//enqueue first to be the last

						subscriber_msg_type_map.get(sub).getKey().add(e);				// add the message b to sub queue
						sub.notify();													// awake for the AWAIT MESSAGE

						future_event = new Future <T>();
						future_event_map.put(e, future_event);							// adding new <event,future> to the map
					}
				}
				if (future_event != null)
					return future_event;
				else
					sub = events_q_map.get(e.getClass()).getValue().peek();
			}
			return null;
		}
		/*
			Pair <Semaphore,ConcurrentLinkedQueue<Subscriber>> event_pair = events_q_map.get(e.getClass());
				// event pair - its a semaphore and queue of subs
			Subscriber sub = events_q_map.get(e.getClass()).getValue().peek();
			//TODO: watch thread safe when others touching the queue
			synchronized (sub){											//synchronized the first on the queue
				try {
					event_pair.getKey().acquire();  					// ACQUIRED Semaphore queue of specified event
					if (subscriber_msg_type_map.containsKey(sub)) {		//checks if subscriber is in UNREGISTER proccess
						subscriber_msg_type_map.get(sub).getKey().add(e);
								// add the message b to sub queue
						Future<T> future_event = new Future<>();
						future_event_map.put(e, future_event);
								// adding new <event,future> to the map
						Subscriber first = event_pair.getValue().poll();
								//dequeue first subscriber
						event_pair.getValue().add(first);
								//enqueue first to be the last
						event_pair.getKey().release();
								// RELEASED semaphore
						return future_event;
					}
					return null;
				} catch (InterruptedException ex) {	}
			}
		}
		*/

		/*
		if( (!events_q_map.containsKey(e.getClass())) || (events_q_map.get(e.getClass()).getValue().peek()==null) ) {
			// no such event, or no subscriber's queue
			return null;
		}
		try {
			mainLock.acquire();

		} catch (InterruptedException e) {}
		*/

		/*Pair <Semaphore,ConcurrentLinkedQueue<Subscriber>> event_pair = events_q_map.get(e.getClass());
		event_pair.getKey().tryAcquire();
			// acquired the semaphore - will dequeue and enqueue!!!
		if(event_pair.getValue().contains(e)) {
				//checking if the event STILL there
			Future<T> future_event = new Future<>();
			future_event_map.put(e, future_event);
				// adding new <event,future> to the map
			Subscriber first = event_pair.getValue().poll();
				//dequeue first subscriber
			event_pair.getValue().add(first);
				//enqueue first to be the last
			subscriber_msg_type_map.get(first).getKey().add(e);
				//add the message to 'first' queue
			event_pair.getKey().release();
			return future_event;
		}
		else{
			event_pair.getKey().release();
			return null;
		}
		*/
		//------------end edit - 18/12----------------------**/
	}

	@Override
	public void register(Subscriber m) {
		// TODO Auto-generated method stub
		//------------start edit - 19/12 --------------------**/
		synchronized (m){
			subscriber_msg_type_map.put(m, new Pair (new ConcurrentLinkedQueue<>(), new ConcurrentLinkedQueue<>()));	// creating new subscriber1
		}
		//------------end edit - 19/12----------------------**/
	}

	@Override
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub
		//------------start edit - 19/12 --------------------**/
		synchronized (m){
			ConcurrentLinkedQueue<Class<? extends Message>> class_q_of_sub = subscriber_msg_type_map.get(m).getValue(); // getting classes q of m
			subscriber_msg_type_map.remove(m, new Pair (new ConcurrentLinkedQueue<>(), new ConcurrentLinkedQueue<>()));	// creating new subscriber1
			for( Class<? extends Message> class_msg: class_q_of_sub){			//for each kind of class_type that subscribed
				ConcurrentLinkedQueue<Subscriber> curr_sub_q;
				if(class_msg.isInstance(Broadcast.class))						//create new q of the specified msg
					curr_sub_q = broadcast_q_map.get(class_msg).getValue();
				else
					curr_sub_q = events_q_map.get(class_msg).getValue();
				curr_sub_q.remove(m);											//remove the subscriber from the q
			}
		}
		//------------end edit - 19/12----------------------**/
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		//------------start edit - 19/12 --------------------**/
		synchronized (m){
			if(!subscriber_msg_type_map.containsKey(m))
				throw new InterruptedException();
			while(subscriber_msg_type_map.get(m).getKey().isEmpty())	//the q is empty, so wait for a message
				subscriber_msg_type_map.get(m).getKey().wait();
			return subscriber_msg_type_map.get(m).getKey().poll();
		}
		//------------end edit - 19/12----------------------**/
	}


	

}
