package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
        public static void main(String[] args) {
        // TODO Implement this
        //------------start edit - 21/12 --------------------**/

        try{
            /**DONE: read input file */
            JsonObject jsonObj = new JsonParser().parse(new FileReader( "..\\MI6\\input201 - 2.json")).getAsJsonObject();
            // jason object returns the json file as json object

            /**DONE: create passive object - inventory squad and agents */
            //Inventory:
            JsonArray inventory_import = jsonObj.get("inventory").getAsJsonArray();
            String [] inv_load = new String [inventory_import.size()];          // this will be the string array of the gadgets
            int i=0;
            for(JsonElement gadget : inventory_import){
                inv_load [i] = gadget.toString();
                i++;
            }
            Inventory.getInstance().load(inv_load);

            //Squad:
            JsonArray squad_import = jsonObj.get("squad").getAsJsonArray();
            Agent [] agents_load = new Agent [squad_import.size()];             // this will be the agent array of the agents
            i=0;
            for(JsonElement agent : squad_import){
                Agent agnt = new Agent();
                agnt.setName( agent.getAsJsonObject().get("name").toString() );
                agnt.setSerialNumber( agent.getAsJsonObject().get("serialNumber").toString() );
                agents_load[i] = agnt;
                i++;
            }
            Squad.getInstance().load(agents_load);

            //Services: inteligence and missionList setup
            List<Thread> threadList = new ArrayList<>();        // monitoring threads

            JsonObject services_import = jsonObj.get("services").getAsJsonObject();
            int intel_total = (services_import.getAsJsonObject().get("intelligence").getAsJsonArray()).size();
            for( int k=0; k<intel_total; k++) {
                JsonElement _set_of_missions = services_import.getAsJsonObject().get("intelligence").getAsJsonArray().get(k);
                int mission_total = _set_of_missions.getAsJsonObject().get("missions").getAsJsonArray().size();
                List<MissionInfo> missionInfoList_list = new LinkedList<>();
                for (i = 0; i < mission_total; i++) {            // creating each mission
                    JsonObject mission_json = _set_of_missions.getAsJsonObject().get("missions").getAsJsonArray().get(i).getAsJsonObject();
                    String missionName = mission_json.getAsJsonObject().get("missionName").toString();
                    String gadget = mission_json.getAsJsonObject().get("gadget").toString();
                    int timeIssued = Integer.parseInt(mission_json.getAsJsonObject().get("timeIssued").toString());
                    int timeExpired = Integer.parseInt(mission_json.getAsJsonObject().get("timeExpired").toString());
                    int duration = Integer.parseInt(mission_json.getAsJsonObject().get("duration").toString());

                    List<String> serialAgentsNumbers = new LinkedList<>();
                    int num_of_agents = mission_json.getAsJsonObject().get("serialAgentsNumbers").getAsJsonArray().size();
                    for (int j = 0; j < num_of_agents; j++)
                        serialAgentsNumbers.add(mission_json.getAsJsonObject().get("serialAgentsNumbers").getAsJsonArray().get(j).toString());

                    missionInfoList_list.add(new MissionInfo(missionName, serialAgentsNumbers, gadget, timeIssued, timeExpired, duration));
                }
                Intelligence intelligence = new Intelligence(missionInfoList_list); // the missionList is from the section up here ^
                Thread intelligence_thread = new Thread(intelligence);
                threadList.add(intelligence_thread);
                //TODO: run it
            }

            /**DONE: create and initialize the subscribers and publishers */
            //Services:
            int m_total = Integer.parseInt( services_import.getAsJsonObject().get("M").toString() );
            int moneypenny_total = Integer.parseInt( services_import.getAsJsonObject().get("Moneypenny").toString() );
            int total_tick = Integer.parseInt( services_import.getAsJsonObject().get("time").toString() );

            for(i=0; i<m_total; i++){
                M m = new M();
                Thread m_thread = new Thread(m);                                    //creating the M threads
                threadList.add(m_thread);
                //TODO : run it
            }
            for(i=0; i<moneypenny_total; i++){
                Moneypenny moneypenny = new Moneypenny(moneypenny_total);   //moneypenny_count will be the total mp
                Thread moneypenny_thread = new Thread(moneypenny);                  // creating the moneypenny threads
                threadList.add(moneypenny_thread);
                //TODO : run it
            }
            Thread timeservice_thread = new Thread (new TimeService(total_tick));   // creating the time service thread
            threadList.add(timeservice_thread);

            for(i=0; i<threadList.size(); i++) {
                threadList.get(i).start();
                //TODO: run the threads
            }


        }catch(FileNotFoundException e){}

        System.out.println("");

        //TODO: after termination - (terminate gracefully) generate output files
        //------------end edit - 21/12----------------------**/
    }
}
