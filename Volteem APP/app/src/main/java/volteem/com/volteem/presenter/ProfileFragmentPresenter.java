package volteem.com.volteem.presenter;



import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.model.view.model.ProfileFragmentModel;


public class ProfileFragmentPresenter  implements Presenter, ProfileFragmentModel.ModelCallback {

    private View view;
    private ProfileFragmentModel model;

    public ProfileFragmentPresenter(View view)
    {
        this.view= view;
        this.model=new ProfileFragmentModel(this);
    }

    @Override
    public void onCreate() {

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

    public void getProfileInformation()
    {
        model.getProfileInformation();

    }

    public void getProfilePicture()
    {
        model.getProfilePicture();
    }

    @Override
    public void onProfileInformationSucceeded(String username, String email, String address, String age, String phone) {

        view.onProfileInformationSucceeded(username, email,address, age, phone);
    }

    @Override
    public void onProfileInformationFailed(LoginException loginException) {
        view.onProfileInformationFailed(loginException);
    }


    public interface  View
    {
        void onProfileInformationSucceeded(String username, String email, String address, String age, String phone);
        void onProfileInformationFailed(LoginException loginException);
    }
}
