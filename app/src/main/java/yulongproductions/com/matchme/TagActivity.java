package yulongproductions.com.matchme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class TagActivity extends ActionBarActivity {

    // API Key for Clarifai
    private final String APP_ID = "hjerQocu0N0dt324XLZw4agpUf-PuMibm5ILwW77";
    private final String APP_SECRET = "jhEaJ74h1dEfVy8MUuM3uZ5vsm_VeSsfaO5TCHA1";

    // Constant client
    private final ClarifaiClient clar = new ClarifaiClient(APP_ID, APP_SECRET);

    // Fields
    private TextView adjective;
    private TextView tagList;
    private ImageView preview;
    private Button continueButton;
    private ArrayList<Tag> list;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        // Hides the action bar
        getSupportActionBar().hide();

        adjective = (TextView) findViewById(R.id.adjTextView);
        tagList = (TextView) findViewById(R.id.tagTextView);
        preview = (ImageView) findViewById(R.id.imagePreview);
        continueButton = (Button) findViewById(R.id.continueButton);
        this.list = new ArrayList<Tag>();

        Intent i = getIntent();
        this.name = i.getStringExtra(getString(R.string.name));
        String target = i.getStringExtra(getString(R.string.adjective));
        byte[] byteArray = i.getByteArrayExtra(getString(R.string.image));

        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        adjective.setText("Adjective: " + target);
        this.preview.setImageBitmap(image);
        preview.setMaxHeight(300);
        preview.setMaxWidth(600);

        new AsyncTask<Bitmap, Void, RecognitionResult>() {
            @Override
            protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                return recognizeBitmap(bitmaps[0]);
            }

            @Override
            protected void onPostExecute(RecognitionResult result) {
                updateUIForResult(result);
            }
        }.execute(image);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TagActivity.this, GameActivity.class);
                i.putExtra(getString(R.string.message), getRating());
                i.putExtra(getString(R.string.name), name);
                startActivity(i);
            }
        });


    }

    // From Demo
    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return clar.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {

            return null;
        }
    }

    /**
     * Updates the UI by displaying tags for the given result.
     */
    private void updateUIForResult(RecognitionResult result) {
        if (result != null) {
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                // Display the list of tags in the UI.
                StringBuilder b = new StringBuilder();
                for (Tag tag : result.getTags()) {
                    list.add(tag);
                    b.append(b.length() > 0 ? ", " : "").append(tag.getName());
                }
                tagList.setText("Tags:\n" + b);
            } else {

                tagList.setText("Sorry, there was an error recognizing your image.");
            }
        } else {
            tagList.setText("Sorry, there was an error recognizing your image.");
        }

    }

    // Returns the rating of user's photo match to adjective
    private String getRating() {
        if (this.hasWord()) {
            return "Your picture is good!";
        } else {
            return "Try again next time";
        }
    }

    // Returns true if the list of tags matches the adjective and false otherwise
    private boolean hasWord() {
        for (int i = 0; i < list.size(); i++) {
            if (adjective.equals(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    // Disables the use of the back button
    @Override
    public void onBackPressed() {
    }
}
