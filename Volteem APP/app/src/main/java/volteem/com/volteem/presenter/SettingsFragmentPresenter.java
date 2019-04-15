package volteem.com.volteem.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import volteem.com.volteem.model.view.model.SettingsFragmentModel;
import volteem.com.volteem.util.VolteemUtils;

public class SettingsFragmentPresenter implements Presenter {
    private View view;
    private SettingsFragmentModel model;
    private SharedPreferences preferences;

    public SettingsFragmentPresenter(View view) {
        this.view = view;
        this.model = new SettingsFragmentModel();
        this.preferences = VolteemUtils.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new SettingsFragmentModel();
        }
        model.setNotificationsState(preferences.getBoolean("notificationsSwitchState", true));
        if (view.isViewActive())
            view.updateNotificationsSwitchState(model.isNotificationsState());
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

    public void changeSwitchState(boolean switchState) {
        boolean notificationsState = model.isNotificationsState();
        if (notificationsState != switchState) {
            preferences.edit().putBoolean("notificationsSwitchState", switchState).apply();
            model.setNotificationsState(switchState);
        }
    }

    public interface View {
        boolean isViewActive();

        void updateNotificationsSwitchState(boolean notificationsState);
    }
}
