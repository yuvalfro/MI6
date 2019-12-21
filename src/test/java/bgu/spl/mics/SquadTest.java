package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
/*
    private Squad sqd_tst;
    private Agent agents[];
    @BeforeEach
    public void setUp() {
        sqd_tst = Squad.getInstance();
        sqd_tst.getAgentsMap().clear();
        Agent a = new Agent("007","Yuval F");
        Agent b = new Agent("0012","Omer L");
        agents = new Agent[] {a,b};
        sqd_tst.load(agents);    // inv now has 4 gadgets in 'gadgets'
    }

    @Test
    public void singleton() {
        Squad sqd_tst2 = Squad.getInstance(); // <- sqd
        Assertions.assertEquals(sqd_tst, sqd_tst2);
    }

    @Test
    public void load() {
        Assertions.assertEquals(2,sqd_tst.getAgentsMap().size());
       Assertions.assertTrue(sqd_tst.getAgentsMap().containsKey("007"));
        Assertions.assertTrue(sqd_tst.getAgentsMap().containsKey("0012"));
        Agent c = new Agent("001","Splflix");
        Agent agents_ext [] = new Agent[] {c};
        sqd_tst.load(agents_ext);
        Assertions.assertEquals(3,sqd_tst.getAgentsMap().size() );
        Assertions.assertTrue(sqd_tst.getAgentsMap().containsKey("001"));
    }

    @Test
    public void releaseAgents(){
        sqd_tst.getAgentsMap().get("007").acquire();    // now 007 available=false
        Assertions.assertFalse(sqd_tst.getAgentsMap().get("007").isAvailable());
        List<String> list=new LinkedList<String>();
        list.add("007");
        sqd_tst.releaseAgents(list);                      // now 007 available=true
        Assertions.assertTrue(sqd_tst.getAgentsMap().get("007").isAvailable());
    }

    @Test
    public void getAgents(){
        List<String> list=new LinkedList<String>();
        list.add("007");
        list.add("0012");
        assertTrue(sqd_tst.getAgents(list));
        list.add("0012313");
        assertFalse(sqd_tst.getAgents(list));       // not exists
        list.remove("0012313");
        assertTrue(sqd_tst.getAgents(list));
    }

    @Test
    public void getAgentsNames(){
        List<String> serials = new LinkedList<>();
        serials.add("007");
        serials.add("0012");
        List<String> names = sqd_tst.getAgentsNames(serials);
        assertTrue(names.contains("Yuval F"));
        assertTrue(names.contains("Omer L"));
    }

 */
}
