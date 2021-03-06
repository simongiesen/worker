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

package me.raatiniemi.worker.domain.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import me.raatiniemi.worker.factory.TimeFactory;

import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TimesheetItemGetTimeSummaryTest {
    private final String expected;
    private final Time time;

    public TimesheetItemGetTimeSummaryTest(String expected, Time time) {
        this.expected = expected;
        this.time = time;
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "1.00",
                                TimeFactory.builder()
                                        .stopInMilliseconds(3600000)
                                        .build()
                        },
                        {
                                "9.00",
                                TimeFactory.builder()
                                        .stopInMilliseconds(32400000)
                                        .build()
                        }
                }
        );
    }

    @Test
    public void getTimeSummary() {
        TimesheetItem item = new TimesheetItem(time);

        assertEquals(expected, item.getTimeSummary());
    }
}
