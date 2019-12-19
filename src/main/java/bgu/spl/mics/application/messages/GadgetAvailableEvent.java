package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

//------------start edit - 19/12 --------------------**/
public class GadgetAvailableEvent implements Event <Boolean>{

    private String gadget;

    public GadgetAvailableEvent(String gadget){
        this.gadget=gadget;
    } //TODO: M will send a specific gadget g

    public String getNeededGadget(){
        return this.gadget;
    }
}
//------------end edit - 19/12----------------------**/