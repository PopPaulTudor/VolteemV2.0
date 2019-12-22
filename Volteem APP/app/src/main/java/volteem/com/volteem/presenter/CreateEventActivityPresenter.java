package volteem.com.volteem.presenter;

import android.net.Uri;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemConstants;

public class CreateEventActivityPresenter implements Presenter, DatabaseUtils.CreateEventCallback {

    private View view;
    private DatabaseUtils databaseUtils;

    public CreateEventActivityPresenter(View view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        if (databaseUtils == null)
            databaseUtils = new DatabaseUtils(this);
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

    public void createEvent(String eventName, String location, long startDate, long finishDate, Event.Type type,
                            String description, long deadline, int volunteersNeeded, Uri mUriPicture) {
        VolteemCommonException exception = validateForm(eventName, location, startDate, finishDate, type, description,
                deadline, volunteersNeeded);
        if (exception != null) {
            view.onCreateEventFailed(exception);
            return;
        }
        databaseUtils.createEvent(eventName, location, startDate, finishDate, type, description, deadline, volunteersNeeded, mUriPicture);
    }

    private VolteemCommonException validateForm(String eventName, String location, long startDate, long finishDate,
                                                Event.Type type, String description, long deadline, int volunteersNeeded) {
        if (eventName.isEmpty()) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_NAME,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (location.isEmpty()) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_LOCATION,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (startDate == -1) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_START_DATE,
                    VolteemConstants.EXCEPTION_MESSAGE_START_DATE_EMPTY);
        }
        if (finishDate == -1) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_FINISH_DATE,
                    VolteemConstants.EXCEPTION_MESSAGE_FINISH_DATE_EMPTY);
        }
        if (finishDate < startDate) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_FINISH_DATE,
                    VolteemConstants.EXCEPTION_MESSAGE_FINISH_DATE_BEFORE_START_DATE);
        }
        if (finishDate < deadline) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_FINISH_DATE,
                    VolteemConstants.EXCEPTION_MESSAGE_FINISH_DATE_BEFORE_DEADLINE);
        }
        if (deadline == -1) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_DEADLINE,
                    VolteemConstants.EXCEPTION_MESSAGE_DEADLINE_EMPTY);
        }
        if (deadline > startDate) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_DEADLINE,
                    VolteemConstants.EXCEPTION_MESSAGE_DEADLINE_AFTER_START_DATE);
        }
        if (type == Event.Type.NIL) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_TYPE,
                    VolteemConstants.EXCEPTION_MESSAGE_TYPE_EMPTY);
        }
        if (description.isEmpty()) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_DESCRIPTION,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        if (volunteersNeeded == 0) {
            return new VolteemCommonException(VolteemConstants.EXCEPTION_EVENT_SIZE,
                    VolteemConstants.EXCEPTION_MESSAGE_EMPTY);
        }
        return null;
    }

    @Override
    public void onCreateEventSuccessful() {
        view.onCreateEventSuccessful();
    }

    @Override
    public void onCreateEventFailed(VolteemCommonException exception) {
        view.onCreateEventFailed(exception);
    }

    public interface View {
        void onCreateEventSuccessful();

        void onCreateEventFailed(VolteemCommonException exception);
    }
}
