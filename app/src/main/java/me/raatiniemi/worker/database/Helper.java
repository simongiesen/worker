package me.raatiniemi.worker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "worker";

    private static final int DATABASE_VERSION = 1;

    private static Helper mHelper;

    public static synchronized Helper getInstance(Context context)
    {
        if ( mHelper == null ) {
            mHelper = new Helper(context);
        }
        return mHelper;
    }

    public Helper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}