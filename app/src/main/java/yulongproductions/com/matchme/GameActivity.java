package yulongproductions.com.matchme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class GameActivity extends ActionBarActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    private static final int CAM_REQUEST = 1313;

    private GrabAdjective mGrabAdjective = new GrabAdjective();
    private String name;
    private String fav;
    private TextView mTextView;
    private TextView mNameTextView;
    private Button takePhoto;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        this.name = intent.getStringExtra(getString(R.string.name));
        this.fav = intent.getStringExtra(getString(R.string.fav));

        takePhoto = (Button)findViewById(R.id.cameraButton);
        image = (ImageView)findViewById(R.id.cameraImage);

        mTextView = (TextView)findViewById(R.id.adjTextView);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mTextView.setText(mGrabAdjective.getAdjective());
        mNameTextView.setText("Current User: " + this.name);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAM_REQUEST);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.alertUserAboutError();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_message");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST) {
            Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
        }
    }
}
