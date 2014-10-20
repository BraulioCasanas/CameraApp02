package patito.com.cameraapp02;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by braulio on 10/10/14.
 */
public class SaveMediaFiles {

    static final int MEDIA_TYPE_IMAGE = 1;
    static final int MEDIA_TYPE_VIDEO = 2;

    static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    static File getOutputMediaFile(int type){
        File directory;

        switch (type) {
            case MEDIA_TYPE_IMAGE:
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                break;
            case MEDIA_TYPE_VIDEO:
                directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                break;
            default:
                directory = null;
        }

        // to be safe, you should check that the SD-card is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStoreDir = new File(directory, "CameraApp02");
        // this location work best if you want to crate images to shared
        // between applications and persist after your app has been uninstalled

        // create a storage directory if it doe snot exist
        if ( ! mediaStoreDir.exists() ) {
            if ( ! mediaStoreDir.mkdirs() ) {
                Log.d("CameraApp02", "Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        switch (type) {
            case MEDIA_TYPE_IMAGE:
                mediaFile = new File(mediaStoreDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mediaFile = new File(mediaStoreDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
                break;
            default:
                mediaFile = null;
                break;
        }
        return mediaFile;
    }
}
