package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.User;

public class OrganiserEventRegisteredUsersFragmentModel extends ViewModel {
    private Event event;
    private ArrayList<User> registeredUsers;

    public OrganiserEventRegisteredUsersFragmentModel(Event event, ArrayList<User> registeredUsers) {
        this.event = event;
        this.registeredUsers = registeredUsers;
    }

    public ArrayList<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(ArrayList<User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
