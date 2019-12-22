package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import volteem.com.volteem.model.entity.Event;

public class OrganiserEventActivityModel extends ViewModel {
    private Event event;
    private Uri eventImage;

    public OrganiserEventActivityModel(Event event) {
        this.event = event;
        this.eventImage = Uri.parse(event.getImageUri());
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Uri getEventImage() {
        return eventImage;
    }

    public void setEventImage(Uri eventImage) {
        this.eventImage = eventImage;
    }
}
