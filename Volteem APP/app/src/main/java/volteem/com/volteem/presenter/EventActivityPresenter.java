package volteem.com.volteem.presenter;

import android.net.Uri;
import android.os.Bundle;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.view.model.EventActivityModel;
import volteem.com.volteem.util.VolteemConstants;

public class EventActivityPresenter implements Presenter {
    private boolean hasActionHappened;
    private View view;
    private EventActivityModel model;
    private Bundle bundleExtras;

    public EventActivityPresenter(View view, Bundle bundleExtras) {
        this.view = view;
        this.bundleExtras = bundleExtras;
        this.model = new EventActivityModel((Event) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                (Uri) bundleExtras.getParcelable(VolteemConstants.INTENT_EXTRA_IMAGE_URI));
        this.hasActionHappened = false;
    }

    @Override
    public void onCreate() {
        if (model == null) {
            model = new EventActivityModel((Event) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT),
                    (Uri) bundleExtras.getParcelable(VolteemConstants.INTENT_EXTRA_IMAGE_URI));
        } else {
            if (model.getEvent() != null) {
                view.loadUI(model.getEvent(), model.getImageUri());
            }
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
        hasActionHappened = true;
    }

    public interface View {
        void loadUI(Event event, Uri uri);
    }
}
