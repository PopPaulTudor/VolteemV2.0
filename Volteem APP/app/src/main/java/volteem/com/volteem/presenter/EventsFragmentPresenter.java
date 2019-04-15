package volteem.com.volteem.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import volteem.com.volteem.model.entity.Event;
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
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(this);
        }
        if (model == null) {
            this.model = new EventsFragmentModel(null);
        }
        if (model.getEventsList() == null) {
            Log.e("events", "not in memory yet");
            getEventsList();
        } else {
            Log.e("events", "already in memory");
            view.onEventsLoadSuccessful(model.getEventsList());
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

    public void getEventsList() {
        if (VolteemUtils.isNetworkAvailable()) {
            databaseUtils.getEventsList();

        } else {
            view.onEventsLoadFailed(new VolteemCommonException("network_issue", "No internet connection."));
        }
    }

    @Override
    public void onEventsLoadSuccessful(ArrayList<Event> eventsList) {
        Collections.sort(eventsList, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                if (event.getDeadline() < t1.getDeadline())
                    return -1;
                if (event.getDeadline() > t1.getDeadline())
                    return 1;
                return 0;
            }
        });
        model.setEventsList(eventsList);
        if (view.isViewActive()) {
            view.onEventsLoadSuccessful(eventsList);
        }
    }

    @Override
    public void onEventsLoadFailed(VolteemCommonException exception) {
        view.onEventsLoadFailed(exception);
    }

    public interface View {
        boolean isViewActive();

        void onEventsLoadSuccessful(ArrayList<Event> eventsList);

        void onEventsLoadFailed(VolteemCommonException exception);
    }
}
