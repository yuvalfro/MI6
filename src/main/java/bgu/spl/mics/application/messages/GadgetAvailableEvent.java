package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.ArrayList;

//------------start edit - 19/12 --------------------**/
public class GadgetAvailableEvent implements Event <ArrayList<Object>>{
      //  <Pair< Boolean, Pair<Integer, String>>>

    private String gadget;

    public GadgetAvailableEvent(String gadget){
        this.gadget=gadget;
    } /**Assumption: M will send a specific gadget g**/

    public String getNeededGadget(){
        return this.gadget;
    }
}
//------------end edit - 19/12----------------------**/