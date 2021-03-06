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

package me.raatiniemi.worker.data.mapper;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import me.raatiniemi.worker.data.provider.ProviderContract.ProjectColumns;
import me.raatiniemi.worker.domain.exception.InvalidProjectNameException;
import me.raatiniemi.worker.domain.model.Project;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ProjectCursorMapperTest {
    private final Project expected;
    private final Cursor cursor;

    public ProjectCursorMapperTest(Project expected, Cursor cursor) {
        this.expected = expected;
        this.cursor = cursor;
    }

    @Parameters
    public static Collection<Object[]> getParameters()
            throws InvalidProjectNameException {
        return Arrays.asList(
                new Object[][]{
                        {
                                createProject(1, "Name"),
                                createCursor(1, "Name")
                        },
                        {
                                createProject(1, "Name"),
                                createCursor(1, "Name")
                        }
                }
        );
    }

    private static Project createProject(long id, String name)
            throws InvalidProjectNameException {
        return Project.builder(name)
                .id(id)
                .build();
    }

    private static Cursor createCursor(long id, String name) {
        Cursor cursor = mock(Cursor.class);

        when(cursor.getColumnIndexOrThrow(BaseColumns._ID)).thenReturn(0);
        when(cursor.getColumnIndexOrThrow(ProjectColumns.NAME)).thenReturn(1);

        when(cursor.getLong(0)).thenReturn(id);
        when(cursor.getString(1)).thenReturn(name);

        return cursor;
    }

    @Test
    public void transform() throws InvalidProjectNameException {
        ProjectCursorMapper mapper = new ProjectCursorMapper();
        Project actual = mapper.transform(cursor);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
