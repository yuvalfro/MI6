package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	//------------start edit - 19/12 --------------------**/
	private String name;
	private List<String> serialAgentsNumbers;
	private String gadget;
	private int timeIssued;
	private int timeExpired;
	private int duration;
	//------------end edit - 19/12----------------------**/


	//------------start edit - 19/12 --------------------**/
	//Constructor
	/** why we has constructor if we has setters to everything */
	public MissionInfo(String name, List<String> serialAgentsNumbers, String gadget, int timeIssued, int timeExpired, int duration){
		this.name = name;
		this.serialAgentsNumbers= serialAgentsNumbers;
		this.gadget=gadget;
		this.timeIssued= timeIssued;
		this.timeExpired= timeExpired;
		this.duration= duration;
	}
	//------------end edit - 19/12----------------------**/

    /**
     * Sets the name of the mission.
     */
    public void setname(String name) {
    	//------------start edit - 20/12 --------------------**/
		this.name=name;
		//------------end edit - 20/12----------------------**/
    }

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		//------------start edit - 20/12 --------------------**/
		return name;
		//------------end edit - 20/12----------------------**/
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
		//------------start edit - 20/12 --------------------**/
		this.serialAgentsNumbers=serialAgentsNumbers;
		//------------end edit - 20/12----------------------**/
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		//------------start edit - 20/12 --------------------**/
		return serialAgentsNumbers;
		//------------end edit - 20/12----------------------**/
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
		//------------start edit - 20/12 --------------------**/
		this.gadget=gadget;
		//------------end edit - 20/12----------------------**/
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		//------------start edit - 20/12 --------------------**/
		return gadget;
		//------------end edit - 20/12----------------------**/
	}

    /**
     * Sets the time the mission was issued in time ticks.
     */
    public void setTimeIssued(int timeIssued) {
		//------------start edit - 20/12 --------------------**/
		this.timeIssued=timeIssued;
		//------------end edit - 20/12----------------------**/
    }

	/**
     * Retrieves the time the mission was issued in time ticks.
     */
	public int getTimeIssued() {
		//------------start edit - 20/12 --------------------**/
		return timeIssued;
		//------------end edit - 20/12----------------------**/
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
		//------------start edit - 20/12 --------------------**/
		this.timeExpired=timeExpired;
		//------------end edit - 20/12----------------------**/
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		//------------start edit - 20/12 --------------------**/
		return timeExpired;
		//------------end edit - 20/12----------------------**/
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
		//------------start edit - 20/12 --------------------**/
		this.duration=duration;
		//------------end edit - 20/12----------------------**/
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		//------------start edit - 20/12 --------------------**/
		return duration;
		//------------end edit - 20/12----------------------**/
	}
}
