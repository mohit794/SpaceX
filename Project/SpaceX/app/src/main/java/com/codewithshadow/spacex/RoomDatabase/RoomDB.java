package com.codewithshadow.spacex.RoomDatabase;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.codewithshadow.spacex.Model.CrewModel;

@Database(entities =  {CrewModel.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;

    final private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context)
    {
        if (database == null)
        {
            //When database is null
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    public abstract MainDao mainDao();
}
