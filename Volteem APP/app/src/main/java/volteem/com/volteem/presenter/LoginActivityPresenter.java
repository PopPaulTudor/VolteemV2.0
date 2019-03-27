package volteem.com.volteem.presenter;

import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.model.view.model.LoginActivityModel;

public class LoginActivityPresenter implements Presenter, LoginActivityModel.ModelCallback {

    private View view;
    private LoginActivityModel model;

    public LoginActivityPresenter(View view) {
        this.view = view;
        this.model = new LoginActivityModel(this);
    }

    @Override
    public void onCreate() {
        this.model = new LoginActivityModel(this);
        if(model.isUserLoggedIn())
            view.onSignInCompleted();
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

    public void onSignInButtonPressed(String eMail, String password) {
        if(!model.isUserLoggedIn())
            model.signIn(eMail, password);
        else
            view.onSignInCompleted();
    }

    @Override
    public void onSignInSucceeded() {
        view.onSignInCompleted();
    }

    @Override
    public void onSignInFailed(LoginException loginException) {
        view.onSignInFailed(loginException);
    }

    public interface View {
        void onSignInCompleted();
        void onSignInFailed(LoginException loginException);
    }
}
