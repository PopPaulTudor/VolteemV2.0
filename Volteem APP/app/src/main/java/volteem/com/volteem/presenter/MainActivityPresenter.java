package volteem.com.volteem.presenter;


import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.MainActivityModel;
import volteem.com.volteem.util.DatabaseUtils;

public class MainActivityPresenter implements Presenter {

    private View view;
    private MainActivityModel model;

    public MainActivityPresenter(View view) {
        this.view = view;
        this.model = new MainActivityModel();
    }

    @Override
    public void onCreate() {

        if (model == null) {
            this.model = new MainActivityModel();
        }
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

    public void logOut() {
        DatabaseUtils.signOut();
        if (!DatabaseUtils.isUserLoggedIn())
            view.onLogOutSuccessful();
        else
            view.onLogOutInformationFailed(new VolteemCommonException("Log Out", "Log out failed"));
    }

    public interface View {

        void onLogOutSuccessful();

        void onLogOutInformationFailed(VolteemCommonException volteemCommonException);
    }
}
