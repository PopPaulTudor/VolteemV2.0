package volteem.com.volteem.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.EventsFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemUtils;

public class EventsFragmentPresenter implements Presenter, DatabaseUtils.EventsCallback {
    private View view;
    private EventsFragmentModel model;
    private DatabaseUtils databaseUtils;

    public EventsFragmentPresenter(View view) {
        this.view = view;
        this.databaseUtils = new DatabaseUtils(this);
        this.model = new EventsFragmentModel(null);
    }

    @Override
    public void onCreate() {
        ///Not retrieving the events list in this method, but in onResume; the onResume method is called even at the first bind of the Fragment
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(this);
        }
        if (model == null) {
            this.model = new EventsFragmentModel(null);
        }
        if (model.getEventsList() == null) {
            Log.d("events", "not in memory yet");
        } else {
            Log.d("events", "already in memory");
            view.onEventsLoadSuccessful(model.getEventsList());
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        Log.d("EventsFragmentPresenter", "resumed, retrieving events");
        getEventsList();
    }

    @Override
    public void onDestroy() {

    }

    public void getEventsList() {
        if (VolteemUtils.isNetworkAvailable()) {
            switch (model.getSelectedEventsCategory()) {
                case UNREGISTERED_EVENTS:
                    databaseUtils.getUnregisteredEventsList();
                    break;
                case REGISTERED_EVENTS:
                    databaseUtils.getRegisteredEventsList();
                    break;
                case OWN_EVENTS:
                    databaseUtils.getOwnEventsList();
                    break;
            }
        } else {
            view.onEventsLoadFailed(new VolteemCommonException("network_issue", "No internet connection."));
        }
    }

    @Override
    public void onEventsLoadSuccessful(ArrayList<Event> eventsList) {
        if (model.getSelectedEventsCategory() == SelectedEventsCategory.UNREGISTERED_EVENTS) {
            Collections.sort(eventsList, new Comparator<Event>() {
                @Override
                public int compare(Event event, Event t1) {
                    return Long.compare(event.getDeadline(), t1.getDeadline());
                }
            });
        } else {
            Collections.sort(eventsList, new Comparator<Event>() {
                @Override
                public int compare(Event event, Event t1) {
                    return Long.compare(event.getStartDate(), t1.getStartDate());
                }
            });
        }
        model.setEventsList(eventsList);
        returnToViewWithFilteredEvents(eventsList);
    }

    private void returnToViewWithFilteredEvents(ArrayList<Event> eventsList) {
        ArrayList<Event> finalEvents = new ArrayList<>();
        if (model.getSelectedEventsType() != Event.Type.NIL) {
            for (Event event : eventsList) {
                if (event.getType() == model.getSelectedEventsType()) {
                    finalEvents.add(event);
                }
            }
        } else {
            finalEvents = eventsList;
        }
        if (view.isViewActive()) {
            view.onEventsLoadSuccessful(finalEvents);
        }
    }

    @Override
    public void onEventsLoadFailed(VolteemCommonException exception) {
        view.onEventsLoadFailed(exception);
    }

    public SelectedEventsCategory getSelectedEventsCategory() {
        return model.getSelectedEventsCategory();
    }

    public Event.Type getSelectedEventsType() {
        return model.getSelectedEventsType();
    }

    public void onApplyFiltersButtonPressed(SelectedEventsCategory selectedEventsCategory, Event.Type selectedEventsType) {
        if (selectedEventsCategory == model.getSelectedEventsCategory() && selectedEventsType == model.getSelectedEventsType()) {
            return;
        }
        view.updateUIForEventsLoading();
        model.setSelectedEventsType(selectedEventsType);
        if (selectedEventsCategory != model.getSelectedEventsCategory()) {
            model.setSelectedEventsCategory(selectedEventsCategory);
            switch (selectedEventsCategory) {
                case UNREGISTERED_EVENTS:
                    databaseUtils.getUnregisteredEventsList();
                    break;
                case REGISTERED_EVENTS:
                    databaseUtils.getRegisteredEventsList();
                    break;
                case OWN_EVENTS:
                    databaseUtils.getOwnEventsList();
                    break;
            }
        } else {
            returnToViewWithFilteredEvents(model.getEventsList());
        }
    }

    public interface View {
        boolean isViewActive();

        void onEventsLoadSuccessful(ArrayList<Event> eventsList);

        void onEventsLoadFailed(VolteemCommonException exception);

        void updateUIForEventsLoading();
    }
}
