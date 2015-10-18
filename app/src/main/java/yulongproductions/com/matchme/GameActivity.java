package yulongproductions.com.matchme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
    private static final int CAM_REQUEST = 2;
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
        String highscore = intent.getStringExtra(getString(R.string.high_score));
        if (highscore != null) {
            Toast.makeText(this, "Your high score is now " + highscore + "%", Toast.LENGTH_LONG).show();
        }

        takePhoto = (Button) findViewById(R.id.cameraButton);
        logout = (Button) findViewById(R.id.logoutButton);
        defineWord = (Button) findViewById(R.id.defineButton);
        upload = (Button) findViewById(R.id.uploadButton);
        image = (ImageView) findViewById(R.id.cameraImage);

        mTextView = (TextView) findViewById(R.id.adjTextView);
        this.adjective = mGrabAdjective.getAdjective();
        mTextView.setText(this.adjective);
        Log.v(TAG, "We're logging: " + this.adjective);
        mNameTextView = (TextView) findViewById(R.id.nameTextView);
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
        Bitmap thumbnail = null;
        Bundle bundle = data.getExtras();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            // The image
            Uri uri = data.getData();
            thumbnail = loadBitmapFromUri(data.getData());

        } else if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAM_REQUEST) {
                //thumbnail = (Bitmap) data.getExtras().get("data");
                thumbnail = ((Bitmap)(data.getExtras().get("data")));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, pxToDp(1300), pxToDp(1850), false);
                image.setImageBitmap(thumbnail);
            }
        }

        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent i = new Intent(this, TagActivity.class);
        i.putExtra(getString(R.string.adjective), this.adjective);
        Log.v(TAG, "We're logging: " + this.adjective);
        i.putExtra(getString(R.string.image), byteArray);
        startActivity(i);


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

            Bitmap b = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
            Bitmap rescaled = Bitmap.createScaledBitmap(b, pxToDp(1300), pxToDp(1850), false);
            //opts.inJustDecodeBounds = true;
            //opts = new BitmapFactory.Options();
            return rescaled;
        } catch (IOException e) {
            // Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }

    private int pxToDp(int px) {
        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public Bitmap RotateBitmap(Bitmap source, float angle)
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
