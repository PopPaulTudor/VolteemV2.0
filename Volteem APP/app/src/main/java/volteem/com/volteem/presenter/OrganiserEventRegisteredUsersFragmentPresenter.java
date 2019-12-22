package volteem.com.volteem.presenter;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.User;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.OrganiserEventRegisteredUsersFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemConstants;

public class OrganiserEventRegisteredUsersFragmentPresenter implements Presenter, DatabaseUtils.EventUsersCallback {
    private View view;
    private Bundle bundle;
    private OrganiserEventRegisteredUsersFragmentModel model;
    private DatabaseUtils databaseUtils;

    public OrganiserEventRegisteredUsersFragmentPresenter(Bundle bundle, View view) {
        this.bundle = bundle;
        this.view = view;
        this.model = new OrganiserEventRegisteredUsersFragmentModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                null);
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new OrganiserEventRegisteredUsersFragmentModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                    null);
        }
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(this);
        }
        if (model.getEvent().getRegisteredVolunteers().isEmpty()) {
            view.onRetrieveUsersListSuccessful(new ArrayList<User>());
            return;
        }
        if (model.getRegisteredUsers() == null) {
            databaseUtils.getEventUsersList(model.getEvent().getRegisteredVolunteers());
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

    @Override
    public void onRetrieveUsersListSuccessful(ArrayList<User> registeredUsers) {
        Collections.sort(registeredUsers, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                return Integer.compare(t1.getExperience(), user.getExperience());
            }
        });
        model.setRegisteredUsers(registeredUsers);
        view.onRetrieveUsersListSuccessful(registeredUsers);
    }

    @Override
    public void onRetrieveUsersListFailed(VolteemCommonException exception) {
        view.onRetrieveUsersListFailed(exception);
    }

    public Event getEvent() {
        return model.getEvent();
    }

    public interface View {
        void onRetrieveUsersListSuccessful(ArrayList<User> registeredUsers);

        void onRetrieveUsersListFailed(VolteemCommonException exception);
    }
}
