package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
	//------------start edit -------------------
	private String serialNumber;
	private String name;
	private boolean available=true;

	/** Constructor */
	// TODO check threads
	/*public Agent(String SerialNumber, String Name){
		this.SerialNumber=SerialNumber;
		this.Name=Name;
		this.available=true;
	} */
	//------------end edit ---------------------

	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		// TODO check threads
		//------------start edit -------------------
		this.serialNumber=serialNumber;
		//------------end edit ---------------------
	}

	/**
     * Retrieves the serial number of an agent.
     * <p>
     * @return The serial number of an agent.
     */
	public String getSerialNumber() {
		// TODO check threads
		//------------start edit -------------------
		return this.serialNumber;
		//------------end edit ---------------------
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		// TODO check threads
		//------------start edit -------------------
		this.name=name;
		//------------end edit ---------------------
	}

	/**
     * Retrieves the name of the agent.
     * <p>
     * @return the name of the agent.
     */
	public String getName() {
		// TODO check threads
		//------------start edit -------------------
		return this.name;
		//------------end edit ---------------------
	}

	/**
     * Retrieves if the agent is available.
     * <p>
     * @return if the agent is available.
     */
	public boolean isAvailable() {
		// TODO check threads
		//------------start edit -------------------
		return this.available;
		//------------end edit ---------------------
	}

	/**
	 * Acquires an agent.
	 */
	public void acquire(){
		// TODO check threads
		//------------start edit -------------------
		this.available = false;
		//------------end edit ---------------------
	}

	/**
	 * Releases an agent.
	 */
	public void release(){
		// TODO check threads
		//------------start edit -------------------
		this.available = true;
		//------------end edit ---------------------
	}
}
