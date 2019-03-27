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

        findViewById(R.id.sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSignInButtonPressed(mEmail.getText().toString(), mPassword.getText().toString());
            }
        });


        findViewById(R.id.register_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Register is premium only, 50$/month.", Toast.LENGTH_LONG).show();
                // presenter.onRegisterButtonPressed();
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
        startActivity(new Intent(this, MainActivity.class));
        finish();
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

