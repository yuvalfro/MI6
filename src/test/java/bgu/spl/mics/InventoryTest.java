package bgu.spl.mics;
import java.util.List;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory inv_tst;
    private String inventory[];
    private String inventory_ext[];
    @BeforeEach
    public void setUp(){
        inv_tst = Inventory.getInstance();
        inventory = new String[]{ "G1","G2","G3","G4"} ;
        inv_tst.load(inventory);    // inv now has 4 gadgets in 'gadgets'
    }

    @Test
    public void singleton(){
        Inventory inv_tst2 = Inventory.getInstance(); // <- inv
        Assertions.assertEquals(inv_tst, inv_tst2);
    }

    @Test
    public void load(){
        Assertions.assertEquals(inv_tst.getGadgets().size(), 4);
        for (int i=0; i<4; i++){
            Assertions.assertTrue(inv_tst.getGadgets().contains(inventory[i]));
        }
        inventory_ext =new String[] {"G5", "G6"};
        inv_tst.load(inventory_ext);
        Assertions.assertEquals(inv_tst.getGadgets().size(), 6);
        for (int i=0; i<2; i++){
            Assertions.assertTrue(inv_tst.getGadgets().contains(inventory_ext[i]));
        }
    }

    @Test
    public void getItem(){

    }
}


/*

    @Test
    public void pop() {
        try{
            stack.pop();
            fail("should've thrown an exception");
        }catch(Exception e){

        }
    }

    @Test
    public void size() {
        assertEquals(stack.size(), 0);
        stack.push(2);
        assertEquals(stack.size(), 1);
    }

 */