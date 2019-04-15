package volteem.com.volteem.presenter;

import android.net.Uri;
import android.text.TextUtils;

import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.RegisterActivityModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemConstants;

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
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EMAIL
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (!eMail.contains("@") || !eMail.contains("."))
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EMAIL
                    , VolteemConstants.EXCEPTION_EMAIL_MESSAGE_INVALID);
        //password related errors
        if (password.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_PASSWORD
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (password.length() < 6)
            return new VolteemCommonException(VolteemConstants.EXCEPTION_PASSWORD
                    , VolteemConstants.EXCEPTION_PASSWORD_LENGTH);
        if (confirmPassword.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_CONFIRM_PASSWORD,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (!TextUtils.equals(password, confirmPassword))
            return new VolteemCommonException(VolteemConstants.EXCEPTION_CONFIRM_PASSWORD
                    , VolteemConstants.EXCEPTION_MESSAGE_PASSWORDS_NOT_MATCH);
        // other errors
        if (firstName.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_FIRST_NAME
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (lastName.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_LAST_NAME
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (birthdate == 0)
            return new VolteemCommonException(VolteemConstants.EXCEPTION_BIRTH_DATE
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (phone.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_PHONE
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (city.isEmpty())
            return new VolteemCommonException(VolteemConstants.EXCEPTION_CITY
                    , VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        if (TextUtils.equals(gender, "Gender"))
            return new VolteemCommonException(VolteemConstants.EXCEPTION_GENDER
                    , VolteemConstants.EXCEPTION_MESSAGE_GENDER_EMPTY);
        return null;
    }

    public void registerUser(String eMail, String password, String confirmPassword, String firstName, String lastName,
                             long birthdate, String city, String phone, String gender, Uri uri) {

        VolteemCommonException volteemCommonException = validateForm(eMail, password, confirmPassword, firstName, lastName, birthdate, city, phone, gender);
        if (volteemCommonException != null) {
            view.onRegisterFailed(volteemCommonException);
            return;
        }
        databaseUtils.registerNewUser(eMail, password, firstName, lastName, birthdate, city, phone, gender, uri);
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
