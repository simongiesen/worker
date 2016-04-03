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

package me.raatiniemi.worker.presentation.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import me.raatiniemi.worker.R;
import me.raatiniemi.worker.data.WorkerContract;
import me.raatiniemi.worker.domain.model.Project;
import me.raatiniemi.worker.presentation.service.ClockOutService;
import me.raatiniemi.worker.presentation.service.PauseService;

/**
 * Notification for pausing or clocking out an active project.
 */
public class PauseNotification {
    private static final int sSmallIcon = R.drawable.ic_timer_black_24dp;

    private static final int sPauseIcon = 0;

    private static final int sClockOutIcon = 0;

    public static Notification build(Context context, Project project) {
        return new NotificationCompat.Builder(context)
                .setContentTitle(project.getName())
                .setSmallIcon(sSmallIcon)
                .addAction(buildPauseAction(context, project))
                .addAction(buildClockOutAction(context, project))
                .build();
    }

    private static NotificationCompat.Action buildPauseAction(
            Context context,
            Project project
    ) {
        Intent intent = new Intent(context, PauseService.class);
        intent.setData(getDataUri(project));

        return new NotificationCompat.Action(
                sPauseIcon,
                context.getString(R.string.notification_pause_action_pause),
                buildPendingIntentWithService(context, intent)
        );
    }

    private static NotificationCompat.Action buildClockOutAction(
            Context context,
            Project project
    ) {
        Intent intent = new Intent(context, ClockOutService.class);
        intent.setData(getDataUri(project));

        return new NotificationCompat.Action(
                sClockOutIcon,
                context.getString(R.string.notification_pause_action_clock_out),
                buildPendingIntentWithService(context, intent)
        );
    }

    private static Uri getDataUri(Project project) {
        return WorkerContract.ProjectContract.getItemUri(project.getId());
    }

    private static PendingIntent buildPendingIntentWithService(
            Context context,
            Intent intent
    ) {
        return PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
