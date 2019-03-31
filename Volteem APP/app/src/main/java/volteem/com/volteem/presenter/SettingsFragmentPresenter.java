package volteem.com.volteem.presenter;

import volteem.com.volteem.model.view.model.SettingsFragmentModel;

public class SettingsFragmentPresenter implements Presenter {
    private View view;
    private SettingsFragmentModel model;

    public SettingsFragmentPresenter(View view) {
        this.view = view;
        this.model = new SettingsFragmentModel();
    }

    @Override
    public void onCreate() {
        this.model = new SettingsFragmentModel();
        boolean notificationsState = model.getNotificationsState();
        if (view.isViewActive())
            view.updateNotificationsSwitchState(notificationsState);
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
        boolean notificationsState = model.getNotificationsState();
        if (notificationsState != switchState) {
            model.changeNotificationsState(switchState);
        }
    }

    public interface View {
        boolean isViewActive();

        void updateNotificationsSwitchState(boolean notificationsState);
    }
}
