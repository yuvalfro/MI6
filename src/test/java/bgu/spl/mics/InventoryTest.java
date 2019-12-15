package bgu.spl.mics;
import java.util.List;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory inv_tst;
    private String inventory[];
    private String inventory_ext[];

    @BeforeEach
    public void setUp() throws Exception{
        inv_tst = Inventory.getInstance();
        inv_tst.getGadgets().clear();
        inventory = new String[]{"G1", "G2", "G3", "G4"};
        inv_tst.load(inventory);    // inv now has 4 gadgets in 'gadgets'
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void singleton() {
        Inventory inv_tst2 = Inventory.getInstance(); // <- inv
        Assertions.assertEquals(inv_tst, inv_tst2);
    }

    @Test
    public void load() {
        Assertions.assertEquals( 4,inv_tst.getGadgets().size());
        for (int i = 0; i < 4; i++) {
            Assertions.assertTrue(inv_tst.getGadgets().contains(inventory[i]));
        }
        inventory_ext = new String[]{"G5", "G6"};
        inv_tst.load(inventory_ext);
        Assertions.assertEquals( 6,inv_tst.getGadgets().size());
        for (int i = 0; i < 2; i++) {
            Assertions.assertTrue(inv_tst.getGadgets().contains(inventory_ext[i]));
        }
    }

    @Test
    public void getItem() {
        Assertions.assertTrue(inv_tst.getItem("G1"));   // removed hopefully
        Assertions.assertFalse(inv_tst.getItem("G1"));   // no G1 hopefully
        Assertions.assertEquals(3,inv_tst.getGadgets().size());
    }
}
