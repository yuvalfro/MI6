package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.publishers.TimeService;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//made sure that there are ONLY 1 PERMIT on the number of INSTANCES! fix it!
	//------------start edit - 20/12 --------------------**/
	private int curr_tick;
	private static int q_count=1;

		/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static Q q_instance;
	} // get instance like TIRGUL 8
	//------------end edit - 20/12----------------------**/

	public Q() {
		// TODO Implement this
		//------------start edit - 20/12 --------------------**/
		super("Q"+q_count);	// Q number #
		q_count++;
		SingletonHolder.q_instance = this;
		/** after the first build of the Q, we will insert it to the singleton holder*/
		//------------end edit - 20/12----------------------**/
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
		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) ->
		{
			ArrayList<Object> tmp_arraylist = new ArrayList<>();
			tmp_arraylist.add( Inventory.getInstance().getItem(e.getNeededGadget()) );				//getting the gadget for the mission, and adding result to this event
			tmp_arraylist.add( curr_tick );				//QTime
			tmp_arraylist.add( e.getNeededGadget() );	//gadget string
			System.out.println("Q at tick time: " +curr_tick);	//TODO: REMOVE THIS TEST
			complete(e , tmp_arraylist);			//e has future <pair of (QTime , Gadget string>
			// Future is < boolean of the gadget - if its there ,  QTime that it got the message at , the String of the gadget  >
			// e.getNeededGadget is the function of the class GadgetAvailableEvent
		} );

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast e) ->
		{
			this.terminate();
		});
		//------------end edit - 19/12----------------------**/
	}

}
