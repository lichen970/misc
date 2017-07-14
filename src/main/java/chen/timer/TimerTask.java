package chen.timer;

import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;

/**
 * Created by Chen on 7/10/17.
 */
@Immutable
public class TimerTask implements Comparable<TimerTask> {

    private final long fireTime;

    private final Runnable task;

    public TimerTask(long fireTime, Runnable task) {
        Preconditions.checkArgument(fireTime > 0);
        this.fireTime = fireTime;
        this.task = Preconditions.checkNotNull(task);
    }

    public long getFireTime() {
        return this.fireTime;
    }

    public Runnable getTask() {
        return this.task;
    }

    @Override
    public int compareTo(TimerTask o) {
        return Long.compare(fireTime, o.getFireTime());
    }
}
