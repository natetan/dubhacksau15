package yulongproductions.com.matchme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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


public class GameActivity extends ActionBarActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    private static final int CAM_REQUEST = 1313;

    private GrabAdjective mGrabAdjective = new GrabAdjective();
    private Dictionary mDictionary = new Dictionary();
    private String name;
    private String fav;
    private String adjective;
    private TextView mTextView;
    private TextView mNameTextView;
    private Button takePhoto;
    private Button logout;
    private Button defineWord;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        this.name = intent.getStringExtra(getString(R.string.name));
        this.fav = intent.getStringExtra(getString(R.string.fav));

        takePhoto = (Button)findViewById(R.id.cameraButton);
        logout = (Button)findViewById(R.id.logoutButton);
        defineWord = (Button)findViewById(R.id.defineButton);
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
                define(adjective);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQUEST) {
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
}
