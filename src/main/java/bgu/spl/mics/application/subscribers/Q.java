package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//TODO : make sure that there are ONLY 1 PERMIT on the number of INSTANCES! fix it!
	//------------start edit - 19/12 --------------------**/
	private static int q_count=1;
	//------------end edit - 19/12----------------------**/

	public Q() {
		// TODO Implement this
		//------------start edit - 19/12 --------------------**/
		super("Q"+q_count);	// Q number #
		q_count++;
		//------------end edit - 19/12----------------------**/
	}

	@Override
	protected void initialize() {
		// TODO Implement this
		//------------start edit - 19/12 --------------------**/
		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) ->
		{
			//TODO implement what to do when Q got an event
			complete(e , Inventory.getInstance().getItem(e.getNeededGadget()));
			//getting the gadget for the mission, and adding result to this event
			// e.getNeededGadget is the function of the class GadgetAvailableEvent
		} );
		//------------end edit - 19/12----------------------**/
	}

}
