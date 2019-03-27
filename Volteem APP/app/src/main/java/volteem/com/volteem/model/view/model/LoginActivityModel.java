package volteem.com.volteem.model.view.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import volteem.com.volteem.model.entity.LoginException;

public class LoginActivityModel {

    private static final String TAG = "LoginActivityModel";
    private ModelCallback modelCallback;
    private FirebaseAuth mAuth;
    private LoginException loginException;

    public LoginActivityModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void signIn(String eMail, String password) {
        if (mAuth.getCurrentUser() != null) {
            modelCallback.onSignInSucceeded();
            return;
        }
        Log.d(TAG, "signing in...");
        loginException = validateForm(eMail, password);
        if (loginException != null) {
            modelCallback.onSignInFailed(loginException);
            return;
        }
        mAuth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            modelCallback.onSignInSucceeded();
                            Log.d(TAG, "signInWitheMail: success");
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                if (exception instanceof FirebaseAuthException) {
                                    if (exception.getMessage().contains("password")) {
                                        loginException = new LoginException("password", exception.getMessage());
                                    } else if (exception.getMessage().contains("email") ||
                                            exception.getMessage().contains("account") ||
                                            exception.getMessage().contains("user")) {
                                        loginException = new LoginException("email", exception.getMessage());
                                    } else {
                                        Log.e(TAG, exception.getMessage());
                                    }
                                } else {
                                    // In this case there can be any Exception
                                    Log.e(TAG, exception.getMessage());
                                }
                            }
                            modelCallback.onSignInFailed(loginException);
                            //TODO: handle more exceptions
                        }
                    }
                });
    }

    private LoginException validateForm(String eMail, String password) {
        if (TextUtils.isEmpty(eMail))
            return new LoginException("email", "Email address cannot be empty.");
        if (TextUtils.isEmpty(password))
            return new LoginException("password", "Password cannot be empty.");
        if (!eMail.contains("@") || !eMail.contains("."))
            return new LoginException("email", "Please enter a valid email address.");
        if (password.length() < 6)
            return new LoginException("password", "Password must be at least 6 characters long.");
        return null;
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void logOut() {
        mAuth.signOut();
    }

    public interface ModelCallback {
        void onSignInSucceeded();

        void onSignInFailed(LoginException loginException);
    }
}
