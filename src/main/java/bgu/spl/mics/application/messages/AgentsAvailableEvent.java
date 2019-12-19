package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

//------------start edit - 19/12 --------------------**/
public class AgentsAvailableEvent implements Event <Boolean>{

    private List<String> getNeededAgents;

    public AgentsAvailableEvent(List<String> agentsList){
        this.getNeededAgents = agentsList;
    } //TODO: M will send a specific list of agents

    public List<String> getNeededAgents(){
        return this.getNeededAgents;
    }
}
//------------end edit - 19/12----------------------**/