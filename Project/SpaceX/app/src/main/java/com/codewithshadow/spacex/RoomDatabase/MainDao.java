package com.codewithshadow.spacex.RoomDatabase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.codewithshadow.spacex.Model.CrewModel;
import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(CrewModel roomData);


    @Delete
    void reset(List<CrewModel> roomData);


    @Query("SELECT * FROM table_name")
    List<CrewModel> getAll();
}
