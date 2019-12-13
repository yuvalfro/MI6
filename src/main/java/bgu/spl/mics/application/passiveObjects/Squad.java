package bgu.spl.mics.application.passiveObjects;
import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	//------------start edit -------------------
	private static Squad instance = null;

	/** Constructor */
	private Squad(){
		//TODO - maybe edit
		agents =  new HashMap< String,Agent>();
	}
	//------------end edit ---------------------

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		//------------start edit -------------------
		if (instance == null)
			instance = new Squad();
		return instance;
		//------------end edit ---------------------
		//return null;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		//------------start edit -------------------
		for(int i=0; i<agents.length; i++){
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
		//------------end edit ---------------------
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		// TODO Check Threads
		//------------start edit -------------------
		for(String SN: serials){
			try {
				this.agents.get(SN).release();
			}catch(Exception e){
				System.out.println(SN +" serial number doesn't exist.");
			}
		}
		//------------end edit ---------------------
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		// TODO MIND of deadlock & deadpool!!  needs to wait until they all available.
		//------------start edit -------------------
		return false;
		//------------end edit ---------------------
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		// TODO Check Threads
		//------------start edit -------------------
				/** OMER edit 13/12 **/
		List<String> names = new LinkedList<>();
		for (String curr:serials) {
			names.add(agents.get(curr).getName());
		}
		return names;
				/** OMER end edit 13/12 **/
		//------------end edit ---------------------
    }


	//------------start edit -------------------
	public Map<String,Agent> getAgentsMap (){
		return agents;
	}
	//------------end edit ---------------------
}
