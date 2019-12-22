package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;

public class EventActivityModel extends ViewModel {
    private Event event;
    private Uri imageUri;
    private SelectedEventsCategory flag;
    private boolean isUserAccepted;

    public EventActivityModel(Event event, SelectedEventsCategory flag, boolean isUserAccepted) {
        this.event = event;
        this.imageUri = Uri.parse(event.getImageUri());
        this.flag = flag;
        this.isUserAccepted = isUserAccepted;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public SelectedEventsCategory getFlag() {
        return flag;
    }

    public void setFlag(SelectedEventsCategory flag) {
        this.flag = flag;
    }

    public boolean isUserAccepted() {
        return isUserAccepted;
    }

    public void setUserAccepted(boolean userAccepted) {
        isUserAccepted = userAccepted;
    }
}
