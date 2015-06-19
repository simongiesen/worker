package me.raatiniemi.worker.model.project;

import java.util.Date;

import me.raatiniemi.worker.exception.DomainException;
import me.raatiniemi.worker.mapper.MapperRegistry;
import me.raatiniemi.worker.mapper.ProjectMapper;
import me.raatiniemi.worker.mapper.TimeMapper;
import me.raatiniemi.worker.model.time.Time;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class ProjectProvider {
    /**
     * Retrieve the projects.
     *
     * @return Observable emitting the projects.
     */
    public Observable<ProjectCollection> getProjects() {
        return Observable.defer(new Func0<Observable<ProjectCollection>>() {
            @Override
            public Observable<ProjectCollection> call() {
                ProjectMapper mapper = MapperRegistry.getProjectMapper();
                return Observable.just(mapper.getProjects());
            }
        });
    }

    /**
     * Retrieve project based on the project id.
     *
     * @param id Id for the project.
     * @return Observable emitting the project.
     */
    public Observable<Project> getProject(final Long id) {
        return Observable.defer(new Func0<Observable<Project>>() {
            @Override
            public Observable<Project> call() {
                ProjectMapper mapper = MapperRegistry.getProjectMapper();
                return Observable.just(mapper.find(id));
            }
        });
    }

    /**
     * Clock in or clock out the project at given date.
     *
     * @param project Project to clock in/out.
     * @param date Date to clock in/out at.
     * @return Observable emitting the clocked in/out project.
     */
    public Observable<Project> clockActivityChange(final Project project, final Date date) {
        try {
            // Depending on whether the project is active we have
            // to clock in or clock out at the given date.
            Observable<Long> observable;
            if (!project.isActive()) {
                observable = clockIn(project.clockInAt(date));
            } else {
                observable = clockOut(project.clockOutAt(date));
            }

            // With the emitted project id, retrieve the project with time.
            return observable.flatMap(new Func1<Long, Observable<Project>>() {
                @Override
                public Observable<Project> call(Long projectId) {
                    // TODO: Use the getProject, with additional argument for retrieving time.
                    ProjectMapper mapper = MapperRegistry.getProjectMapper();
                    return Observable.just(mapper.reload(projectId));
                }
            });
        } catch (DomainException e) {
            return Observable.error(e);
        }
    }

    /**
     * Clock in the project.
     *
     * @param time Clock in time for project.
     * @return Observable emitting the id for the project.
     */
    private Observable<Long> clockIn(final Time time) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                TimeMapper mapper = MapperRegistry.getTimeMapper();
                mapper.insert(time);

                return Observable.just(time.getProjectId());
            }
        });
    }

    /**
     * Clock out the project.
     *
     * @param time Clock out time for project.
     * @return Observable emitting the id for the project.
     */
    private Observable<Long> clockOut(final Time time) {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                TimeMapper mapper = MapperRegistry.getTimeMapper();
                mapper.update(time);

                return Observable.just(time.getProjectId());
            }
        });
    }
}