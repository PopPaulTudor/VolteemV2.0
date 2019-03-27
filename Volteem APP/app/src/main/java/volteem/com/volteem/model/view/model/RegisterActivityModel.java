package volteem.com.volteem.model.view.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.model.entity.User;

public class RegisterActivityModel {

    private static final String TAG = "RegisterActivityModel";
    private ModelCallback modelCallback;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private LoginException loginException;

    public RegisterActivityModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void registerNewUser(final String eMail, String password, String confirmPassword, final String firstName, final String lastName,
                                final long birthdate, final String city, final String phone, final String gender) {

        loginException = validateForm(eMail, password, confirmPassword, firstName, lastName, birthdate, city, phone, gender);
        if (loginException != null) {
            modelCallback.onRegisterFailed(loginException);
            return;
        }
        Log.d(TAG, "creating account...");
        mAuth.createUserWithEmailAndPassword(eMail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "creating user: successful");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID;
                            if (user != null) {
                                userID = user.getUid();
                                User newUser = new User(firstName, lastName, eMail, city, phone, gender, birthdate);
                                mDatabase.child("users").child(userID).setValue(newUser);

                                UserProfileChangeRequest mProfileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstName)
                                        .build();

                                user.updateProfile(mProfileUpdate);
                                user.sendEmailVerification();
                            }
                            modelCallback.onRegisterSucceeded();

                        } else {
                            Log.d(TAG, "creating user: failed");
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                loginException = new LoginException("email", "Email address is already in use.");
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    loginException = new LoginException("email", "Please enter a valid email address.");
                                } else {
                                    loginException = new LoginException("unknown", task.getException().getMessage());
                                    Log.w("Error registering ", task.getException());
                                }
                            }
                            modelCallback.onRegisterFailed(loginException);
                        }
                    }

                });

    }

    private LoginException validateForm(final String eMail, String password, String confirmPassword, final String firstName,
                                        final String lastName, final long birthdate, final String city, final String phone,
                                        final String gender) {
        //eMail related errors
        if (eMail.isEmpty())
            return new LoginException("email", "This field cannot be empty.");
        if (!eMail.contains("@") || !eMail.contains("."))
            return new LoginException("email", "Please enter a valid email address.");
        //password related errors
        if (password.isEmpty())
            return new LoginException("password", "This field cannot be empty.");
        if (confirmPassword.isEmpty())
            return new LoginException("confirm_password", "This field cannot be empty.");
        if (!TextUtils.equals(password, confirmPassword))
            return new LoginException("confirm_password", "Passwords do not match");
        if (password.length() < 6)
            return new LoginException("password", "Your password must be at least 6 characters long.");
        // other errors
        if (firstName.isEmpty())
            return new LoginException("firstname", "This field cannot be empty.");
        if (lastName.isEmpty())
            return new LoginException("lastname", "This field cannot be empty.");
        if (birthdate == 0)
            return new LoginException("birthdate", "This field cannot be empty.");
        if (city.isEmpty())
            return new LoginException("city", "This field cannot be empty.");
        if (phone.isEmpty())
            return new LoginException("phone", "This field cannot be empty.");
        if (TextUtils.equals(gender, "Gender"))
            return new LoginException("gender", "Please select a gender.");
        return null;
    }

    public interface ModelCallback {
        void onRegisterSucceeded();

        void onRegisterFailed(LoginException loginException);
    }
}
