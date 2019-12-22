package volteem.com.volteem.presenter;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.EventActivityModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemConstants;

public class EventActivityPresenter implements Presenter, DatabaseUtils.SingleEventCallback {

    private View view;
    private EventActivityModel model;
    private Bundle bundleExtras;
    private DatabaseUtils databaseUtils;

    public EventActivityPresenter(View view, @NonNull Bundle bundleExtras) {
        this.view = view;
        this.bundleExtras = bundleExtras;
        this.model = new EventActivityModel((Event) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                (SelectedEventsCategory) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_FLAG),
                bundleExtras.getBoolean(VolteemConstants.INTENT_EXTRA_STATUS));
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            model = new EventActivityModel((Event) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                    (SelectedEventsCategory) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_FLAG),
                    bundleExtras.getBoolean(VolteemConstants.INTENT_EXTRA_STATUS));
        }
        if (model.getEvent() != null) {
            view.loadUI(model.getEvent(), model.getImageUri());
        } else {
            ///TODO: retrieve the event if it is null (probably came from a notification)
        }
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(this);
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
        //TODO: decide whether ot not action happened
    }

    public void registerToEvent() {
        databaseUtils.registerToEvent(model.getEvent().getEventID(), model.getEvent().getCreatedBy(), model.getEvent().getName());
    }

    public void leaveEvent() {
        databaseUtils.leaveEvent(model.getEvent().getEventID(), model.getEvent().getCreatedBy(), model.getEvent().getName());
    }

    public SelectedEventsCategory getFlag() {
        return model.getFlag();
    }

    @Override
    public void onRegisterToEventSuccessful() {
        view.onRegisterToEventSuccessful();
    }

    @Override
    public void onRegisterToEventFailed(VolteemCommonException exception) {
        view.onRegisterToEventFailed(exception);
    }

    @Override
    public void onLeaveEventSuccessful() {
        view.onLeaveEventSuccessful();
    }

    @Override
    public void onLeaveEventFailed(VolteemCommonException exception) {
        view.onLeaveEventFailed(exception);
    }

    public boolean isUserAccepted() {
        return model.isUserAccepted();
    }

    public interface View {
        void loadUI(Event event, Uri uri);

        void onRegisterToEventSuccessful();

        void onRegisterToEventFailed(VolteemCommonException exception);

        void onLeaveEventSuccessful();

        void onLeaveEventFailed(VolteemCommonException exception);
    }
}
