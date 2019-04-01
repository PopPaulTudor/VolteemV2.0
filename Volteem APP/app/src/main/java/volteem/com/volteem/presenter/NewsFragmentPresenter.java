package volteem.com.volteem.presenter;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.NewsFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;

public class NewsFragmentPresenter implements Presenter, DatabaseUtils.NewsCallback {

    private View view;
    private NewsFragmentModel model;
    private DatabaseUtils databaseUtils;

    public NewsFragmentPresenter(View view) {
        this.view = view;
        this.model = new NewsFragmentModel(null);
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override

    public void onCreate() {
        /*This method is called when the view is created, but also when it is rotated. When it rotates,
          we don't want the data to be re-retrieved, so we verify whether or not the model has already
          been instantiated, and whether or not the list of news in the model is null. For the View's
          lifecycle compared to the Model's lifecycle, check out this photo: https://bit.ly/2TOflUh
        */
        if (model == null) {
            this.model = new NewsFragmentModel(null);
        }
        getNewsListData(); ///We call the method for the data retrieval
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

    private void getNewsListData() {
        ///If the model's list is null, we need to load the data from the database; if it is not null, we can simply return it as it is
        if (model.getNewsList() == null) {
            databaseUtils.retrieveNewsList();
        } else {
            view.onDataRetrieved(model.getNewsList());
        }
    }

    @Override
    public void onDataRetrieved(ArrayList<NewsMessage> newsList) {
        ///The data retrieval was successful, we set the Model's list and call the view method
        model.setNewsList(newsList);
        if (view.isViewActive()) {
            view.onDataRetrieved(newsList);
        }
    }

    @Override
    public void onDataRetrieveFailed(VolteemCommonException volteemCommonException) {
        if (view.isViewActive()) {
            view.onDataRetrieveFailed(volteemCommonException);
        }
    }

    public interface View {
        boolean isViewActive();

        void onDataRetrieved(ArrayList<NewsMessage> newsList);

        void onDataRetrieveFailed(VolteemCommonException volteemCommonException);
    }
}
