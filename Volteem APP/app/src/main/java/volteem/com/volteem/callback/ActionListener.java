package volteem.com.volteem.callback;

import volteem.com.volteem.model.entity.Event;

public interface ActionListener {
    interface NewsDeletedListener {
        void onNewsDeleted();
    }

    interface EventAdapterListener {
        void onPicturesLoaded();

        void onClickEvent(Event event, boolean isUserAccepted);
    }

    interface UsersRemovedListener {
        void onAllUsersRemoved();
    }
}
