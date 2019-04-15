package volteem.com.volteem.model.view.model;

import android.arch.lifecycle.ViewModel;

public class SettingsFragmentModel extends ViewModel {

    private boolean notificationsState;

    public SettingsFragmentModel() {

    }

    public boolean isNotificationsState() {
        return notificationsState;
    }

    public void setNotificationsState(boolean notificationsState) {
        this.notificationsState = notificationsState;
    }
}
