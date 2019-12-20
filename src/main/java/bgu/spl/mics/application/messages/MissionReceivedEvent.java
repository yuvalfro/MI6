package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

//------------start edit - 19/12 --------------------**/
public class MissionReceivedEvent implements Event<String> {
    private MissionInfo m_info;
    private int sentTime;

    public MissionReceivedEvent(MissionInfo m_info , int sentTime){
        this.sentTime=sentTime;
        this.m_info=m_info;
    }

    public MissionInfo getMissionInfo(){
        return m_info;
    }

    public int getTimeIssued(){ return sentTime;}
}
//------------end edit - 19/12----------------------**/