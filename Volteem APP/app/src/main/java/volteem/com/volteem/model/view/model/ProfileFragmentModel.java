package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;

import volteem.com.volteem.model.entity.User;

public class ProfileFragmentModel extends ViewModel {

    private User user;

    public ProfileFragmentModel(User user) {
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
