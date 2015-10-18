package yulongproductions.com.matchme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;


public class GameActivity extends ActionBarActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    private static final int CAM_REQUEST = 1313;
    private static final int PICK_IMAGE_REQUEST = 1;

    private GrabAdjective mGrabAdjective = new GrabAdjective();
    private Dictionary mDictionary = new Dictionary();
    private String name;
    private String password;
    private String adjective;
    private TextView mTextView;
    private TextView mNameTextView;
    private Button takePhoto;
    private Button logout;
    private Button defineWord;
    private Button upload;
    private ImageView image;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        this.name = intent.getStringExtra(getString(R.string.name));
        this.password = intent.getStringExtra(getString(R.string.password));

        takePhoto = (Button)findViewById(R.id.cameraButton);
        logout = (Button)findViewById(R.id.logoutButton);
        defineWord = (Button)findViewById(R.id.defineButton);
        upload = (Button)findViewById(R.id.uploadButton);
        image = (ImageView)findViewById(R.id.cameraImage);

        mTextView = (TextView)findViewById(R.id.adjTextView);
        this.adjective = mGrabAdjective.getAdjective();
        mTextView.setText(this.adjective);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mNameTextView.setText("Current User: " + this.name);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAM_REQUEST);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        defineWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define(adjective);
                Toast.makeText(GameActivity.this, adjective + ": ", Toast.LENGTH_LONG).show();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            // The image
            Uri uri = data.getData();

            bitmap = loadBitmapFromUri(data.getData());
        } else if (requestCode == CAM_REQUEST) {
            Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
        }
    }

    private String getDefinition(String word) throws IOException {
        return mDictionary.define(word);
    }

    private void define(String word) {
        String URL = "http://dictionary.reference.com/browse/" + word;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Toast.makeText(GameActivity.this, getDefinition(adjective), Toast.LENGTH_LONG).show();
                            mTextView.setText(mTextView.getText() + " " + getDefinition(adjective));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    // From Demo
    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            // The image may be large. Load an image that is sized for display. This follows best
            // practices from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inJustDecodeBounds = true;
            //opts = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
        } catch (IOException e) {
            // Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }
}
