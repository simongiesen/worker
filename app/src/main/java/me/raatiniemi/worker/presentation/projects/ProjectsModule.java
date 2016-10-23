/*
 * Copyright (C) 2016 Worker Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.worker.presentation.projects;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.raatiniemi.worker.data.mapper.ProjectContentValuesMapper;
import me.raatiniemi.worker.data.mapper.ProjectCursorMapper;
import me.raatiniemi.worker.data.mapper.TimeContentValuesMapper;
import me.raatiniemi.worker.data.mapper.TimeCursorMapper;
import me.raatiniemi.worker.data.repository.ProjectResolverRepository;
import me.raatiniemi.worker.data.repository.TimeResolverRepository;
import me.raatiniemi.worker.domain.interactor.ClockActivityChange;
import me.raatiniemi.worker.domain.interactor.ClockIn;
import me.raatiniemi.worker.domain.interactor.ClockOut;
import me.raatiniemi.worker.domain.interactor.CreateProject;
import me.raatiniemi.worker.domain.interactor.GetProjectTimeSince;
import me.raatiniemi.worker.domain.interactor.GetProjects;
import me.raatiniemi.worker.domain.interactor.RemoveProject;
import me.raatiniemi.worker.domain.repository.ProjectRepository;
import me.raatiniemi.worker.domain.repository.TimeRepository;
import me.raatiniemi.worker.presentation.projects.presenter.NewProjectPresenter;
import me.raatiniemi.worker.presentation.projects.presenter.ProjectsPresenter;

@Module
public class ProjectsModule {
    @Provides
    @Singleton
    ProjectsPresenter providesProjectsPresenter(Context context) {
        ProjectRepository projectRepository = new ProjectResolverRepository(
                context.getContentResolver(),
                new ProjectCursorMapper(),
                new ProjectContentValuesMapper()
        );

        TimeRepository timeRepository = new TimeResolverRepository(
                context.getContentResolver(),
                new TimeCursorMapper(),
                new TimeContentValuesMapper()
        );

        return new ProjectsPresenter(
                context,
                EventBus.getDefault(),
                new GetProjects(projectRepository, timeRepository),
                new GetProjectTimeSince(timeRepository),
                new ClockActivityChange(
                        projectRepository,
                        timeRepository,
                        new ClockIn(timeRepository),
                        new ClockOut(timeRepository)
                ),
                new RemoveProject(projectRepository)
        );
    }

    @Provides
    @Singleton
    NewProjectPresenter providesNewProjectPresenter(Context context) {
        ProjectRepository projectRepository = new ProjectResolverRepository(
                context.getContentResolver(),
                new ProjectCursorMapper(),
                new ProjectContentValuesMapper()
        );

        return new NewProjectPresenter(
                context,
                new CreateProject(projectRepository)
        );
    }
}