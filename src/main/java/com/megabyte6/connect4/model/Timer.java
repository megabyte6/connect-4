package com.megabyte6.connect4.model;

import java.time.Duration;
import com.megabyte6.connect4.App;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Timer {

    @Getter
    private long startTime;
    @Getter
    private long remainingTime;
    @Getter
    private long updateDelay;
    private boolean active = false;

    // Since the timer is just a method call. A call to update should not run
    // if App.delay() is still running.
    private boolean timerInstanceActive = false;

    @NonNull
    private Runnable onTimeout = () -> {
    };
    @NonNull
    private Runnable onUpdate = () -> {
    };

    public Timer(long millis) {
        this(millis, 1000);
    }

    public Timer(long millis, long updateDelay) {
        startTime = millis;
        remainingTime = startTime;
        this.updateDelay = updateDelay;
    }

    private void update() {
        if (!active) {
            timerInstanceActive = false;
            return;
        }

        remainingTime -= updateDelay;
        onUpdate.run();

        if (remainingTime <= 0) {
            timerInstanceActive = false;
            stop();
            remainingTime = 0;

            onTimeout.run();
            return;
        }

        App.delay(updateDelay, this::update);
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
            App.delay(updateDelay, this::update);
    }

    public void reset() {
        active = false;
        setRemainingTime(startTime);
    }

    public void setStartTime(long millis) throws IllegalStateException {
        if (millis < 0)
            throw new IllegalArgumentException("Timer start time cannot be negative.");
        startTime = millis;
    }

    public void setRemainingTime(long millis) throws IllegalArgumentException {
        if (millis < 0)
            throw new IllegalArgumentException("Timer time cannot be negative");
        setStartTime(millis);
        remainingTime = millis;
    }

    public String getFormattedTime() {
        final Duration duration = Duration.ofMillis(remainingTime);
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
