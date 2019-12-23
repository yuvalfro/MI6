package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
        public static void main(String[] args) throws FileNotFoundException {
        // TODO Implement this
        //------------start edit - 22/12 --------------------**/

            /**@DONE: read input file */
            Gson gson = new Gson();
            Reader reader = new FileReader(args[0]) ;  //args[0] = "..\\MI6\\input201[3].json"
            JsonClassSerializer gsonLoad = gson.fromJson(reader,JsonClassSerializer.class);


            /**@DONE: read input file */
            //Inventory:
            Inventory.getInstance().load(gsonLoad.inventory);

            //Squad:
            Squad.getInstance().load(gsonLoad.squad);

            /**@DONE: create and initialize the subscribers and publishers */
            List<Thread> threadList = new ArrayList<>();        // monitoring threads

            //Services: inteligence and missionList setup
            for(int i=0; i<gsonLoad.services.intelligence.length; i++){
                Intelligence intelligence = new Intelligence( gsonLoad.services.intelligence[i].missions ); // the missionList is from the section up here ^
                Thread intelligence_thread = new Thread(intelligence);
                threadList.add(intelligence_thread);        // Thread add
            }

            //Services: time, M, Moneypenny
            int m_total = gsonLoad.services.M;
            int moneypenny_total = gsonLoad.services.Moneypenny;
            int total_tick = gsonLoad.services.time;

            Q q = new Q();                                                          // creating the Q
            Thread q_thread = new Thread(q);
            threadList.add(q_thread);                       // Thread add

            for(int i=0; i<moneypenny_total; i++){
                Moneypenny moneypenny = new Moneypenny(moneypenny_total);           //moneypenny_count will be the total mp
                Thread moneypenny_thread = new Thread(moneypenny);                  // creating the moneypenny threads
                threadList.add(moneypenny_thread);          // Thread add
            }
            for(int i=0; i<m_total; i++){
                M m = new M();
                Thread m_thread = new Thread(m);                                    //creating the M threads
                threadList.add(m_thread);                   // Thread add
            }

            Thread timeservice_thread = new Thread (new TimeService(total_tick));   // creating the time service thread
            threadList.add(timeservice_thread);             // Thread add

            /** Thread start is starting the thread and calls the run() function of the object */
            for(int i=0; i<threadList.size(); i++) {
                threadList.get(i).start();
            }


            /**@DONE: after termination - (terminate gracefully) generate output files */
            for(int i=0; i<threadList.size(); i++) {
                try {
                    threadList.get(i).join();
                } catch (InterruptedException e) { }
             }

            List<String> gadget_left = Inventory.getInstance().getGadgets();    //TODO: DELETE THIS TEST
            List<Report> diray_list = Diary.getInstance().getReports();         //TODO: DELETE THIS TEST
            int sent_missions = Diary.getInstance().getTotal();                 //TODO: DELETE THIS TEST
            int completed_missions = Diary.getInstance().getReports().size();   //TODO: DELETE THIS TEST


             gson = new GsonBuilder().serializeNulls().create();        //TODO: DELETE THIS TEST
            String inventoryOutputJson = gson.toJson(gadget_left);         //TODO: DELETE THIS TEST
            String diaryOutputJson = gson.toJson(diray_list);           //TODO: DELETE THIS TEST
            System.out.println(inventoryOutputJson);                    //TODO: DELETE THIS TEST
            System.out.println(diaryOutputJson);                        //TODO: DELETE THIS TEST

            //Inventory out:
            Inventory.getInstance().printToFile(args[1]);   //args[1] = "..\\MI6\\inventoryOutputFile.json"

            //Diary out:
            Diary.getInstance().printToFile(args[2]);   //args[2] = "..\\MI6\\diaryOutputFile.json"
    }

    public static class JsonClassSerializer{
        public String[] inventory;
        public Services services;
        public Agent[] squad;
    }
    public static class Services{
        public int M;
        public int Moneypenny;
        public JsonIntelligence[] intelligence;
        public int time;
    }
    public static class JsonIntelligence{
        public List<MissionInfo> missions;
    }

    //------------end edit - 22/12----------------------**/
}
