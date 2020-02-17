package com.example.schoolscheduler.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTest(TestEntity testEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TestEntity> tests);

    @Delete
    void deleteTest(TestEntity testEntity);

    @Query("SELECT * FROM tests WHERE id = :id")
    TestEntity getTestById(int id);

    @Query("SELECT * FROM tests ORDER BY startDate DESC")
    LiveData<List<TestEntity>> getAll();

    @Query("DELETE FROM tests")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM tests")
    int getCount();

}
