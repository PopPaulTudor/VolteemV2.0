package volteem.com.volteem.model.view.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import volteem.com.volteem.model.entity.LoginException;

public class RegisterActivityModel {

    private ModelCallback modelCallback;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mEmail, mPassword, mPhone, mCity, mFirstname, mLastname, mConfirmPass;
    private long mBirthdate;

    public RegisterActivityModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    public void registerNewUser(String eMail, String password, String confirmPassword, String firstName, String lastName,
                                long birthdate, String city, String phone, String gender) {

    }

    public interface ModelCallback {
        void onRegisterFailed();
        void onRegisterFailed(LoginException loginException);
    }
}
