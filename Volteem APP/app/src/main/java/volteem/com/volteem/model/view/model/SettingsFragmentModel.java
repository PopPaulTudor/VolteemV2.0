package volteem.com.volteem.model.view.model;

import android.content.Context;
import android.content.SharedPreferences;

import volteem.com.volteem.util.VolteemApp;

public class SettingsFragmentModel {

    private Context appContext = VolteemApp.getContext();
    private SharedPreferences preferences;

    public SettingsFragmentModel() {
        this.preferences = appContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public boolean getNotificationsState() {
        return preferences.getBoolean("notificationsSwitchState", true);
    }

    public void changeNotificationsState(boolean notificationsState) {
        preferences.edit().putBoolean("notificationsSwitchState", notificationsState).apply();
    }
}
