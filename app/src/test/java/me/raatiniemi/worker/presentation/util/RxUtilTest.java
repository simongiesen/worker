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

package me.raatiniemi.worker.presentation.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rx.Subscription;

import static me.raatiniemi.worker.presentation.util.RxUtil.unsubscribeIfNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RxUtilTest {
    @Test
    public void unsubscribeIfNotNull_withUnsubscribedSubscription() {
        Subscription subscription = mock(Subscription.class);
        when(subscription.isUnsubscribed()).thenReturn(true);

        unsubscribeIfNotNull(subscription);

        verify(subscription, never()).unsubscribe();
    }

    @Test
    public void unsubscribeIfNotNull_withActiveSubscription() {
        Subscription subscription = mock(Subscription.class);
        when(subscription.isUnsubscribed()).thenReturn(false);

        unsubscribeIfNotNull(subscription);

        verify(subscription).unsubscribe();
    }
}
