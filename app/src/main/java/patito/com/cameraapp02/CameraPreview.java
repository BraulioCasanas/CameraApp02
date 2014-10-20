package patito.com.cameraapp02;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by braulio on 10/9/14.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is create and destroy
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android version prior 3.0
        // mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage(), e);
        }
        Log.i(TAG, " pasando por el surfaceCreated ");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

       /* if ( mHolder.getSurface() == null ) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }*/

        /*try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            prepareVideoRecorder();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage(), e);
        }*/

        Log.i(TAG, " pasando por el surfaceChanged ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, " pasando por el surfaceDestroyed ");
    }
}
