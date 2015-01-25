package me.raatiniemi.worker.mapper;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;

import me.raatiniemi.worker.domain.DomainObject;
import me.raatiniemi.worker.domain.Project;

public class ProjectMapper extends AbstractMapper
{
    private static final String TABLE_NAME = "project";

    private interface Columns
    {
        String NAME = "name";

        String DESCRIPTION = "description";
    }

    public static final String CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ( " +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Columns.NAME + " TEXT NOT NULL, " +
            Columns.DESCRIPTION + " TEXT NULL, " +
            "UNIQUE (" + Columns.NAME + ") ON CONFLICT ROLLBACK" +
        ");";

    private TimeMapper mTimeMapper;

    public ProjectMapper(Context context, TimeMapper timeMapper)
    {
        super(context);
        mTimeMapper = timeMapper;
    }

    protected DomainObject load(Cursor row)
    {
        long id = row.getLong(row.getColumnIndex(BaseColumns._ID));
        String name = row.getString(row.getColumnIndex(Columns.NAME));
        String description = row.getString(row.getColumnIndex(Columns.DESCRIPTION));

        Project project = new Project(id, name);
        project.setDescription(description);

        return project;
    }

    public ArrayList<Project> getProjects()
    {
        ArrayList<Project> result = new ArrayList<>();

        String[] columns = new String[]{
            BaseColumns._ID,
            Columns.NAME,
            Columns.DESCRIPTION
        };

        Cursor rows = mDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
        if (rows.moveToFirst()) {
            do {
                Project project = (Project) load(rows);
                result.add(project);
            } while (rows.moveToNext());
        }

        return result;
    }
}
