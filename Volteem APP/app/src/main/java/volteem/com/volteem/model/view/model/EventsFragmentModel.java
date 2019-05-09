package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;

public class EventsFragmentModel extends ViewModel {
    private ArrayList<Event> eventsList;
    private SelectedEventsCategory selectedEventsCategory;
    private Event.Type selectedEventsType;

    public EventsFragmentModel(ArrayList<Event> eventsList) {
        this.eventsList = eventsList;
        this.selectedEventsCategory = SelectedEventsCategory.UNREGISTERED_EVENTS;
        this.selectedEventsType = Event.Type.NIL;
    }

    public ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public SelectedEventsCategory getSelectedEventsCategory() {
        return selectedEventsCategory;
    }

    public void setSelectedEventsCategory(SelectedEventsCategory selectedEventsCategory) {
        this.selectedEventsCategory = selectedEventsCategory;
    }

    public Event.Type getSelectedEventsType() {
        return selectedEventsType;
    }

    public void setSelectedEventsType(Event.Type selectedEventsType) {
        this.selectedEventsType = selectedEventsType;
    }
}
