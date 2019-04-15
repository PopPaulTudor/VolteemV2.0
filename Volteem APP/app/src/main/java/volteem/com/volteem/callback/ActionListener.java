package volteem.com.volteem.callback;

import android.net.Uri;

import java.io.Serializable;

import volteem.com.volteem.model.entity.Event;

public interface ActionListener {
    interface NewsDeletedListener {
        void onNewsDeleted();
    }

    interface EventAdapterListener {
        void onPicturesLoaded();

        void onClickEvent(Event event, Uri uri);
    }

    interface EventsActionListener extends Serializable {
        void onEventActivityDetached(boolean hasActionHappened);
    }
}
