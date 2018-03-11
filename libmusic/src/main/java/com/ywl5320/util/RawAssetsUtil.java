package com.ywl5320.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ywl on 2018-3-9.
 */

public class RawAssetsUtil {

    /**
     * @param context
     * @param rawId
     * @param audioName
     * @return
     */
    public static String getRawFilePath(Context context, int rawId, String audioName)
    {
        File afile = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().openRawResource(rawId);
            if(is == null)
            {
                return null;
            }
            File file = context.getDir("music", Context.MODE_PRIVATE);
            afile = new File(file, audioName);
            if(!afile.exists())
            {

                fos = new FileOutputStream(afile);
                byte[] buffer = new byte[is.available()];
                int lenght = 0;
                while ((lenght = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(is != null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(afile != null && afile.exists())
        {
            return afile.getAbsolutePath();
        }
        return null;
    }

    /**
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsFilePath(Context context, String fileName)
    {
        File afile = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().getAssets().open(fileName);
            File file = context.getDir("music", Context.MODE_PRIVATE);
            afile = new File(file.getAbsolutePath() + "/" + fileName);
            if(!afile.exists())
            {
                fos = new FileOutputStream(afile);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(is != null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(afile != null &&afile.exists())
        {
            return afile.getAbsolutePath();
        }
        return null;
    }
}
