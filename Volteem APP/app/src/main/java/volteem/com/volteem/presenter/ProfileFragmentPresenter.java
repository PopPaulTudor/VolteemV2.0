package volteem.com.volteem.presenter;


import android.net.Uri;

import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.ProfileFragmentModel;
import volteem.com.volteem.util.CalendarUtils;
import volteem.com.volteem.util.DatabaseUtils;


public class ProfileFragmentPresenter implements Presenter, DatabaseUtils.ProfileCallBack {

    private View view;
    private ProfileFragmentModel model;
    private DatabaseUtils databaseUtils;

    public ProfileFragmentPresenter(View view) {
        this.view = view;
        this.model = new ProfileFragmentModel(null, null);
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {

        if (model == null)
            this.model = new ProfileFragmentModel(null, null);

        getProfileInformation();
        // getProfilePicture();
        //getEvents();
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

    private void getProfileInformation() {
        if (model.getUser() == null) {
            databaseUtils.getProfileInformation();
        } else {
            String username = model.getUser().getFirstName() + " " + model.getUser().getLastName();
            String age = CalendarUtils.getAgeFromBirthdate(model.getUser().getBirthDate()) + "";
            view.onProfileInformationSucceeded(username, model.getUser().geteMail(), model.getUser().getCity(), age, model.getUser().getPhone(), model.getUri());
        }
    }

    @Override
    public void onProfileInformationSucceeded(User user, Uri uri) {

        model.setUser(user);
        model.setUri(uri);
        String username = user.getFirstName() + " " + user.getLastName();
        String age = CalendarUtils.getAgeFromBirthdate(user.getBirthDate()) + "";
        view.onProfileInformationSucceeded(username, user.geteMail(), user.getCity(), age, user.getPhone(), uri);
    }

    @Override
    public void onProfileInformationFailed(VolteemCommonException exception) {
        view.onProfileInformationFailed(exception);
    }

    public interface View {
        void onProfileInformationSucceeded(String username, String email, String address, String age, String phone, Uri uri);

        void onProfileInformationFailed(VolteemCommonException exception);
    }
}
