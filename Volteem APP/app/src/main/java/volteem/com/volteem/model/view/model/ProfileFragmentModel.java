package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import volteem.com.volteem.model.entity.User;

public class ProfileFragmentModel extends ViewModel {

    private User user;
    private Uri uri;

    public ProfileFragmentModel(User user, Uri uri) {
        this.user = user;
        this.uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
