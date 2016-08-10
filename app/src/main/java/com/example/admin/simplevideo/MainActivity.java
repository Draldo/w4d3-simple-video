package com.example.admin.simplevideo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private VideoView mVideoview;
    private VideoView mStream;
    private static final  int VIDEO_CAPTURE = 101;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoview = (VideoView) findViewById(R.id.video_view);
        mVideoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.small));
        mVideoview.setMediaController(new MediaController(this));
        mVideoview.requestFocus();
        mVideoview.start();

        mStream = (VideoView) findViewById(R.id.streamvideo_view);
        mStream.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4");
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mStream);
        mStream.setMediaController(mediaController);
        mStream.requestFocus();
        mStream.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mStream.start();
            }
        });

    }

    public void doMagic(View view) {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            videoUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==VIDEO_CAPTURE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                playbackRecordedVideo();
            } else if (resultCode==RESULT_CANCELED){
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void playbackRecordedVideo() {
        mVideoview.setVideoURI(videoUri);
        mVideoview.setMediaController(new MediaController(this));
        mVideoview.requestFocus();
        mVideoview.start();
    }
}
