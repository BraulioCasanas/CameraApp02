package patito.com.cameraapp02;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;


public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private FrameLayout preview;
    private boolean isRecording = false;

    private String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if ( ! checkCameraHardware(this) )
            finish();

        prepareCamera();

        // add a listener to the capture button
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( isRecording ) {

                    // stop recording and release camera
                    mMediaRecorder.stop();  // stop the recording
                    releaseMediaRecorder();

                    isRecording = false;
                    Log.i(TAG, "deteniendo ... ");
                } else {

                    prepareVideoRecorder();
                    // Camera is available and unlocked, MediaRecorder is prepared,
                    // now you can start recording
                    try {
                        mMediaRecorder.start();
                        isRecording = true;
                        Log.i(TAG, "grabando ... ");
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        releaseMediaRecorder();
//                        mCamera.lock();
//                        mCamera.startPreview();
                        Log.i(TAG, "error garabando ... ");
                    } catch (Exception e) {
                        e.printStackTrace();
                        releaseMediaRecorder();
//                        mCamera.lock();
//                        mCamera.startPreview();
                        Log.i(TAG, "error garabando ... ");
                    }
                }
            }
        });
    }

    private void prepareCamera() {

        // create an instance of camera
        mCamera = getCameraInstance();

        // set Camera Parameters

        // create our Preview view and set it as the content for our activity
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    /**
     * A safe way to get an instance of the camera Object
     * @return {@link android.hardware.Camera}
     */
    private Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open(); // attempt to get a Camera Instance
        } catch (Exception e) {
            // Camera instance is not available ( in use or does not exist)
            Log.e(TAG, "Error attempt to get a Camera instance ", e);
        }
        return c; // return null if camera in unavailable
    }

    private boolean prepareVideoRecorder(){

        mMediaRecorder = new MediaRecorder();

            // step 1: Unlock and set camera to MediaRecorder
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);

            // step 2: Set sources
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // step 3: Set a CamcorderProfile (requires API Level 8 or higher)
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

            // step 4: Set output file
            mMediaRecorder.setOutputFile(SaveMediaFiles.getOutputMediaFileUri(SaveMediaFiles.MEDIA_TYPE_VIDEO).getPath());


            // step 5: Set the preview output
//            mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());


        // step 6: prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage(), e);
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage(), e);
            releaseMediaRecorder();
            return false;
        }catch (Exception e) {
            Log.d(TAG, "Exception preparing MediaRecorder: " + e.getMessage(), e);
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {

        if ( mMediaRecorder != null ) {
            mMediaRecorder.reset();     // clear recorder configuration
            mMediaRecorder.release();   // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();             // lock camera for later use
        }
    }

    private void releasePreview() {
        if ( preview != null ) {
            preview.removeView(mPreview);
        }
    }

    private void releaseCamera() {
        if ( mCamera != null ) {
            mCamera.release();  // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder(); // if you are using MediaRecorder, release it first
        releaseCamera();        // release the camera immediately on pause event
        releasePreview();
    }

    /**
     * Check is this device has a camera
     * @param context
     * @return boolean
     */
    private boolean checkCameraHardware(Context context) {

        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
