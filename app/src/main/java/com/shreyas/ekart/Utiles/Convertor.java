package com.shreyas.ekart.Utiles;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shreyas.ekart.models.SaveImage;

import java.lang.reflect.Type;

public class Convertor {

    @TypeConverter
    public String fromImagesJson(SaveImage image) {
        return new Gson().toJson(image);
    }

    @TypeConverter
    public SaveImage toSaveImageObject(String jsonObject) {
        Type type = new TypeToken<SaveImage>(){}.getType();
        return new Gson().fromJson(jsonObject, type);
    }
}
