package volteem.com.volteem.callback;

import android.net.Uri;

import volteem.com.volteem.model.entity.Event;

public interface ActionListener {
    interface NewsDeletedListener {
        void onNewsDeleted();
    }

    interface EventAdapterListener {
        void onPicturesLoaded();

        void onClickEvent(Event event, Uri uri);
    }

    interface EventsActionListener {
        void onEventActivityDetached(boolean hasActionHappened);
    }
}
