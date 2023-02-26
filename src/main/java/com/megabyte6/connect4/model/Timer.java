package com.megabyte6.connect4.model;

import java.time.Duration;
import com.megabyte6.connect4.App;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Timer {

    private long startTime;
    private long time;
    private long updateDelay;
    private boolean active = false;

    // Since the timer is just a method call. A call to update should not run
    // if App.delay() is still running.
    private boolean timerInstanceActive = false;

    private Runnable onTimeout = () -> {
    };
    private Runnable onUpdate = () -> {
    };

    public Timer(long millis) {
        this(millis, 1000);
    }

    public Timer(long millis, long updateDelay) {
        startTime = millis;
        time = startTime;
        this.updateDelay = updateDelay;
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
            stop();
            time = 0;

            onTimeout.run();
            return;
        }

        App.delay(updateDelay, () -> update());
        timerInstanceActive = true;
    }

    public void start() {
        reset();
        resume();
    }

    public void stop() {
        active = false;
    }

    public void resume() {
        active = true;

        if (!timerInstanceActive)
            App.delay(updateDelay, () -> update());
    }

    public void reset() {
        active = false;
        setTime(startTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long millis) {
        if (millis < 0)
            throw new IllegalArgumentException("Timer start time cannot be negative.");
        startTime = millis;
    }

    public long getRemainingTime() {
        return time;
    }

    public void setTime(long millis) {
        if (millis < 0)
            throw new IllegalArgumentException("Timer time cannot be negative");
        setStartTime(millis);
        time = millis;
    }

    public String getFormattedTime() {
        final Duration duration = Duration.ofMillis(time);
        final long seconds = duration.getSeconds();
        final long dd = seconds / 86400;
        final long HH = seconds / 3600;
        final long mm = (seconds % 3600) / 60;
        final long ss = seconds % 60;
        return (dd > 0 ? dd + ":" : "")
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

    public void setOnTimeout(@NonNull Runnable onTimeout) {
        this.onTimeout = onTimeout;
    }

    public void setOnUpdate(@NonNull Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

}
