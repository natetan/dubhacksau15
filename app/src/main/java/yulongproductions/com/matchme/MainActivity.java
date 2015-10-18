package yulongproductions.com.matchme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mStartButton;
    private Button infoButton;
    private TextView infoView;
    private EditText mNameText;
    private EditText mFavText;
    private Context mContext;
    private GlobalFunctions mGlobal;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button)findViewById(R.id.startButton);
        infoButton = (Button)findViewById(R.id.infoButton);
        infoView = (TextView)findViewById(R.id.infoView);
        mNameText = (EditText)findViewById(R.id.nameEditText);
        mFavText = (EditText)findViewById(R.id.favEditText);
        mGlobal = new GlobalFunctions(this);
        mGlobal.setupUI(findViewById(R.id.mainLayout));

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameText.getText().toString();
                String fav = mFavText.getText().toString();
                if (name == null || name.equals("") || fav == null || fav.equals("")) {
                    alertUserAboutError();
                } else {
                    startGame(name, fav);
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count % 2 == 0) {
                    infoView.setText(getInfo());
                } else {
                    infoView.setText("");
                }
                count++;
            }
        });
    }

    private void startGame(String name, String fav) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getString(R.string.name), name);
        intent.putExtra(getString(R.string.fav), fav);
        startActivity(intent);
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_message");
    }

    private String getInfo() {
        return "Your task is to take a picture that most accurately " +
                "reflects the given word. Points will be given out " +
                "accordingly to how similar (or different) your picture" +
                "is to the word.";
    }
}
