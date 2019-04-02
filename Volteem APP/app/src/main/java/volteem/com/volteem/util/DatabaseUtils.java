package volteem.com.volteem.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.model.entity.VolteemCommonException;

public class DatabaseUtils {

    private static final String TAG = "DatabaseUtils";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private VolteemCommonException volteemCommonException;
    private LoginCallback loginCallback;
    private RegisterCallback registerCallback;
    private NewsCallback newsCallback;
    private ArrayList<NewsMessage> newsList;

    public DatabaseUtils(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public DatabaseUtils(RegisterCallback registerCallback) {
        this.registerCallback = registerCallback;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseUtils(NewsCallback newsCallback) {
        this.newsCallback = newsCallback;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void signIn(String eMail, String password) {
        if (mAuth.getCurrentUser() != null) {
            loginCallback.onSignInSucceeded();
            return;
        }
        Log.d(TAG, "signing in...");
        mAuth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            loginCallback.onSignInSucceeded();
                            Log.d(TAG, "signInWitheMail: success");
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                if (exception instanceof FirebaseAuthException) {
                                    if (exception.getMessage().contains("password")) {
                                        volteemCommonException = new VolteemCommonException("password", exception.getMessage());
                                    } else if (exception.getMessage().contains("email") ||
                                            exception.getMessage().contains("account") ||
                                            exception.getMessage().contains("user")) {
                                        volteemCommonException = new VolteemCommonException("email", exception.getMessage());
                                    } else {
                                        volteemCommonException = new VolteemCommonException("other", exception.getMessage());
                                        Log.e(TAG, exception.getMessage());
                                    }
                                } else {
                                    // In this case there can be any Exception
                                    Log.e(TAG, exception.getMessage());
                                    volteemCommonException = new VolteemCommonException("other", exception.getMessage());
                                }
                            }
                            loginCallback.onSignInFailed(volteemCommonException);
                            //TODO: handle more exceptions
                        }
                    }
                });
    }

    public void registerNewUser(final String eMail, String password, final String firstName, final String lastName,
                                final long birthdate, final String city, final String phone, final String gender) {

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
                            registerCallback.onRegisterSucceeded();
                        } else {
                            Log.d(TAG, "creating user: failed");
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                volteemCommonException = new VolteemCommonException("email", "Email address is already in use.");
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    volteemCommonException = new VolteemCommonException("email", "Please enter a valid email address.");
                                } else {
                                    volteemCommonException = new VolteemCommonException("other", task.getException().getMessage());
                                    Log.w("Error registering ", task.getException());
                                }
                            }
                            registerCallback.onRegisterFailed(volteemCommonException);
                        }
                    }

                });
    }

    public void retrieveNewsList() {
        newsList = new ArrayList<>();
        mDatabase.child("news").orderByChild("receivedBy").equalTo(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    newsList.add(dataSnapshot1.getValue(NewsMessage.class));
                }
                Collections.reverse(newsList);
                newsCallback.onDataRetrieved(newsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                VolteemCommonException volteemCommonException = new VolteemCommonException("unknown", databaseError.getMessage());
                newsCallback.onDataRetrieveFailed(volteemCommonException);
            }
        });
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public interface LoginCallback {

        void onSignInSucceeded();

        void onSignInFailed(VolteemCommonException volteemCommonException);
    }

    public interface RegisterCallback {

        void onRegisterSucceeded();

        void onRegisterFailed(VolteemCommonException volteemCommonException);
    }

    public interface NewsCallback {

        void onDataRetrieved(ArrayList<NewsMessage> newsList);

        void onDataRetrieveFailed(VolteemCommonException volteemCommonException);
    }
}