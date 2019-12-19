package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

import java.util.List;

//------------start edit - 19/12 --------------------**/
public class TickBroadcast implements Broadcast {
    private int curr_tick;

    public TickBroadcast(int curr_tick) {
        this.curr_tick = curr_tick;
    }

    public int getCurrTick(){
        return curr_tick;
    }
}
//------------end edit - 19/12----------------------**/