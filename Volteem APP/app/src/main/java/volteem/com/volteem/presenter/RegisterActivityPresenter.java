package volteem.com.volteem.presenter;

import android.text.TextUtils;

import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.RegisterActivityModel;
import volteem.com.volteem.util.DatabaseUtils;

public class RegisterActivityPresenter implements Presenter, DatabaseUtils.RegisterCallback {

    private View view;
    private RegisterActivityModel model;
    private DatabaseUtils databaseUtils;

    public RegisterActivityPresenter(View view) {
        this.view = view;
        this.model = new RegisterActivityModel();
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new RegisterActivityModel();
        }
        if (databaseUtils == null) {
            this.databaseUtils = new DatabaseUtils(this);
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    private VolteemCommonException validateForm(final String eMail, String password, String confirmPassword, final String firstName,
                                                final String lastName, final long birthdate, final String city, final String phone,
                                                final String gender) {
        //eMail related errors
        if (eMail.isEmpty())
            return new VolteemCommonException("email", "This field cannot be empty.");
        if (!eMail.contains("@") || !eMail.contains("."))
            return new VolteemCommonException("email", "Please enter a valid email address.");
        //password related errors
        if (password.isEmpty())
            return new VolteemCommonException("password", "This field cannot be empty.");
        if (password.length() < 6)
            return new VolteemCommonException("password", "Your password must be at least 6 characters long.");
        if (confirmPassword.isEmpty())
            return new VolteemCommonException("confirm_password", "This field cannot be empty.");
        if (!TextUtils.equals(password, confirmPassword))
            return new VolteemCommonException("confirm_password", "Passwords do not match");
        // other errors
        if (firstName.isEmpty())
            return new VolteemCommonException("firstname", "This field cannot be empty.");
        if (lastName.isEmpty())
            return new VolteemCommonException("lastname", "This field cannot be empty.");
        if (birthdate == 0)
            return new VolteemCommonException("birthdate", "This field cannot be empty.");
        if (phone.isEmpty())
            return new VolteemCommonException("phone", "This field cannot be empty.");
        if (city.isEmpty())
            return new VolteemCommonException("city", "This field cannot be empty.");
        if (TextUtils.equals(gender, "Gender"))
            return new VolteemCommonException("gender", "Please select a gender.");
        return null;
    }

    public void registerUser(String eMail, String password, String confirmPassword, String firstName, String lastName,
                             long birthdate, String city, String phone, String gender) {

        VolteemCommonException volteemCommonException = validateForm(eMail, password, confirmPassword, firstName, lastName, birthdate, city, phone, gender);
        if (volteemCommonException != null) {
            view.onRegisterFailed(volteemCommonException);
            return;
        }
        databaseUtils.registerNewUser(eMail, password, firstName, lastName, birthdate, city, phone, gender);
    }

    @Override
    public void onRegisterSucceeded() {
        view.onRegisterSuccessful();
    }

    @Override
    public void onRegisterFailed(VolteemCommonException volteemCommonException) {
        view.onRegisterFailed(volteemCommonException);
    }

    public interface View {
        void onRegisterSuccessful();

        void onRegisterFailed(VolteemCommonException volteemCommonException);
    }
}
