package demo.technology.chorus.chorusdemo.processing;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by edisonChang on 2016/9/14.
 */
public class FileUtils {

    private static final int BUFFER_SIZE = 1024;

    public static File getPrivateFile(Context context, String jarName) {
        try {
            return new File(context.getFilesDir().getAbsolutePath() + "/" + jarName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyFromAssets(Context context, String sourceName, File destFile) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(sourceName);
            out = new FileOutputStream(destFile);
            copyFile(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isLegal(File file) {
        try {
            if (file != null) {
                if (file.exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
