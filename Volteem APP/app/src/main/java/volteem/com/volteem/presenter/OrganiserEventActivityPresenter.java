package volteem.com.volteem.presenter;

import android.net.Uri;
import android.os.Bundle;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.view.model.OrganiserEventActivityModel;
import volteem.com.volteem.util.VolteemConstants;

public class OrganiserEventActivityPresenter implements Presenter {

    private View view;
    private OrganiserEventActivityModel model;
    private Bundle bundle;

    public OrganiserEventActivityPresenter(Bundle bundle, View view) {
        this.bundle = bundle;
        this.view = view;
        this.model = new OrganiserEventActivityModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT));
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new OrganiserEventActivityModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT));
        }
        if (model.getEvent() != null) {
            view.loadUI(model.getEvent(), model.getEventImage());
        } else {
            ///TODO: retrieve the event (probably came from a notification)
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

    public interface View {
        void loadUI(Event event, Uri eventImage);
    }
}
