package com.shreyas.ekart.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.shreyas.ekart.models.DetailsModel;

import java.util.List;

@Dao
public interface ContentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DetailsModel model);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DetailsModel model);

    @Query("DELETE FROM details_table WHERE content_id=:id")
    void deleteItem(int id);

    @Query("SELECT * FROM details_table")
    LiveData<List<DetailsModel>> displayItems();

    @Query("SELECT * FROM details_table where content_id = :id")
    LiveData<DetailsModel> getParticularItem(int id);




}
