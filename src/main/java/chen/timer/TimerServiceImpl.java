package chen.timer;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;
import java.util.function.Function;

/**
 * Created by Chen on 7/10/17.
 */
public class TimerServiceImpl implements TimerService {

    protected final Logger logger = LoggerFactory.getLogger(TimerServiceImpl.class);

    private final PriorityQueue<TimerTask> taskQueue;

    // consumer/worker of the task queue
    private final Thread eventLoop;

    public TimerServiceImpl() {
        taskQueue = new PriorityQueue<>();
        eventLoop = new TimerEventLoop(taskQueue);
        eventLoop.start();
    }


    @Override
    public void scheduleNow(Runnable task) {
        schedule(0, task);
    }

    @Override
    public void schedule(long delayInMs, Runnable task) {
        Preconditions.checkArgument(task != null);
        Preconditions.checkArgument(delayInMs >= 0);
        long now = System.currentTimeMillis();
        TimerTask timerTask = new TimerTask(now + delayInMs, task);
        synchronized (taskQueue) {
            taskQueue.add(timerTask);
            // newly added task is the 1st at queue, so need worker to wake up
            // to schedule it
            if (timerTask == taskQueue.peek()) {
                taskQueue.notify();
            }
        }
    }

    @Override
    public void scheduleAtFixedRate(long interval, Runnable task) {
        throw new UnsupportedOperationException();
    }


}

class TimerEventLoop extends Thread {

    private final PriorityQueue<TimerTask> taskQueue;

    TimerEventLoop(PriorityQueue<TimerTask> taskQueue) {
        this.taskQueue = Preconditions.checkNotNull(taskQueue);
    }

    @Override
    public void run() {
        try {
            loop();
        } finally {
            synchronized (taskQueue) {
                taskQueue.clear();
            }
        }
    }

    private void loop() {
        // main event loop for the pooling thread
        while (true) {
            try {
                TimerTask task;
                boolean fireNow = false;
                synchronized (taskQueue) {
                    // retrieve task from queue, if empty then wait
                    while (taskQueue.isEmpty()) {
                        taskQueue.wait();
                    }
                    // do we need this?
                    if (taskQueue.isEmpty()) {
                        break;
                    }
                    task = taskQueue.poll();
                    Preconditions.checkState(task != null);
                    long currentTime = System.currentTimeMillis();
                    long fireTime = task.getFireTime();
                    if (fireTime <= currentTime) {
                        fireNow = true;
                    }
                    //wait some time to wake up
                    // wait need to be within synchronized block
                    if (!fireNow) {
                        taskQueue.wait(fireTime - currentTime);
                    }
                }
                // execute any outstanding task
                if (fireNow) {
                    task.getTask().run();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}