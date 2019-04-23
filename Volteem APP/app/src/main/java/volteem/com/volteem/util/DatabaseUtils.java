package volteem.com.volteem.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.Feedback;
import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.model.entity.VolteemCommonException;

public class DatabaseUtils {
    /* Methods that do not require callbacks can be declared static; also, if you don't need callbacks
        in a class, do not instantiate the DatabaseUtils as it is not needed, only call the static methods,
        and do not create interfaces for that class to implement.
    */
    private static final String TAG = "DatabaseUtils";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private VolteemCommonException volteemCommonException;
    private LoginCallback loginCallback;
    private RegisterCallback registerCallback;
    private NewsCallback newsCallback;
    private ProfileCallBack profileCallBack;
    private DisplayPhotoCallBack displayPhotoCallBack;
    private EventsCallback eventsCallback;
    private CreateEventCallback createEventCallback;
    private ArrayList<NewsMessage> newsList;
    private ArrayList<Event> mEventsList;
    private ArrayList<Uri> mUriList;

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

    public DatabaseUtils(ProfileCallBack profileCallBack) {
        this.profileCallBack = profileCallBack;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseUtils(DisplayPhotoCallBack displayPhotoCallBack) {
        this.displayPhotoCallBack = displayPhotoCallBack;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public DatabaseUtils(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseUtils(CreateEventCallback createEventCallback) {
        this.createEventCallback = createEventCallback;
    }

    /**
     * signIn method takes in two Strings (email & password) and performs the Firebase Login.
     * user eMail * @param eMail
     * user password * @param password
     */
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
                                        volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_PASSWORD
                                                , exception.getMessage());
                                    } else if (exception.getMessage().contains("email") ||
                                            exception.getMessage().contains("account") ||
                                            exception.getMessage().contains("user")) {
                                        volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_EMAIL
                                                , exception.getMessage());
                                    } else {
                                        volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER
                                                , exception.getMessage());
                                        Log.e(TAG, exception.getMessage());
                                    }
                                } else {
                                    // In this case there can be any Exception
                                    Log.e(TAG, exception.getMessage());
                                    volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER
                                            , exception.getMessage());
                                }
                            }
                            loginCallback.onSignInFailed(volteemCommonException);
                            //TODO: handle more exceptions
                        }
                    }
                });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void registerNewUser(final String eMail, String password, final String firstName, final String lastName,
                                final long birthdate, final String city, final String phone, final String gender, final Uri uri) {

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

                                StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                                StorageReference filePath = mStorage.child("Photos").child("User").child(userID);

                                if (uri != null)
                                    filePath.putBytes(ImageUtils.compressImage(uri, VolteemUtils.getContext().getResources()));
                            }
                            registerCallback.onRegisterSucceeded();
                        } else {
                            Log.d(TAG, "creating user: failed");
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_EMAIL
                                        , VolteemConstants.EXCEPTION_EMAIL_MESSAGE_ALREADY_IN_USE);
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_EMAIL
                                            , VolteemConstants.EXCEPTION_EMAIL_MESSAGE_INVALID);
                                } else {
                                    volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER
                                            , Objects.requireNonNull(task.getException()).getMessage());
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
        mDatabase.child("news").orderByChild("receivedBy").equalTo(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                VolteemCommonException volteemCommonException = new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER
                        , databaseError.getMessage());
                newsCallback.onDataRetrieveFailed(volteemCommonException);
            }
        });
    }

    public void getProfileInformation() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        ValueEventListener mVolunteerProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    profileCallBack.onProfileInformationFailed(new VolteemCommonException("User not found", "Can not retrieve information about the user"));
                    return;
                }
                profileCallBack.onProfileInformationSucceeded(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                profileCallBack.onProfileInformationFailed(new VolteemCommonException("Information Profile",
                        "Could not get information for profile"));
            }
        };
        if (firebaseUser != null) {
            mDatabase.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(mVolunteerProfileListener);
            mDatabase.removeEventListener(mVolunteerProfileListener);
        }
    }

    public void getProfilePicture() {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = mStorage.child("Photos").child("User").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profileCallBack.onProfilePictureSucceeded(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profileCallBack.onProfilePictureFailed(new VolteemCommonException("Profile picture", "could not get picture"));

            }
        });
    }

    public void getProfileEvents() {
        final String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase.child("events").orderByChild("users/" + userId + "/flag").equalTo(VolteemConstants.VOLUNTEER_EVENT_FLAG_DONE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayList<Event> mPastEventsList = new ArrayList<>();
                        final ArrayList<Feedback> feedbacks = new ArrayList<>();

                        for (final DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            boolean isAccepted = TextUtils.equals(Objects.requireNonNull(eventSnapshot.child("users/" + userId + "/status").getValue()).toString(), VolteemConstants.VOLUNTEER_EVENT_STATUS_ACCEPTED);
                            if (isAccepted) {
                                final Event event = eventSnapshot.getValue(Event.class);
                                mPastEventsList.add(event);

                                if (event != null) {
                                    mDatabase.child("users").child("volunteers").child(userId).child("feedback")
                                            .child(event.getEventID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            feedbacks.add(new Feedback(event.getEventID(), dataSnapshot.getValue(String.class)));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            profileCallBack.onEventsFailed(new VolteemCommonException("Events Profile", databaseError.getMessage()));
                                        }
                                    });
                                }
                            }
                        }
                        if (mPastEventsList.isEmpty()) {
                            profileCallBack.onEventsFailed(new VolteemCommonException("Events Profile", "There are no events"));

                        } else {
                            Collections.sort(mPastEventsList, new Comparator<Event>() {

                                public int compare(Event event, Event event1) {
                                    return Long.compare(event.getStartDate(), event1.getStartDate());
                                }
                            });
                            profileCallBack.onEventsSucceeded(mPastEventsList, feedbacks);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        profileCallBack.onEventsFailed(new VolteemCommonException("Events Profile", "Failed"));
                    }
                });
    }

    public void changeProfilePhoto(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference filePath = null;
        if (user != null) {
            filePath = mStorage.child("Photos").child("User").child(user.getUid());
        }
        if (uri != null) {
            if (filePath != null) {
                filePath.putBytes(ImageUtils.compressImage(uri, VolteemUtils.getContext().getResources()));
            }
            profileCallBack.onProfilePhotoChangedSucceeded("Succeeded");

        }
    }

    public void changeProfileData(String firstName, String secondName, String phone, String address, long birthdate) {
        String message = "";
        if (birthdate != 0) {
            mDatabase.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("birthDate").setValue(birthdate);
            message = message + " " + "birthdate";
        }
        if (firstName != null) {
            mDatabase.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("firstName").setValue(firstName);
            message = message + " " + "firstName";
        }
        if (secondName != null) {
            mDatabase.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("lastName").setValue(secondName);
            message = message + " " + "secondName";
        }
        if (phone != null) {
            mDatabase.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("phone").setValue(phone);
            message = message + " " + "phone";
        }
        if (address != null) {
            mDatabase.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("city").setValue(address);
            message = message + " " + "address";
        }

        if (!message.equals(""))
            profileCallBack.onProfileDataChangedSucceeded(message, firstName, secondName, phone, address, birthdate);
        else profileCallBack.onProfileDataChangedFailed();
    }

    public void getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayPhotoCallBack.onUserIdSucceeded(user.getUid());
        }
    }

    public void getEventsList() {
        final ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_sports));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_music));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_festival));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_charity));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_training));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_other));
        mDatabase.child("events").orderByChild("users/" + mAuth.getUid()).equalTo(null)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mEventsList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Event currentEvent = data.getValue(Event.class);
                            if (currentEvent.getDeadline() > CalendarUtils.getCurrentTimeInMillis()) {
                                mEventsList.add(currentEvent);
                                if (currentEvent.getImageUri() == null)
                                    currentEvent.setImageUri(imageUris.get(currentEvent.getType().ordinal()).toString());
                            }
                        }
                        eventsCallback.onEventsLoadSuccessful(mEventsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("VolEventsF: loadEvents", databaseError.getMessage());
                        eventsCallback.onEventsLoadFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER
                                , databaseError.getMessage()));
                    }
                });
    }

    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void createEvent(String eventName, String location, long startDate, long finishDate, Event.Type type,
                            String description, long deadline, int volunteersNeeded, Uri mUriPicture) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String eventID = mDatabase.child("events").push().getKey();
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Photos").child("Event").child(eventID);
        final Event newEvent = new Event(FirebaseAuth.getInstance().getCurrentUser().getUid(), eventName, location, description, eventID,
                null, startDate, finishDate, deadline, CalendarUtils.getCurrentTimeInMillis(), type, volunteersNeeded, null);
        if (mUriPicture != null) {
            filePath.putBytes(ImageUtils.compressImage(mUriPicture, null)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newEvent.setImageUri(uri.toString());
                                writeEventToDatabase(newEvent);
                            }
                        });
                    } else {
                        createEventCallback.onCreateEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                                task.getException().getMessage()));
                    }
                }
            });

        } else {
            writeEventToDatabase(newEvent);
        }
    }

    private void writeEventToDatabase(Event event) {
        mDatabase.child("events/" + event.getEventID()).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    createEventCallback.onCreateEventSuccessful();
                } else {
                    createEventCallback.onCreateEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                            task.getException().getMessage()));
                }
            }
        });
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

    public interface ProfileCallBack {
        void onProfileInformationSucceeded(User user);

        void onProfileInformationFailed(VolteemCommonException volteemCommonException);

        void onProfilePictureSucceeded(Uri uri);

        void onProfilePictureFailed(VolteemCommonException volteemCommonException);

        void onEventsSucceeded(ArrayList<Event> events, ArrayList<Feedback> feedbacks);

        void onEventsFailed(VolteemCommonException volteemCommonException);

        void onProfilePhotoChangedSucceeded(String message);

        void onProfileDataChangedSucceeded(String message, String firstName, String secondName, String phone, String address, long birthdate);

        void onProfileDataChangedFailed();
    }

    public interface DisplayPhotoCallBack {
        void onUserIdSucceeded(String userId);
    }

    public interface EventsCallback {
        void onEventsLoadSuccessful(ArrayList<Event> eventsList);

        void onEventsLoadFailed(VolteemCommonException exception);
    }

    public interface CreateEventCallback {
        void onCreateEventSuccessful();

        void onCreateEventFailed(VolteemCommonException exception);
    }
}
