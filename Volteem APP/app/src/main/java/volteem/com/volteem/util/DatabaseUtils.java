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
import volteem.com.volteem.model.entity.RegisteredUser;
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
    private SingleEventCallback singleEventCallback;
    private EventInfoCallback eventInfoCallback;
    private EventUsersCallback eventUsersCallback;
    private FeedbackRetrieverCallback feedbackRetrieverCallback;

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

    public DatabaseUtils(SingleEventCallback singleEventCallback) {
        this.singleEventCallback = singleEventCallback;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseUtils(EventInfoCallback eventInfoCallback) {
        this.eventInfoCallback = eventInfoCallback;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseUtils(EventUsersCallback eventUsersCallback) {
        this.eventUsersCallback = eventUsersCallback;
    }

    /**
     * static method returns id of signed in user
     *
     * @return String: id of the currently signed in user
     */
    public static String getUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    /**
     * accepts a user to an event
     *
     * @param userID String: id of the user to be accepted
     * @param event  Event: event to be accepted to
     */
    public static void acceptVolunteer(String userID, Event event) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("events").child(event.getEventID()).child("users").child(userID).child("status").setValue(VolteemConstants.VOLUNTEER_EVENT_STATUS_ACCEPTED);
        String newsID = databaseReference.child("news").push().getKey();
        databaseReference.child("news").child(newsID).setValue(new NewsMessage(getUserID(), userID, newsID, VolteemConstants.MESSAGE_ACCEPTED_TO_EVENT + " " +
                event.getName() + "!", CalendarUtils.getCurrentTimeInMillis(), NewsMessage.Type.ACCEPTED_TO_EVENT, false, false, event.getEventID()));
    }

    /**
     * performs the Firebase Login.
     *
     * @param eMail    user eMail
     * @param password user password
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

    /**
     * Static method which performs the sign out of the user. Updating the View is recommended after calling this method.
     */
    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * registers a new user with Firebase; this method creates both the account in Firebase Auth AND the user's entry in the
     * database, populated with his data
     *
     * @param eMail     user email: this address will be used to perform signIn; unchangeable
     * @param password  user password
     * @param firstName user first name
     * @param lastName  user last name
     * @param birthdate user birth date
     * @param city      user city
     * @param phone     user phone
     * @param gender    user gender (oops)
     * @param uri       user profile picture URI
     */
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
                                User newUser = new User(userID, firstName, lastName, eMail, city, phone, gender, birthdate, 0, CalendarUtils.getCurrentTimeInMillis());
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

    /**
     * retrieves the news list of the currently signed in user
     */
    public void retrieveNewsList() {
        final ArrayList<NewsMessage> newsList = new ArrayList<>();
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

    /**
     * retrieves the profile information of the currently signed in user
     */
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

    /**
     * retrieves the picture of the currently signed in user
     */
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

    /**
     * retrieves the list of events the current user has participated to in the past
     */
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

    /**
     * changes the profile picture of the currently signed in user
     *
     * @param uri new profile picture URI
     */
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

    /**
     * changes the profile information of the currently signed in user
     *
     * @param firstName  new first name
     * @param secondName new last name
     * @param phone      new phone
     * @param address    new address
     * @param birthdate  new birth date
     */
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

    /**
     * retrieves the list of events the currently signed in user hasn't already registered to
     * callbacks to the presenter when the retrieval is finished
     */
    public void getUnregisteredEventsList() {
        final ArrayList<Uri> imageUris = parseImageUris();
        mDatabase.child("events").orderByChild("users/" + mAuth.getUid()).equalTo(null)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<Event> mEventsList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Event currentEvent = data.getValue(Event.class);
                            if (currentEvent.getDeadline() > CalendarUtils.getCurrentTimeInMillis()) {
                                mEventsList.add(currentEvent);
                                if (currentEvent.getImageUri() == null)
                                    currentEvent.setImageUri(imageUris.get(currentEvent.getType().ordinal() - 1).toString());
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

    /**
     * retrieves the list of events the currently signed in user has already registered to
     * callbacks to the presenter when the retrieval is finished
     */
    public void getRegisteredEventsList() {
        final ArrayList<Uri> imageUris = parseImageUris();
        mDatabase.child("events").orderByChild("users/" + mAuth.getUid() + "/flag").equalTo(VolteemConstants.VOLUNTEER_EVENT_FLAG_PENDING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<Event> mEventsList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Event currentEvent = data.getValue(Event.class);
                            if (currentEvent.getFinishDate() > CalendarUtils.getCurrentTimeInMillis()) {
                                mEventsList.add(currentEvent);
                                if (currentEvent.getImageUri() == null)
                                    currentEvent.setImageUri(imageUris.get(currentEvent.getType().ordinal() - 1).toString());
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

    /**
     * retrieves the list of events the currently signed in user has personally created himself
     * callbacks to the presenter when the retrieval is finished
     */
    public void getOwnEventsList() {
        final ArrayList<Uri> imageUris = parseImageUris();
        mDatabase.child("events").orderByChild("createdBy").equalTo(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<Event> mEventsList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            final Event currentEvent = data.getValue(Event.class);
                            if (currentEvent.getFinishDate() > CalendarUtils.getCurrentTimeInMillis()) {
                                mEventsList.add(currentEvent);
                                if (currentEvent.getImageUri() == null)
                                    currentEvent.setImageUri(imageUris.get(currentEvent.getType().ordinal() - 1).toString());
                            }
                            ArrayList<String> regUsers = new ArrayList<>();
                            ArrayList<String> accUsers = new ArrayList<>();
                            for (DataSnapshot registeredUsers : data.child("users").getChildren()) {
                                if (TextUtils.equals(String.valueOf(registeredUsers.child("status").getValue()), VolteemConstants
                                        .VOLUNTEER_EVENT_STATUS_PENDING)) {
                                    regUsers.add(String.valueOf(registeredUsers.child("id").getValue()));
                                } else {
                                    accUsers.add(String.valueOf(registeredUsers.child("id").getValue()));
                                }
                            }

                            currentEvent.setRegisteredVolunteers(regUsers);
                            currentEvent.setAcceptedVolunteers(accUsers);
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

    /**
     * @return ArrayList of Uri representing all the default event images corresponding to the types
     */
    private ArrayList<Uri> parseImageUris() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_sports));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_music));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_festival));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_charity));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_training));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_other));
        return imageUris;
    }

    /**
     * @return boolean representing the answer to the question in the method's name
     */
    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    /**
     * creates a new event to be stored it in the database;
     * stores the event's picture in the Firebase storage
     * callsback to presenter when finished
     *
     * @param eventName        String: event name
     * @param location         String: event location
     * @param startDate        long: event start date in MM
     * @param finishDate       long: event finish date in MM
     * @param type             Event.Type: event type
     * @param description      String: event description
     * @param deadline         long: event deadline in MM
     * @param volunteersNeeded int: event size
     * @param mUriPicture      Uri: event picture Uri
     */
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

    /**
     * writes the event to the database
     *
     * @param event Event: event to be written to database
     */
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

    /**
     * registers the currently signed in user to the event whose data is provided as parameters;
     * callsback to the presenter when finished
     *
     * @param eventID    String: id of the event to register to (usually the eventID field of an Event instance)
     * @param eventOwner String: id of the creator of the event to register to (usually the createdBy field of an Event instance)
     * @param eventName  String String: name of the event to register to (usually the name field of an Event instance)
     */
    public void registerToEvent(String eventID, String eventOwner, String eventName) {
        String newsID = mDatabase.child("news").push().getKey();

        mDatabase.child("news/" + newsID).setValue(new NewsMessage(mAuth.getUid(), eventOwner, newsID, VolteemConstants.MESSAGE_NEW_VOLUNTEER_REGISTERED + " " + eventName,
                CalendarUtils.getCurrentTimeInMillis(), NewsMessage.Type.VOLUNTEER_REGISTERED_TO_EVENT, false, false, eventID));

        mDatabase.child("events").child(eventID).child("users")
                .child(mAuth.getUid())
                .setValue(new RegisteredUser(mAuth.getUid(), VolteemConstants.VOLUNTEER_EVENT_STATUS_PENDING, VolteemConstants.VOLUNTEER_EVENT_FLAG_PENDING))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            singleEventCallback.onRegisterToEventSuccessful();
                        } else {
                            singleEventCallback.onRegisterToEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                                    task.getException().getMessage()));
                        }
                    }
                });
    }

    /**
     * deletes the entry of the currently signed in user to the event with the given id;
     * generates negative feedback for the user;
     * callsback to the presenter when finished
     *
     * @param eventID   String: id of the event to leave
     * @param createdBy String: id of the organiser of the event to leave
     * @param eventName String: name of the event to leave
     */
    public void leaveEvent(final String eventID, String createdBy, final String eventName) {
        String newsID = mDatabase.child("news").push().getKey();
        mDatabase.child("news/" + newsID).setValue(new NewsMessage(mAuth.getUid(), createdBy, newsID, VolteemConstants.MESSAGE_VOLUNTEER_LEFT +
                " " + eventName, CalendarUtils.getCurrentTimeInMillis(), NewsMessage.Type.VOLUNTEER_LEFT, false, false, eventID));
        mDatabase.child("events").child(eventID).child("users").child(mAuth.getUid()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDatabase.child("users/" + mAuth.getUid() + "/feedback/" + eventID).setValue("This user has left the event " + eventName);
                    singleEventCallback.onLeaveEventSuccessful();
                } else {
                    singleEventCallback.onLeaveEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                            task.getException().getMessage()));
                }
            }
        });
    }

    /**
     * updates an event in the database
     * callbacks to presenter when finished
     *
     * @param event Event: event to be updated
     */
    public void updateEvent(final Event event) {
        mDatabase.child("events/" + event.getEventID()).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    eventInfoCallback.onEditEventSuccessful(event);
                } else {
                    eventInfoCallback.onEditEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                            task.getException().getMessage()));
                }
            }
        });
    }

    /**
     * deletes the event from the database and announces all the users registered to it
     * callsback to the presenter when finished
     *
     * @param eventToDelete Event: the event to be deleted
     */
    public void deleteEvent(Event eventToDelete) {
        for (String volunteer_id : eventToDelete.getRegisteredVolunteers()) {
            String newsID = mDatabase.child("news").push().getKey();
            NewsMessage newsMessage = new NewsMessage(getUserID(), volunteer_id, newsID, eventToDelete.getName() + " " + VolteemConstants.MESSAGE_EVENT_DELETED,
                    CalendarUtils.getCurrentTimeInMillis(), NewsMessage.Type.EVENT_DELETED, false, false, eventToDelete.getEventID());
            mDatabase.child("news/" + newsID).setValue(newsMessage);
        }
        for (String volunteer_id : eventToDelete.getAcceptedVolunteers()) {
            String newsID = mDatabase.child("news").push().getKey();
            NewsMessage newsMessage = new NewsMessage(getUserID(), volunteer_id, newsID, eventToDelete.getName() + " " + VolteemConstants.MESSAGE_EVENT_DELETED,
                    CalendarUtils.getCurrentTimeInMillis(), NewsMessage.Type.EVENT_DELETED, false, false, eventToDelete.getEventID());
            mDatabase.child("news/" + newsID).setValue(newsMessage);
        }
        mDatabase.child("events").child(eventToDelete.getEventID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    eventInfoCallback.onDeleteEventSuccessful();
                } else {
                    eventInfoCallback.onDeleteEventFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                            task.getException().getMessage()));
                }
            }
        });
    }

    /**
     * retrieves the list of users
     *
     * @param usersIds ArrayList of String: list of ids of users whose profiles need to be retrieved
     */
    public void getEventUsersList(final ArrayList<String> usersIds) {
        final ArrayList<User> registeredUsersList = new ArrayList<>();
        for (final String userID : usersIds) {
            FirebaseDatabase.getInstance().getReference().child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    registeredUsersList.add(currentUser);
                    if (usersIds.size() == registeredUsersList.size()) {
                        eventUsersCallback.onRetrieveUsersListSuccessful(registeredUsersList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    eventUsersCallback.onRetrieveUsersListFailed(new VolteemCommonException(VolteemConstants.EXCEPTION_OTHER,
                            databaseError.getMessage()));
                }
            });
        }
    }

    public void getUserFeedbackList() {

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

    public interface SingleEventCallback {
        void onRegisterToEventSuccessful();

        void onRegisterToEventFailed(VolteemCommonException exception);

        void onLeaveEventSuccessful();

        void onLeaveEventFailed(VolteemCommonException exception);
    }

    public interface EventInfoCallback {
        void onEditEventSuccessful(Event updatedEvent);

        void onEditEventFailed(VolteemCommonException exception);

        void onDeleteEventSuccessful();

        void onDeleteEventFailed(VolteemCommonException exception);
    }

    public interface EventUsersCallback {
        void onRetrieveUsersListSuccessful(ArrayList<User> registeredUsers);

        void onRetrieveUsersListFailed(VolteemCommonException exception);
    }

    public interface FeedbackRetrieverCallback {
        void onRetrieveFeedbackSuccessful(ArrayList<String> feedbackList);
    }
}
