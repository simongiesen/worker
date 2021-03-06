/*
 * Copyright (C) 2017 Worker Project
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

package me.raatiniemi.worker.presentation.project;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.raatiniemi.worker.domain.interactor.GetTimesheet;
import me.raatiniemi.worker.domain.interactor.MarkRegisteredTime;
import me.raatiniemi.worker.domain.interactor.RemoveTime;
import me.raatiniemi.worker.domain.repository.TimeRepository;
import me.raatiniemi.worker.domain.repository.TimesheetRepository;
import me.raatiniemi.worker.presentation.project.presenter.TimesheetPresenter;
import me.raatiniemi.worker.presentation.util.HideRegisteredTimePreferences;

@Module
public class ProjectModule {
    @Provides
    @Singleton
    TimesheetPresenter providesTimesheetPresenter(
            @NonNull TimeRepository timeRepository,
            @NonNull TimesheetRepository timesheetRepository,
            @NonNull HideRegisteredTimePreferences hideRegisteredTimePreferences
    ) {
        return new TimesheetPresenter(
                hideRegisteredTimePreferences,
                EventBus.getDefault(),
                new GetTimesheet(timesheetRepository),
                new MarkRegisteredTime(timeRepository),
                new RemoveTime(timeRepository)
        );
    }
}
