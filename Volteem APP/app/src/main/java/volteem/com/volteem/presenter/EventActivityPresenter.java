package volteem.com.volteem.presenter;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.view.model.EventActivityModel;

public class EventActivityPresenter implements Presenter {
    private boolean hasActionHappened;
    private View view;
    private EventActivityModel model;
    private ActionListener.EventsActionListener eventsActionListener;
    private Bundle bundleExtras;

    public EventActivityPresenter(View view, Bundle bundleExtras) {
        this.view = view;
        this.bundleExtras = bundleExtras;
        this.model = new EventActivityModel((Event) bundleExtras.getSerializable("volteem_event"),
                (Uri) bundleExtras.getParcelable("volteem_image"));
        this.eventsActionListener = (ActionListener.EventsActionListener) bundleExtras.getSerializable("volteem_interface");
        this.hasActionHappened = false;
    }

    @Override
    public void onCreate() {
        if (model == null) {
            model = new EventActivityModel((Event) bundleExtras.getSerializable("volteem_event"),
                    (Uri) bundleExtras.getParcelable("volteem_image"));
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
        if (eventsActionListener == null)
            Log.e("interface", "null");
        else {
            Log.e("interface", "not null");
            eventsActionListener.onEventActivityDetached(hasActionHappened);
        }
    }

    public void onSignUpForEventButtonPressed() {
        hasActionHappened = true;
    }

    public interface View {
        void loadUI(Event event, Uri uri);
    }
}
