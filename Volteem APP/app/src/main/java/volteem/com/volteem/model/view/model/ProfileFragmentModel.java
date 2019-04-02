package volteem.com.volteem.model.view.model;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.entity.User;

public class ProfileFragmentModel {

    private ModelCallback modelCallback;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    private User user;

    public ProfileFragmentModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }


    public void getProfileInformation()
    {
        FirebaseUser firebaseUser=mAuth.getCurrentUser();

        ValueEventListener mVolunteerProfileListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    return;
                }

                modelCallback.onProfileInformationSucceeded(user.getFirstName()+" "+user.getLastName(),user.geteMail(),user.getCity(),getAgeFromLong(user.getBirthDate()),user.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            modelCallback.onProfileInformationFailed(new VolteemCommonException("Information Profile",
                    "Could not get information for profile"));
            }
        };
        assert firebaseUser != null;
        mDatabase.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(mVolunteerProfileListener);
        mDatabase.removeEventListener(mVolunteerProfileListener);

    }


    public void getProfilePicture()
    {




    }

    public String getAgeFromLong(long age)
    {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(age);

        //todo modify with month
        return Calendar.getInstance().get(Calendar.YEAR)-calendar.get(Calendar.YEAR)+"";
    }

    public interface ModelCallback {

        void onProfileInformationSucceeded(String username, String email, String address,
                                           String age, String phone);

        void onProfileInformationFailed(VolteemCommonException loginException);

    }
}
