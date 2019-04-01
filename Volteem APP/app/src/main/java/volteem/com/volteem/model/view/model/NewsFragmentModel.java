package volteem.com.volteem.model.view.model;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.NewsMessage;

public class NewsFragmentModel {
    private ArrayList<NewsMessage> newsList;

    public NewsFragmentModel(ArrayList<NewsMessage> newsList) {
        this.newsList = newsList;
    }

    public ArrayList<NewsMessage> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<NewsMessage> newsList) {
        this.newsList = newsList;
    }
}
