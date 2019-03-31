package volteem.com.volteem.presenter;

import java.util.ArrayList;

import volteem.com.volteem.adapter.NewsAdapter;
import volteem.com.volteem.model.entity.DataRetrieveException;
import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.view.model.NewsFragmentModel;

public class NewsFragmentPresenter implements Presenter, NewsFragmentModel.ModelCallback {

    private View view;
    private NewsFragmentModel model;

    public NewsFragmentPresenter(View view) {
        this.view = view;
        this.model = new NewsFragmentModel(this);
    }

    @Override

    public void onCreate() {
        this.model = new NewsFragmentModel(this);
        model.retrieveNewsList();
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
    public void onDataRetrieved(ArrayList<NewsMessage> newsList) {
        if (view.isViewActive()) {
            view.onDataRetrieved(newsList);
        }
    }

    @Override
    public void onDataRetrieveFailed(DataRetrieveException dataRetrieveException) {
        if (view.isViewActive()) {
            view.onDataRetrieveFailed(dataRetrieveException);
        }
    }

    public interface View {
        boolean isViewActive();

        void onDataRetrieved(ArrayList<NewsMessage> newsList);

        void onDataRetrieveFailed(DataRetrieveException dataRetrieveException);
    }
}
