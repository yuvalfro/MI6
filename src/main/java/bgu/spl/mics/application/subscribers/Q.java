package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.ArrayList;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//made sure that there are ONLY 1 PERMIT on the number of INSTANCES!
	//------------start edit - 20/12 --------------------**/
	private int curr_tick;
	private static int q_count=1;
	private boolean terminate = false;
	//------------end edit - 20/12----------------------**/

	public Q() {
		//------------start edit - 20/12 --------------------**/
		super("Q"+q_count);	// Q number #
		q_count++;
		/**assumption: should we implement this as a "getInstance" or just delete because we created only 1 anyway **/
		/** after the first build of the Q, we will insert it to the singleton holder*/
		//------------end edit - 20/12----------------------**/
	}

	@Override
	protected void initialize() {
		//------------start edit - 19/12 --------------------**/
		//using lambda expression
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			this.curr_tick = b.getCurrTick();
		});	//this broadcast is a TickBroadcast which informs us of an update on the time ticks

		//using lambda expression
		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) ->
		{
			ArrayList<Object> tmp_arraylist = new ArrayList<>();
			if(!terminate)
				tmp_arraylist.add( Inventory.getInstance().getItem(e.getNeededGadget()) );				//getting the gadget for the mission, and adding result to this event
			else
				tmp_arraylist.add ( false);				// to the end of time, to notify M

			tmp_arraylist.add( curr_tick );				//QTime
			tmp_arraylist.add( e.getNeededGadget() );	//gadget string
	/** System.out.println("Q at tick time: " +curr_tick);	//TODO: REMOVE THIS TEST   **/
			complete(e , tmp_arraylist);			//e has future <pair of (QTime , Gadget string>
			// Future is < boolean of the gadget - if its there ,  QTime that it got the message at , the String of the gadget  >
			// e.getNeededGadget is the function of the class GadgetAvailableEvent
		} );

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast e) ->
		{
			terminate = true;
			this.terminate();
		});
		//------------end edit - 19/12----------------------**/
	}

}
