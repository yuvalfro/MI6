package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<String>();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void resolve(){
        //Check resolve(T result), isDone() and get()
        String s = "Test";
        assertEquals(false,future.isDone());
        future.resolve(s);
        assertEquals(true, future.isDone());
        assertEquals("Test", future.get());
    }

    @Test
    public void getTimed(){
        assertNull(future.get(0,TimeUnit.MILLISECONDS));
    }
}
