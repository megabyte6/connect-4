package com.megabyte6.connect4.model;

import java.time.Duration;
import com.megabyte6.connect4.App;

public class Timer {

    private static final Timer instance = new Timer();

    private long startTime = 0;
    private long time = 0;
    private long updateDelay = 1000;
    private boolean active = false;

    // Since the timer is just a method call. A call to update should not run
    // if App.delay() is still running.
    private boolean timerInstanceActive = false;

    private Runnable onTimeout = () -> {
    };
    private Runnable onUpdate = () -> {
    };

    private Timer() {}

    public static Timer getInstance() {
        return instance;
    }

    private void update() {
        if (!active) {
            timerInstanceActive = false;
            return;
        }

        time -= updateDelay;
        onUpdate.run();

        if (time <= 0) {
            timerInstanceActive = false;
            pause();
            time = 0;

            onTimeout.run();
            return;
        }

        App.delay(updateDelay, () -> update());
        timerInstanceActive = true;
    }

    public void start() {
        reset();
        unpause();
    }

    public void unpause() {
        active = true;

        if (!timerInstanceActive)
            App.delay(updateDelay, () -> update());
    }

    public void pause() {
        active = false;
    }

    public void reset() {
        active = false;
        setTime(startTime);
    }

    public void clear() {
        active = false;
        setTime(0);
        setOnTimeout(() -> {
        });
        setOnUpdate(() -> {
        });
    }

    public long getStartTime() {
        return startTime;
    }

    public void setTime(long millis) {
        if (millis < 0)
            throw new IllegalArgumentException();
        startTime = millis;
    }

    public long getRemainingTime() {
        return time;
    }

    public String getFormattedTime() {
        final Duration duration = Duration.ofMillis(time);
        final long seconds = duration.getSeconds();
        final long dd = seconds / 86400;
        final long HH = seconds / 3600;
        final long mm = (seconds % 3600) / 60;
        final long ss = seconds % 60;
        return "Time: "
                + (dd > 0 ? dd + ":" : "")
                + (HH > 0 ? HH + ":" : "")
                + (mm > 0 ? mm + ":" : "")
                + (ss > 0 ? ss + "" : "0");
    }

    public long getUpdateDelay() {
        return updateDelay;
    }

    public void setUpdateDelay(long updateDelay) {
        if (updateDelay < 0)
            throw new IllegalArgumentException();
        this.updateDelay = updateDelay;
    }

    public void setOnTimeout(Runnable onTimeout) {
        if (onTimeout == null)
            return;
        this.onTimeout = onTimeout;
    }

    public void setOnUpdate(Runnable onUpdate) {
        if (onUpdate == null)
            return;
        this.onUpdate = onUpdate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + (int) (time ^ (time >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Timer other = (Timer) obj;
        if (active != other.active)
            return false;
        if (time != other.time)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Timer [active=" + active + ", time=" + time + "]";
    }

}
