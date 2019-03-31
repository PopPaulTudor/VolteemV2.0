package volteem.com.volteem.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import volteem.com.volteem.R;
import volteem.com.volteem.presenter.SettingsFragmentPresenter;

public class SettingsFragment extends Fragment implements SettingsFragmentPresenter.View {

    private SettingsFragmentPresenter presenter;
    private SwitchCompat notificationSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        presenter = new SettingsFragmentPresenter(this);
        notificationSwitch = view.findViewById(R.id.switch_notifications);
        presenter.onCreate();
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final boolean switchState = notificationSwitch.isChecked();
                if (!switchState) {
                    AlertDialog muteNotificationsAlert = new AlertDialog.Builder(getActivity())
                            .setTitle("Mute notifications")
                            .setMessage("Are you sure you want to mute all notifications?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    presenter.changeSwitchState(switchState);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notificationSwitch.setChecked(true);
                                }
                            })
                            .create();
                    muteNotificationsAlert.show();
                } else {
                    presenter.changeSwitchState(switchState);
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean isViewActive() {
        return (isAdded() && !isDetached() && !isRemoving());
    }

    @Override
    public void updateNotificationsSwitchState(boolean notificationsState) {
        notificationSwitch.setChecked(notificationsState);
    }
}
