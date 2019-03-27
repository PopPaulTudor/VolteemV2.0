package volteem.com.volteem.presenter;

import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.model.view.model.RegisterActivityModel;

public class RegisterActivityPresenter implements Presenter, RegisterActivityModel.ModelCallback {

    private View view;
    private RegisterActivityModel model;

    public RegisterActivityPresenter(View view) {
        this.view = view;
        this.model = new RegisterActivityModel(this);
    }

    @Override
    public void onCreate() {
        this.model = new RegisterActivityModel(this);
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

    public void onRegisterButtonPressed(String eMail, String password, String confirmPassword, String firstName, String lastName,
                                        long birthdate, String city, String phone, String gender) {

        model.registerNewUser(eMail, password, confirmPassword, firstName, lastName, birthdate, city, phone, gender);
    }

    @Override
    public void onRegisterSucceeded() {
        view.onRegisterSuccessful();
    }

    @Override
    public void onRegisterFailed(LoginException loginException) {
        view.onRegisterFailed(loginException);
    }

    public interface View {
        void onRegisterSuccessful();

        void onRegisterFailed(LoginException loginException);
    }
}
