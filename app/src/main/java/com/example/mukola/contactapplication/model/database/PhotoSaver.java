package com.example.mukola.contactapplication.model.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PhotoSaver {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";


    @NonNull
    Context context;

    public PhotoSaver(@NonNull Context context){
        this.context = context;
    }

    public String saveToInternalStorage(Bitmap bitmapImage){

        String path = getRandomString(5);

        Log.d("Random",path);

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,path + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("PATH",directory.getAbsolutePath()+"/"+path);
        return directory.getAbsolutePath()+"/"+path+".jpg";
    }

    public Bitmap loadImageFromStorage(@NonNull String path)
    {
        Bitmap b = null;

        try {
            File f=new File(path);

            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;

    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


}
