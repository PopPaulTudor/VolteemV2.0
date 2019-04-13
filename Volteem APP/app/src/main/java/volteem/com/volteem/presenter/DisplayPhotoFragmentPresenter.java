package volteem.com.volteem.presenter;

import volteem.com.volteem.model.view.model.DisplayPhotoFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;

public class DisplayPhotoFragmentPresenter implements Presenter, DatabaseUtils.DisplayPhotoCallBack {
    private View view;
    private DisplayPhotoFragmentModel model;
    private DatabaseUtils databaseUtils;

    public DisplayPhotoFragmentPresenter(View view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        if (model == null) {
            this.model = new DisplayPhotoFragmentModel(null);
        }

        if (databaseUtils == null) {
            this.databaseUtils = new DatabaseUtils(this);
        }
        databaseUtils.getUserId();
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

    @Override
    public void onUserIdSucceeded(String userId) {
        model.setUserID(userId);
        view.getUserIdSuccessful(model.getUserID());
    }


    public interface View {
        void getUserIdSuccessful(String userID);

    }
}
