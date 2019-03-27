package volteem.com.volteem.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.presenter.LoginActivityPresenter;

public class LoginActivity extends AppCompatActivity implements LoginActivityPresenter.View {

    private static final String TAG = "LoginActivity";
    private EditText mEmail, mPassword;
    private ProgressDialog mProgressDialog;
    private View mProgressView;
    private View mLoginFormView;
    private TextView status;
    private LoginActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginActivityPresenter(this);
        presenter.onCreate();
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmail.setFilters(createEmailAddressFilter());

        if (status == null) {

            status = findViewById(R.id.login_status);
            status.setText("Log in status: not logged in.");
        }

        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSignInButtonPressed(mEmail.getText().toString(), mPassword.getText().toString());
            }
        });


        findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!presenter.isUserLoggedIn()) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "already logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TODO: after testing delette goto mainactivity and sign out buttons
        findViewById(R.id.go_to_mainactivity).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isUserLoggedIn()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.sign_out).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isUserLoggedIn()) {
                    presenter.logOut();
                    status.setText("Log in status: not logged in");
                } else {
                    Toast.makeText(LoginActivity.this, "not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private InputFilter[] createEmailAddressFilter() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        return new InputFilter[]{filter};
    }

    /*private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }*/

    @Override
    public void onSignInCompleted() {
        //TODO: after testing go back to this method's functionality
        if (status == null)
            status = findViewById(R.id.login_status);
        status.setText("Log in status: logged in");
        //startActivity(new Intent(this, MainActivity.class));
        //finish();
    }

    @Override
    public void onSignInFailed(LoginException loginException) {
        mEmail.setError(null);
        mPassword.setError(null);
        if (loginException.getCause().equals("email")) {
            mEmail.setError(loginException.getMessage());
            mEmail.requestFocus();
        } else {
            mPassword.setError(loginException.getMessage());
            mPassword.requestFocus();
        }
    }
}

