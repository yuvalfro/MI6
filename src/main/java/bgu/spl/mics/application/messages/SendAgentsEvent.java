package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.List;

//------------start edit - 19/12 --------------------**/
public class SendAgentsEvent implements Event<Boolean> {
    private List<String> agentsToSend;
    private int timeForMission;

    public SendAgentsEvent(List<String> agentsToSend, int timeForMission) {
        this.agentsToSend=agentsToSend;
        this.timeForMission=timeForMission;
    }

    public List<String> sendAgentsInfo(){
        return agentsToSend;
    }

    public int getTimeForMission(){
        return timeForMission;
    }
}
//------------end edit - 19/12----------------------**/