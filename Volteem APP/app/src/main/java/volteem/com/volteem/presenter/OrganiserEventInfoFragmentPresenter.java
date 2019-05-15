package volteem.com.volteem.presenter;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.OrganiserEventInfoFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemConstants;

public class OrganiserEventInfoFragmentPresenter implements Presenter, DatabaseUtils.EventInfoCallback {

    private View view;
    private Bundle bundle;
    private OrganiserEventInfoFragmentModel model;
    private DatabaseUtils databaseUtils;

    public OrganiserEventInfoFragmentPresenter(Bundle bundle, View view) {
        this.view = view;
        this.bundle = bundle;
        this.model = new OrganiserEventInfoFragmentModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT));
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            model = new OrganiserEventInfoFragmentModel((Event) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_EVENT));
        }
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(this);
        }
        if (model.getEvent() != null) {
            view.loadUI(model.getEvent(), model.getEventImage());
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

    public void onSaveButtonPressed(String name, String location, Event.Type type, String description, int size, long startDate, long finishDate,
                                    long deadline) {
        Event currentEvent = model.getEvent();
        ///Don't bother reading the following condition in the if statement, it translates to "if at least one of the parameters is changed"
        if (!name.equals(currentEvent.getName()) || (!location.equals(currentEvent.getLocation())) || (type != currentEvent.getType()) ||
                (!description.equals(currentEvent.getDescription())) || (size != currentEvent.getSize()) ||
                (startDate != currentEvent.getStartDate()) || (finishDate != currentEvent.getFinishDate())
                || (deadline != currentEvent.getDeadline())) {
            VolteemCommonException exception = validateForm(name, location, description, size);
            if (exception != null) {
                view.onEditEventFailed(exception);
                return;
            }
            currentEvent = new Event(model.getEvent().getCreatedBy(), name, location, description, model.getEvent().getEventID(),
                    model.getEvent().getImageUri(), startDate, finishDate, deadline, model.getEvent().getTimestamp(), type, size, null);
            currentEvent.setRegisteredVolunteers(model.getEvent().getRegisteredVolunteers());
            currentEvent.setAcceptedVolunteers(model.getEvent().getAcceptedVolunteers());
            databaseUtils.updateEvent(currentEvent);
        } else {
            view.onEditEventSuccessful();
        }
    }

    private VolteemCommonException validateForm(String name, String location, String description, int size) {
        if (TextUtils.isEmpty(name)) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_NAME,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (TextUtils.isEmpty(location)) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_LOCATION,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (TextUtils.isEmpty(description)) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_DESCRIPTION,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (size == -1) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_SIZE,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        return null;
    }

    @Override
    public void onEditEventSuccessful(Event updatedEvent) {
        model.setEvent(updatedEvent);
        view.onEditEventSuccessful();
    }

    @Override
    public void onEditEventFailed(VolteemCommonException exception) {
        view.onEditEventFailed(exception);
    }

    @Override
    public void onDeleteEventSuccessful() {
        view.onDeleteEventSuccessful();
    }

    @Override
    public void onDeleteEventFailed(VolteemCommonException exception) {
        view.onDeleteEventFailed(exception);
    }

    public void onCancelButtonPressed() {
        view.loadUI(model.getEvent(), model.getEventImage());
    }

    public void onDeleteItemPressed() {
        databaseUtils.deleteEvent(model.getEvent());
    }

    public interface View {
        void loadUI(Event event, Uri eventImage);

        void onEditEventSuccessful();

        void onEditEventFailed(VolteemCommonException exception);

        void onDeleteEventSuccessful();

        void onDeleteEventFailed(VolteemCommonException exception);
    }
}
