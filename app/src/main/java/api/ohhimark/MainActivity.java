package api.ohhimark;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private EditText loginUsername;
    private EditText password;
    private Button loginButton;
    private TextView signUpTextView;
    private TextView logo;

    private boolean loginStatus = true;



    public void signUp (View view)
    {
        if (loginStatus)
        {
            loginButton.setText("Sign Up");
            signUpTextView.setText("Login");
            loginStatus = false;

        } else
        {
            loginButton.setText("Login");
            signUpTextView.setText("Sign Up");
            loginStatus = true;

        }


    }

    public void login (View view)
    {
        if (loginStatus)
        {
            ParseUser.logInInBackground(loginUsername.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null )
                    {
                        usersActivity();
                        Log.i("info", "Logged in");

                    } else  {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } else
        {
            ParseUser user = new ParseUser();
            user.setUsername(loginUsername.getText().toString());
            user.setPassword(password.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        usersActivity();


                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginUsername = (EditText) findViewById(R.id.loginEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        signUpTextView = (TextView) findViewById(R.id.textView2);
        logo = (TextView) findViewById(R.id.hiTextView);
        ParseUser.logOut();

        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.consLayout);
        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    private void usersActivity ()
    {
        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
        startActivity(intent);
    }

}
