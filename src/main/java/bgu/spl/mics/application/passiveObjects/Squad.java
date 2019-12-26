package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	//------------start edit -20/12 -------------------
	private ConcurrentHashMap<String, Semaphore> agents_semaphore_map;
	private boolean terminated;

	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static Squad squad_instance = new Squad();
	} // get instance like TIRGUL 8

	/** Constructor */
	private Squad(){
		this.agents = new HashMap<>();
		this.agents_semaphore_map = new ConcurrentHashMap<>();
		this.terminated = false;
	}

	//------------end edit - 20/12 ---------------------
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		//------------start edit - 20/12-------------------
		return SingletonHolder.squad_instance;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		//------------start edit - 20/12-------------------
		for(int i=0; i<agents.length; i++){
			this.agents.put(agents[i].getSerialNumber(),agents[i]);		//insert the agents to the map of agents
			this.agents_semaphore_map.put(agents[i].getSerialNumber() , new Semaphore(1,true));		//creating map of agent's semaphore, to manage fairness in the getAgents method
		}
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) throws InterruptedException {
		/**Assumption: DONE in M at terminate broadcast**/
		//------------start edit - 20/12 -------------------
		for(String SN: serials){
			this.agents.get(SN).release();						//change the value of isAvailable to true
			this.agents_semaphore_map.get(SN).release();		// wake up all threads that has been waiting to this agent
/**EDIT*/	synchronized (agents.get(SN)) {
				this.agents.get(SN).notifyAll();		//notfiy the agent for the getAgents method
/**EDIT*/	}
		}
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   time ticks to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
		/** ASSUMPTION: will happen only after getAgents will return true*/
		/** no worry of TERMINATE: we already checked that we have enough time */
		//------------start edit - 20/12 -------------------
		for(String curr_serial: serials){				/** SHOULD BE ALREADY FALSE IN THE AGENT FIELD*/
			agents.get(curr_serial).acquire(); 			//acquired agent from AGENT.JAVA
		}	// redundant...

		//sleep for time* 100 (as said in the forum SendAgents...)
		sleep(time*100);

		for(String curr_serial: serials){				// SHOULD BE ALREADY FALSE IN THE AGENT FIELD
			agents.get(curr_serial).release(); 			//released agent from AGENT.JAVA
		}	//now the agents are again available
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) throws InterruptedException {
		/** ASSUMPTION: only this function is locked and only it TRUE will cause sendAgentsEvent (and more) from the M class */
		//------------start edit - 20/12 -------------------
		for(String curr_serial: serials){	//checks if the agents are in the squad
			if(!agents.containsKey(curr_serial))
				return false;
		}
		serials.sort(String::compareTo);	//sorting serialNumbers by lexicographic why
		for(String curr_serial:serials){
			Agent curr_agent = agents.get(curr_serial);							//get agent
			Semaphore curr_semaphore = agents_semaphore_map.get(curr_serial);	//get it's semaphore

			curr_semaphore.acquire(); /***/
	/**EDIT*/synchronized (curr_agent){		//synch current agent, for waiting on it in the next line
				while(!curr_agent.isAvailable() & !terminated) {		// if not avail, wait.  //termination - EXIT and return false
					curr_agent.wait();					// wait on the current agent - NOT RELEASEING THE SEMAPHORE
					/**BEWARE: may stuck there when moneypenny terminated!!! */
					/** Solution:  added field terminated to */
	/**EDIT*/	}
			}
			if(terminated) {				//termination skipps acquire
				curr_semaphore.release();
				return false;
			}
			curr_agent.acquire();					//agent is now available = false
			curr_semaphore.release();	/***/    	//unlocking the semaphore

		}
		return true;
		//------------end edit - 20/12 ---------------------
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		//------------start edit -------------------
		List<String> names = new LinkedList<>();
		for (String curr:serials) {
			names.add(agents.get(curr).getName());
		}
		return names;
		//------------end edit ---------------------
    }

	//------------start edit - 20/12 -------------------
	//getter of the agents map
	public Map<String,Agent> getAgentsMap (){	return agents;	}

	//getter of the terminate field
	public boolean terminated(){		return terminated;}

	//setter of the terminate field
	public void terminate(){
    	this.terminated=true;
    }

    //getter of the agents semaphore map
	public ConcurrentHashMap<String, Semaphore> getAgentSemaphoreMap(){
    	return agents_semaphore_map;
	}
	//------------end edit - 20/12---------------------
}
