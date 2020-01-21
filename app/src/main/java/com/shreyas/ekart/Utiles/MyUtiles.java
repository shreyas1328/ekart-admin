package com.shreyas.ekart.Utiles;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.shreyas.ekart.BuildConfig;
import com.shreyas.ekart.R;
import com.shreyas.ekart.models.DetailsModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

public class MyUtiles {

    public static void checkReadPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                1);
    }

    public static Object getGlideLoad(DetailsModel model) {
        String imageSource = model.getImage().getImageSource();
        String imageContent = model.getImage().getImageContent();
        Log.d("uuyt", "getGlideLoad: "+imageSource);
        if (imageSource.equals("link")) {
            return imageContent;
        }else if(imageSource.equals("camera")) {
            return  stringToBitMap(imageContent);
        }else if (imageSource.equals("gallery")) {
            Log.d("uuyt", "getGlideLoad: "+new File((Uri.parse(imageContent)).getPath()));
            return new File((Uri.parse(imageContent)).getPath());
        }
        return "";
    }

    public static String getPath(Context context, Uri uri ) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
    }

    public static String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private static Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String getRandomColor() {
        String[] colors = {"#BC8F8F", "#F4A460", "#FAEBD7", "#7B68EE", "#FFA500", "#FFFF00", "#228B22", "#4169E1"};
        Random random = new Random();
        return colors[random.nextInt(7)];
    }

    public static void permissionError(Activity activity) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.permission_denied_text), Snackbar.LENGTH_LONG).setActionTextColor(Color.parseColor(activity.getString(R.string.status_color))).setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

            }
        });
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);  //Or as much as you need
        snackbar.show();
    }
}
