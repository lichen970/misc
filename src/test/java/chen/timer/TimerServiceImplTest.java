package chen.timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chen on 7/12/17.
 */
public class TimerServiceImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void scheduleNow() throws Exception {
        TimerService timerService = new TimerServiceImpl();
        timerService.scheduleNow(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello there!");
            }
        });
    }

    @Test
    public void schedule() throws Exception {
    }

    @Test
    public void scheduleAtFixedRate() throws Exception {
    }

}