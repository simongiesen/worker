package me.raatiniemi.worker.model.domain.time;

import java.util.Date;

import me.raatiniemi.worker.model.domain.DomainObject;
import me.raatiniemi.worker.exception.domain.ClockOutBeforeClockInException;

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
     * <p>
     * UNIX timestamp, in milliseconds, representing the date and time at
     * which the interval was considered clocked in.
     */
    private Long mStart;

    /**
     * Timestamp for when the time interval ends, or zero if active.
     * <p>
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
    public Time(Long id, Long projectId, Long start, Long stop) throws ClockOutBeforeClockInException {
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
    public Time(Long projectId) throws ClockOutBeforeClockInException {
        this(null, projectId, (new Date()).getTime(), 0L);
    }

    /**
     * Getter method for the project id.
     *
     * @return Id for the project connected to the time interval.
     */
    public Long getProjectId() {
        return mProjectId;
    }

    /**
     * Internal setter method for the project id.
     *
     * @param projectId Id for the project connected to the time interval.
     */
    private void setProjectId(Long projectId) {
        mProjectId = projectId;
    }

    /**
     * Getter method for timestamp when the time interval start.
     *
     * @return Timestamp for time interval start, in milliseconds.
     */
    public Long getStart() {
        return mStart;
    }

    /**
     * Setter method for timestamp when the time interval start.
     *
     * @param start Timestamp for time interval start, in milliseconds.
     */
    public void setStart(Long start) {
        mStart = start;
    }

    /**
     * Getter method for timestamp when the time interval ends.
     *
     * @return Timestamp for time interval end, in milliseconds, or zero if active.
     */
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
    public void setStop(Long stop) throws ClockOutBeforeClockInException {
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
     */
    public void clockInAt(Date date) {
        setStart(date.getTime());
    }

    /**
     * Set the clock out timestamp at given date.
     *
     * @param date Date at which to clock out.
     * @throws ClockOutBeforeClockInException If clock out occur before clock in.
     */
    public void clockOutAt(Date date) throws ClockOutBeforeClockInException {
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
     * <p>
     * The time is only considered registered if the interval is not active,
     * i.e. both the start and stop values must be valid (not zero).
     *
     * @return Registered time in milliseconds, or zero if interval is active.
     */
    public Long getTime() {
        Long time = 0L;

        if (!isActive()) {
            time = getStop() - getStart();
        }

        return time;
    }

    /**
     * Get the time interval.
     * <p>
     * If the interval is active, the current time will be used to calculate
     * the time between start and now.
     *
     * @return Interval in milliseconds.
     */
    public Long getInterval() {
        Long stop = getStop();

        if (isActive()) {
            stop = (new Date()).getTime();
        }

        return stop - getStart();
    }
}
