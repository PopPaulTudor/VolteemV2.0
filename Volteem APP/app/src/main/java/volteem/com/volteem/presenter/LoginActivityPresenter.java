package volteem.com.volteem.presenter;

import android.text.TextUtils;
import android.util.Log;

import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.LoginActivityModel;
import volteem.com.volteem.util.DatabaseUtils;

public class LoginActivityPresenter implements Presenter, DatabaseUtils.LoginCallback {

    private final static String TAG = "LoginActivityPresenter";
    private View view;
    private LoginActivityModel model;
    private DatabaseUtils databaseUtils;

    public LoginActivityPresenter(View view) {
        this.view = view;
        this.model = new LoginActivityModel();
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new LoginActivityModel();
        }
        if (databaseUtils == null) {
            this.databaseUtils = new DatabaseUtils(this);
        }
        if (databaseUtils.isUserLoggedIn()) {
            view.onSignInCompleted();
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

    public void signIn(String eMail, String password) {
        Log.d(TAG, "signing in...");
        if (!databaseUtils.isUserLoggedIn()) {
            VolteemCommonException volteemCommonException = validateForm(eMail, password);
            if (volteemCommonException != null) {
                view.onSignInFailed(volteemCommonException);
                return;
            }
            databaseUtils.signIn(eMail, password);
        } else
            view.onSignInCompleted();
    }

    private VolteemCommonException validateForm(String eMail, String password) {
        if (TextUtils.isEmpty(eMail))
            return new VolteemCommonException("email", "Email address cannot be empty.");
        if (!eMail.contains("@") || !eMail.contains("."))
            return new VolteemCommonException("email", "Please enter a valid email address.");
        if (TextUtils.isEmpty(password))
            return new VolteemCommonException("password", "Password cannot be empty.");
        return null;
    }

    @Override
    public void onSignInSucceeded() {
        view.onSignInCompleted();
    }

    @Override
    public void onSignInFailed(VolteemCommonException volteemCommonException) {
        view.onSignInFailed(volteemCommonException);
    }

    public interface View {
        void onSignInCompleted();

        void onSignInFailed(VolteemCommonException volteemCommonException);
    }
}
