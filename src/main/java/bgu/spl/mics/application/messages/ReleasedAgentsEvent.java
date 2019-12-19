package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

//------------start edit - 19/12 --------------------**/
public class ReleasedAgentsEvent implements Event <Boolean>{

    private List<String> releasedAgents;

    public ReleasedAgentsEvent(List<String> agentsList){
        this.releasedAgents = agentsList;
    } //TODO: M will send a specific list of agents

    public List<String> getReleasedAgents(){
        return this.releasedAgents;
    }
}
//------------end edit - 19/12----------------------**/
