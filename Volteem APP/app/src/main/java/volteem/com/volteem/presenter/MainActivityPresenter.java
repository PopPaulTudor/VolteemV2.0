package volteem.com.volteem.presenter;


import volteem.com.volteem.model.view.model.MainActivityModel;

public class MainActivityPresenter implements Presenter, MainActivityModel.ModelCallback {

    private View view;
    private MainActivityModel model;


    public MainActivityPresenter(View view) {
        this.view=view;
        this.model=new MainActivityModel(this);
    }


    @Override
    public void onCreate() {

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
        view.logOut();
    }


    public interface View {

        void logOut();
    }
}
