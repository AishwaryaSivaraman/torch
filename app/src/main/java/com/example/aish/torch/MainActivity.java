package com.example.aish.torch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;



public class MainActivity extends ActionBarActivity {

    ImageButton bulbSwitch;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters params;
    MediaPlayer mp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bulbSwitch = (ImageButton) findViewById(R.id.bulbSwitch);

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Error");
            alertDialog.setMessage("No Flash support!").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.show();
            return;
        }else{

            getCamera();
            toggleButtonImage();
            bulbSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFlashOn){
                        turnFlashOff();
                    }else{
                        turnFlashOn();
                    }
                }
            });
        }


    }

    private void getCamera(){
        if(camera == null)
        {
            try{

                camera = Camera.open();
                params = camera.getParameters();
            }catch (RuntimeException e) {
                Log.e("Camera Error.Failed to open", e.getMessage());
            }
        }
    }

    private void turnFlashOn(){
        if(camera == null || params == null){
            return;
        }
        else{

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn=true;

            toggleButtonImage();
        }
    }

    private void turnFlashOff() {
        if(isFlashOn){
            if(camera == null || params == null)
                return;
        }

        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.startPreview();
        isFlashOn = false;

        toggleButtonImage();
    }


    public void toggleButtonImage() {
        if(isFlashOn){
            bulbSwitch.setImageResource(R.drawable.on);
        }else{
            bulbSwitch.setImageResource(R.drawable.off);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnFlashOff();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected  void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(hasFlash)
            turnFlashOn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

}
