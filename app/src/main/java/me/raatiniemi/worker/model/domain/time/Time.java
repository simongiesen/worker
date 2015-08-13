package me.raatiniemi.worker.model.domain.time;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

import me.raatiniemi.worker.exception.domain.ClockOutBeforeClockInException;
import me.raatiniemi.worker.model.domain.DomainObject;

/**
 * Represent a time interval registered to a project.
 */
public class Time extends DomainObject {
    /**
     * Id for the project connected to the time interval.
     */
    private Long mProjectId;

    /**
     * Timestamp for when the time interval starts.
     * <p/>
     * UNIX timestamp, in milliseconds, representing the date and time at
     * which the interval was considered clocked in.
     */
    private Long mStart;

    /**
     * Timestamp for when the time interval ends, or zero if active.
     * <p/>
     * UNIX timestamp, in milliseconds, representing the date and time at
     * which the interval was considered clocked out.
     */
    private Long mStop;

    /**
     * Default constructor.
     */
    public Time() {
        super();
    }

    /**
     * Constructor.
     *
     * @param id Id for the time interval.
     * @param projectId Id for the project connected to the time interval.
     * @param start Timestamp for when the interval starts.
     * @param stop Timestamp for when the interval ends, or zero if active.
     * @throws ClockOutBeforeClockInException If stop time is before start time, and stop is not zero.
     */
    public Time(@Nullable Long id, @NonNull Long projectId, @NonNull Long start, @NonNull Long stop) throws ClockOutBeforeClockInException {
        super(id);

        setProjectId(projectId);
        setStart(start);

        // Only set the stop time if the time is not active,
        // otherwise an exception will be thrown.
        if (stop > 0) {
            setStop(stop);
        }
    }

    /**
     * Constructor, short hand for clock in activity.
     *
     * @param projectId Id for the project connected to the time interval.
     * @throws ClockOutBeforeClockInException If stop time is before start time.
     */
    public Time(@NonNull Long projectId) throws ClockOutBeforeClockInException {
        this(null, projectId, (new Date()).getTime(), 0L);
    }

    /**
     * Getter method for the project id.
     *
     * @return Id for the project connected to the time interval.
     */
    @Nullable
    public Long getProjectId() {
        return mProjectId;
    }

    /**
     * Internal setter method for the project id.
     *
     * @param projectId Id for the project connected to the time interval.
     */
    public void setProjectId(@NonNull Long projectId) {
        mProjectId = projectId;
    }

    /**
     * Getter method for timestamp when the time interval start.
     *
     * @return Timestamp for time interval start, in milliseconds.
     */
    @NonNull
    public Long getStart() {
        if (null == mStart) {
            mStart = 0L;
        }

        return mStart;
    }

    /**
     * Setter method for timestamp when the time interval start.
     *
     * @param start Timestamp for time interval start, in milliseconds.
     * @throws ClockOutBeforeClockInException If value for start is more than value for stop.
     */
    public void setStart(@NonNull Long start) throws ClockOutBeforeClockInException {
        // Check that the start value is less than the stop value, but only
        // if the stop value is not zero. Should not be able to change clock
        // in to occur after clock out.
        if (!isActive() && start > getStop()) {
            throw new ClockOutBeforeClockInException(
                "Clock in occur after clock out"
            );
        }

        mStart = start;
    }

    /**
     * Getter method for timestamp when the time interval ends.
     *
     * @return Timestamp for time interval end, in milliseconds, or zero if active.
     */
    @NonNull
    public Long getStop() {
        if (null == mStop) {
            mStop = 0L;
        }

        return mStop;
    }

    /**
     * Setter method for timestamp when the time interval ends.
     *
     * @param stop Timestamp for time interval end, in milliseconds.
     * @throws ClockOutBeforeClockInException If value for stop is less than value for start.
     */
    public void setStop(@NonNull Long stop) throws ClockOutBeforeClockInException {
        // Check that the stop value is lager than the start value,
        // should not be able to clock out before clocked in.
        if (stop < getStart()) {
            throw new ClockOutBeforeClockInException(
                "Clock out occur before clock in"
            );
        }

        mStop = stop;
    }

    /**
     * Set the clock in timestamp at a given date.
     *
     * @param date Date at which to clock in.
     * @throws ClockOutBeforeClockInException If clock in occur after clock out.
     */
    public void clockInAt(@NonNull Date date) throws ClockOutBeforeClockInException {
        setStart(date.getTime());
    }

    /**
     * Set the clock out timestamp at given date.
     *
     * @param date Date at which to clock out.
     * @throws ClockOutBeforeClockInException If clock out occur before clock in.
     */
    public void clockOutAt(@NonNull Date date) throws ClockOutBeforeClockInException {
        setStop(date.getTime());
    }

    /**
     * Check if the time interval is active.
     *
     * @return True if time interval is active, otherwise false.
     */
    public boolean isActive() {
        return 0 == getStop();
    }

    /**
     * Get the registered time.
     * <p/>
     * The time is only considered registered if the interval is not active,
     * i.e. both the start and stop values must be valid (not zero).
     *
     * @return Registered time in milliseconds, or zero if interval is active.
     */
    @NonNull
    public Long getTime() {
        Long time = 0L;

        if (!isActive()) {
            time = getStop() - getStart();
        }

        return time;
    }

    /**
     * Get the time interval.
     * <p/>
     * If the interval is active, the current time will be used to calculate
     * the time between start and now.
     *
     * @return Interval in milliseconds.
     */
    @NonNull
    public Long getInterval() {
        Long stop = getStop();

        if (isActive()) {
            stop = (new Date()).getTime();
        }

        return stop - getStart();
    }
}
