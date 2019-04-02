package volteem.com.volteem.presenter;


import com.google.firebase.auth.FirebaseAuth;

import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.MainActivityModel;
import volteem.com.volteem.util.DatabaseUtils;

public class MainActivityPresenter implements Presenter, DatabaseUtils.MainCallBack {

    private View view;
    private MainActivityModel model;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseUtils databaseUtils;



    public MainActivityPresenter(View view) {
        this.view = view;
        this.model = new MainActivityModel();
        this.databaseUtils=new DatabaseUtils(this);
    }

    @Override
    public void onCreate() {

        if(model==null) {
            this.model = new MainActivityModel();
        }

        if(databaseUtils==null)
            this.databaseUtils= new DatabaseUtils(this);

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
        databaseUtils.signOut();
        if (!databaseUtils.isUserLoggedIn())
            view.onLogOutSuccessful();
        else view.onLogOutInformationFailed(new VolteemCommonException("Log Out","Log out failed"));
    }

    @Override
    public void onSignOutSucceeded() {

    }

    @Override
    public void onSignOutInformationFailed(VolteemCommonException volteemCommonException) {

    }

    public interface View {

        void onLogOutSuccessful();
        void onLogOutInformationFailed(VolteemCommonException volteemCommonException);


    }
}
