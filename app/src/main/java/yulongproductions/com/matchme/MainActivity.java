package yulongproductions.com.matchme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mStartButton;
    private Button infoButton;
    private TextView infoView;
    private EditText mNameText;
    private EditText password;
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
        password = (EditText)findViewById(R.id.passwordEditText);
        mGlobal = new GlobalFunctions(this);
        mGlobal.setupUI(findViewById(R.id.mainLayout));

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameText.getText().toString();
                String password = MainActivity.this.password.getText().toString();
                if (this.isEmpty(name) || this.isEmpty(password)) {
                    alertUserAboutError();
                } else {
                    startGame(name, password);
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

    private void startGame(String name, String password) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(getString(R.string.name), name);
        intent.putExtra(getString(R.string.password), password);
        startActivity(intent);
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_message");
    }

    private boolean isEmpty(String field) {
        return field == null || field.equals("");
    }

    private String getInfo() {
        return "Your task is to take a picture that most accurately " +
                "reflects the given word. Points will be given out " +
                "accordingly to how similar (or different) your picture" +
                "is to the word.";
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNameText.setText("");
        password.setText("");
    }

    @Override
    public void onBackPressed() {
    }
}