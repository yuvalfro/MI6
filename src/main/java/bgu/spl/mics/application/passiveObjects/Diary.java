package bgu.spl.mics.application.passiveObjects;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	//------------start edit -20/12 -------------------
	private List<Report> reports;
	private int total;

	/** for signleton - thread safe*/
	private static class SingletonHolder {
		private static Diary diary_instance = new Diary();
	} // get instance like TIRGUL 8

	/** Constructor */
	private Diary(){
		reports = new LinkedList<Report>();
		total=0;
	}
	//------------end edit - 20/12 ---------------------

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		//------------start edit - 20/12-------------------
		return Diary.SingletonHolder.diary_instance;
		//------------end edit - 20/12 ---------------------
	}

	public List<Report> getReports() {
		//------------start edit - 20/12-------------------
		return reports;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		//------------start edit - 20/12-------------------
		this.reports.add(reportToAdd);
		/** ASSUMPTION: in M we will synchronized Diary, that every access to the diary will be thread safe**/
		//------------end edit - 20/12 ---------------------
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		//------------start edit - 22/12 -------------------
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fileWriter = new FileWriter(filename);
			gson.toJson(this,Diary.class,fileWriter);
			fileWriter.flush(); //flush data to file   <---
			fileWriter.close(); //close write          <---
		} catch (IOException e) {}

		//------------end edit - 22/12 ---------------------
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		//------------start edit - 20/12-------------------
		return total;
		//------------end edit - 20/12 ---------------------
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		//------------start edit - 20/12-------------------
		total++;
		//------------end edit - 20/12 ---------------------
	}
}
