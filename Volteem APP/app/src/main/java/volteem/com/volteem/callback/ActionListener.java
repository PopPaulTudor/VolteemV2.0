package volteem.com.volteem.callback;

import android.net.Uri;

import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.NGO;

public interface ActionListener {
    interface NewsDeletedListener {
        void onNewsDeleted();
    }

    interface EventAdapterListener {
        void onPicturesLoaded();

        void onClickEvent(Event event);
    }

    interface NGOAdapterListener{
        void onPicturesLoaded();

        void onClickNGO(NGO ngo);

    }
}
