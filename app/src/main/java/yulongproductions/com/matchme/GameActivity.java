package yulongproductions.com.matchme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class GameActivity extends ActionBarActivity {
    public static final String TAG = GameActivity.class.getSimpleName();

    private GrabAdjective mGrabAdjective = new GrabAdjective();
    private String name;
    private String fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        this.name = intent.getStringExtra(getString(R.string.name));
        this.fav = intent.getStringExtra(getString(R.string.fav));

        TextView mTextView = (TextView)findViewById(R.id.adjTextView);
        mTextView.setText(mGrabAdjective.getAdjective());
    }

    @Override
    public void onBackPressed() {
        this.alertUserAboutError();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_message");
    }
}
