package app.sunshine.sakr.com.findyourchild.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import app.sunshine.sakr.com.findyourchild.R;
import app.sunshine.sakr.com.findyourchild.utility.TrackerApplication;


public class SignUpActivity extends ActionBarActivity
{
    protected EditText mUserName;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignup;
    protected Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mUserName = (EditText)findViewById(R.id.userNameField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        mEmail = (EditText)findViewById(R.id.emailField);

        mSignup = (Button)findViewById(R.id.signupButton);
        mSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                if(username.isEmpty() || password.isEmpty() || email.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message);
                    builder.setTitle(R.string.signup_error_title);
                    builder.setPositiveButton(android.R.string.ok,null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } // end if
                else
                {
                    ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(username);
                    parseUser.setPassword(password);
                    parseUser.setEmail(email);
                    parseUser.signUpInBackground(new SignUpCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            if(e==null)
                            {
                                TrackerApplication.updateParseInstallation(ParseUser.getCurrentUser());

                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle(R.string.signup_error_title);
                                builder.setPositiveButton(android.R.string.ok,null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });

        mCancel = (Button)findViewById(R.id.cancelButton);
        mCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            } // end method onClick
        });
    } //end method onCreate

} //end class SignUpActivity
