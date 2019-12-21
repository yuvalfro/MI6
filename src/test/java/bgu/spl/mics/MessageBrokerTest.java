package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    MessageBroker msgBrkr;
    Subscriber subscriber_monpen;
    Subscriber subscriber_m;

    @BeforeEach
    public void setUp(){
        msgBrkr = MessageBrokerImpl.getInstance();
      //  subscriber_monpen = new Moneypenny();
        subscriber_m = new M();
    }

    @Test
    public void test1() {
        msgBrkr.register(subscriber_monpen);

        ExampleEvent event_exmp = new ExampleEvent("EventName");
        msgBrkr.subscribeEvent(event_exmp.getClass(),subscriber_monpen );    //subscribe event for subscriber

        Future <String> future_obj= msgBrkr.sendEvent(event_exmp);         //send event example
        try{
            assertEquals(event_exmp,msgBrkr.awaitMessage(subscriber_monpen)); //message is the event example!
        }catch(Exception e){
            fail("did not recieved anything from subscriber_monpen");         // not the message we want
        }
        msgBrkr.complete(event_exmp,"Done");                          // completed - "Done"
        assertEquals("Done", future_obj.get());                     // checks if future_obj is Done either
    }

    @Test
    public void test2(){
        msgBrkr.register(subscriber_m);

        ExampleBroadcast brodcast_exmp = new ExampleBroadcast("BroadcastName");
        msgBrkr.subscribeBroadcast(brodcast_exmp.getClass(),subscriber_m);

        msgBrkr.sendBroadcast(brodcast_exmp);                               //send broadcast example
        try{
            assertEquals(brodcast_exmp,msgBrkr.awaitMessage(subscriber_m)); //message is the event example!
        }catch(Exception e){
            fail("did not recieved anything from subscriber_m");         // not the message we want
        }
        msgBrkr.unregister(subscriber_m);
        msgBrkr.sendBroadcast(brodcast_exmp);                               //send broadcast example
        try{
            assertNotEquals(brodcast_exmp,msgBrkr.awaitMessage(subscriber_m)); //message ISNT the event example!
        }catch(Exception e){
            fail("DID recieved anything from subscriber_m");         // the message we want
        }

    }
}
