package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.Feedback;
import volteem.com.volteem.model.entity.User;

public class ProfileFragmentModel extends ViewModel {

    private User user;
    private Uri uri;
    private ArrayList<Event> events;
    private ArrayList<Feedback> feedbacks;

    public ProfileFragmentModel(User user, Uri uri,ArrayList<Event> events,ArrayList<Feedback> feedbacks)
    {
        this.user = user;
        this.uri = uri;
        this.events=events;
        this.feedbacks=feedbacks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(ArrayList<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
