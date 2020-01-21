package com.shreyas.ekart.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.shreyas.ekart.models.DetailsModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DetailsModel.class}, version = 1, exportSchema = false)
public abstract class ContentRoomDataBase extends RoomDatabase {

    public abstract ContentDAO contentDAO();

    private static volatile ContentRoomDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWrite = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContentRoomDataBase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContentRoomDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ContentRoomDataBase.class, "details_table").build();
                }
            }
        }
        return INSTANCE;
    }
}
