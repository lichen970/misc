package chen.timer;

/**
 * Created by Chen on 7/10/17.
 */
public interface TimerService {
    void scheduleNow(Runnable task);
    void schedule(long timeInMs, Runnable task);
    void scheduleAtFixedRate(long interval, Runnable task);
}
