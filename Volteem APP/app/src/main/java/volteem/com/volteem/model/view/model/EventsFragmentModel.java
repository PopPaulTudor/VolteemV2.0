package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.Event;

public class EventsFragmentModel extends ViewModel {
    private ArrayList<Event> eventsList;

    public EventsFragmentModel(ArrayList<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<Event> eventsList) {
        this.eventsList = eventsList;
    }
}
