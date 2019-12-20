package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
	//------------start edit - 20/12-------------------
	private String MissionName;
	private int M;
	private int Moneypenny;
	private List<String> AgentsSerialNumbers;
	private List<String> AgentsNames;
	private String GadgetName;
	private int QTime;
	private int TimeIssued;
	private int TimeCreated;


	/** Constructor */
	//------------end edit - 20/12 ---------------------
	/**
     * Retrieves the mission name.
     */
	public String getMissionName() {
		//------------start edit - 20/12-------------------
		return MissionName;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the mission name.
	 */
	public void setMissionName(String missionName) {
		//------------start edit - 20/12-------------------
		this.MissionName = missionName;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the M's id.
	 */
	public int getM() {
		//------------start edit - 20/12-------------------
		return M;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the M's id.
	 */
	public void setM(int m) {
		//------------start edit - 20/12-------------------
		this.M=m;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the Moneypenny's id.
	 */
	public int getMoneypenny() {
		//------------start edit - 20/12-------------------
		return Moneypenny;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the Moneypenny's id.
	 */
	public void setMoneypenny(int moneypenny) {
		//------------start edit - 20/12-------------------
		this.Moneypenny=moneypenny;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the serial numbers of the agents.
	 * <p>
	 * @return The serial numbers of the agents.
	 */
	public List<String> getAgentsSerialNumbers() {
		//------------start edit - 20/12-------------------
		return AgentsSerialNumbers;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the serial numbers of the agents.
	 */
	public void setAgentsSerialNumbers(List<String> agentsSerialNumbers) {
		//------------start edit - 20/12-------------------
		this.AgentsSerialNumbers=agentsSerialNumbers;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the agents names.
	 * <p>
	 * @return The agents names.
	 */
	public List<String> getAgentsNames() {
		//------------start edit - 20/12-------------------
		return AgentsNames;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the agents names.
	 */
	public void setAgentsNames(List<String> agentsNames) {
		//------------start edit - 20/12-------------------
		this.AgentsNames=agentsNames;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the name of the gadget.
	 * <p>
	 * @return the name of the gadget.
	 */
	public String getGadgetName() {
		//------------start edit - 20/12-------------------
		return GadgetName;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the name of the gadget.
	 */
	public void setGadgetName(String gadgetName) {
		//------------start edit - 20/12-------------------
		this.GadgetName=gadgetName;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public int getQTime() {
		//------------start edit - 20/12-------------------
		return QTime;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
	 */
	public void setQTime(int qTime) {
		//------------start edit - 20/12-------------------
		this.QTime=qTime;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the time when the mission was sent by an Intelligence Publisher.
	 */
	public int getTimeIssued() {
		//------------start edit - 20/12-------------------
		return TimeIssued;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the time when the mission was sent by an Intelligence Publisher.
	 */
	public void setTimeIssued(int timeIssued) {
		//------------start edit - 20/12-------------------
		this.TimeIssued=timeIssued;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Retrieves the time-tick when the report has been created.
	 */
	public int getTimeCreated() {
		//------------start edit - 20/12-------------------
		return TimeCreated;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Sets the time-tick when the report has been created.
	 */
	public void setTimeCreated(int timeCreated) {
		//------------start edit - 20/12-------------------
		this.TimeCreated=timeCreated;
		//------------end edit - 20/12 ---------------------
	}
}
