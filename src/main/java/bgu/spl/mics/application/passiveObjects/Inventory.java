package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private List<String> gadgets;
	//------------start edit - 20/12-------------------
	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static Inventory inventory_instance = new Inventory();
	} // get instance like TIRGUL 8

	/** Constructor */
	private Inventory(){
		gadgets = new LinkedList<>();
	}
	//------------end edit - 20/12---------------------
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		//------------start edit - 20/12-------------------
		return SingletonHolder.inventory_instance;
		//------------end edit - 20/12---------------------
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		//------------start edit - 20/12-------------------
		for(int i=0; i<inventory.length; i++){
			gadgets.add(inventory[i]);
		}
		//------------end edit - 20/12---------------------
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		//------------start edit - 20/12 -------------------
		synchronized (gadgets) {	//locking all the gadgets for safety - may find a gadget and than before i'll take it, it will be deleted
			for (String curr : gadgets) {    // for each
				if (curr.equals(gadget)) {
					gadgets.remove(curr);	//remove this gadget from the gadgets list
					return true;
				}
			}
		}
		//------------end edit - 20/12 ---------------------
		return false;
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		//TODO: Implement this
	}

	//------------start edit -------------------
	//public List<String> getGadgets (){
	//	return gadgets;
	//}

	//------------end edit ---------------------
}
