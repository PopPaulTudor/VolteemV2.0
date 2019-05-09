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
                (SelectedEventsCategory) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_FLAG));
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            model = new EventActivityModel((Event) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                    (SelectedEventsCategory) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_FLAG));
        } else {
            if (model.getEvent() != null) {
                view.loadUI(model.getEvent(), model.getImageUri());
            }
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

    public void onSignUpForEventButtonPressed() {
        databaseUtils.registerToEvent(model.getEvent().getEventID(), model.getEvent().getCreatedBy(), model.getEvent().getName());
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

    public interface View {
        void loadUI(Event event, Uri uri);

        void onRegisterToEventSuccessful();

        void onRegisterToEventFailed(VolteemCommonException exception);
    }
}
