package volteem.com.volteem.presenter;


import android.net.Uri;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.Feedback;
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
        this.model = new ProfileFragmentModel(null, null, null, null);
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null)
            this.model = new ProfileFragmentModel(null, null, null, null);

        getProfileInformation();
        getProfilePicture();
        getEvents();
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

    public void changePhoto(Uri uri) {
        databaseUtils.changePhoto(uri);
    }

    private void getProfileInformation() {
        if (model.getUser() == null) {
            databaseUtils.getProfileInformation();
        } else {
            String username = model.getUser().getFirstName() + " " + model.getUser().getLastName();
            String age = CalendarUtils.getAgeFromBirthdate(model.getUser().getBirthDate()) + "";
            view.onProfileInformationSucceeded(username, model.getUser().geteMail(), model.getUser().getCity(), age, model.getUser().getPhone());
        }
    }

    private void getProfilePicture() {
        if (model.getUri() == null)
            databaseUtils.getProfilePicture();
        else
            view.onProfilePictureSucceeded(model.getUri());
    }

    private void getEvents() {
        if (model.getEvents() == null)
            databaseUtils.getEvents();
        else
            view.onEventsSucceeded(model.getEvents(), model.getFeedback());
    }

    public void changeData(String username, String phone, String address, long age) {
        String[] nameAndSurname = username.split(" ", 2);

        if (phone.isEmpty() || phone.equals(model.getUser().getPhone())) phone = null;

        if (address.isEmpty() || address.equals(model.getUser().getCity())) address = null;

        if (nameAndSurname[0].isEmpty() || nameAndSurname[0].equals(model.getUser().getFirstName()))
            nameAndSurname[0] = null;

        if (nameAndSurname[1].isEmpty() || nameAndSurname[1].equals(model.getUser().getLastName()))
            nameAndSurname[1] = null;

        if (age <= 0 || age == model.getUser().getBirthDate()) age = 0;

        databaseUtils.changeData(nameAndSurname[0], nameAndSurname[1], phone, address, age);
    }

    @Override
    public void onProfileInformationSucceeded(User user) {
        model.setUser(user);
        String username = user.getFirstName() + " " + user.getLastName();
        String age = CalendarUtils.getAgeFromBirthdate(user.getBirthDate()) + "";
        view.onProfileInformationSucceeded(username, user.geteMail(), user.getCity(), age, user.getPhone());
    }

    @Override
    public void onProfileInformationFailed(VolteemCommonException volteemCommonException) {
        view.onProfileInformationFailed(volteemCommonException);
    }

    @Override
    public void onProfilePictureSucceeded(Uri uri) {
        model.setUri(uri);
        view.onProfilePictureSucceeded(uri);
    }

    @Override
    public void onProfilePictureFailed(VolteemCommonException volteemCommonException) {
        view.onProfilePictureFailed(volteemCommonException);
    }

    @Override
    public void onEventsSucceeded(ArrayList<Event> events, ArrayList<Feedback> feedback) {
        model.setEvents(events);
        model.setFeedback(feedback);
        view.onEventsSucceeded(events, feedback);
    }

    @Override
    public void onEventsFailed(VolteemCommonException volteemCommonException) {
        view.onEventsFailed(volteemCommonException);
    }

    @Override
    public void onProfilePhotoChangedSucceeded(String message) {
        view.onProfilePhotoChangedSucceeded(message);

    }

    @Override
    public void onProfileDataChangedSucceeded(String message, String firstName, String secondName, String phone, String address, long birthdate) {

        if (message.contains("phone"))
            model.getUser().setPhone(phone);

        if (message.contains("birthdate"))
            model.getUser().setBirthDate(birthdate);

        if (message.contains("firstName"))
            model.getUser().setFirstName(firstName);

        if (message.contains("secondName"))
            model.getUser().setLastName(secondName);

        if (message.contains("address"))
            model.getUser().setCity(address);

        view.onDataChangedSucceeded(message);
    }

    @Override
    public void onProfileDataChangedFailed() {
        view.onDataChangedFailed();
    }

    public interface View {
        void onProfileInformationSucceeded(String username, String email, String address, String age, String phone);

        void onProfileInformationFailed(VolteemCommonException exception);

        void onProfilePictureSucceeded(Uri uri);

        void onProfilePictureFailed(VolteemCommonException volteemCommonException);

        void onEventsSucceeded(ArrayList<Event> events, ArrayList<Feedback> feedbacks);

        void onEventsFailed(VolteemCommonException volteemCommonException);

        void onProfilePhotoChangedSucceeded(String message);

        void onDataChangedSucceeded(String message);

        void onDataChangedFailed();
    }
}
