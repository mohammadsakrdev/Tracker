package app.sunshine.sakr.com.findyourchild.ui;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import app.sunshine.sakr.com.findyourchild.R;
import app.sunshine.sakr.com.findyourchild.utility.TrackerApplication;


public class LoginActivity extends ActionBarActivity
{
    protected EditText mUserName;
    protected EditText mPassword;
    protected Button mLoginBtn;
    protected TextView mSignupText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mSignupText = (TextView)findViewById(R.id.signupText);
        mSignupText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mUserName = (EditText)findViewById(R.id.userNameField);
        mPassword = (EditText)findViewById(R.id.passwordField);

        mLoginBtn = (Button)findViewById(R.id.loginButton);

        mLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String userName = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(userName.isEmpty() || password.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(getString(R.string.login_error_title));
                    builder.setMessage(getString(R.string.login_error_message));
                    builder.setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }//end if
                else
                {
                    ParseUser.logInInBackground(userName,password,new LogInCallback()
                    {
                        @Override
                        public void done(ParseUser user, ParseException e)
                        {
                            if(e == null)
                            {
                                //success
                                TrackerApplication.updateParseInstallation(user);

                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }//end if
                            else
                            {
                                //login fail
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(getString(R.string.login_error_title));
                                builder.setMessage(getString(R.string.login_error_message));
                                builder.setPositiveButton(android.R.string.ok,null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }// end else
                        } // end method done
                    });
                }//end else
            } //end method onClick
        });

    } // end method onCreate
} // end class LoginActivity
