package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import volteem.com.volteem.model.entity.Event;

public class EventActivityModel extends ViewModel {
    private Event event;
    private Uri imageUri;

    public EventActivityModel(Event event) {
        this.event = event;
        this.imageUri = Uri.parse(event.getImageUri());
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
}
