package volteem.com.volteem.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.NewsAdapter;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.NewsFragmentPresenter;

public class NewsFragment extends Fragment implements NewsFragmentPresenter.View, ActionListener.NewsDeletedListener {

    private NewsFragmentPresenter presenter;
    private RecyclerView newsRecView;
    private TextView noNewsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        presenter = new NewsFragmentPresenter(this);
        noNewsTextView = view.findViewById(R.id.no_news_text);
        newsRecView = view.findViewById(R.id.newsRecView);
        newsRecView.setHasFixedSize(true);
        presenter.onCreate();
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
    public void onDataRetrieved(ArrayList<NewsMessage> newsList) {
        if (!newsList.isEmpty()) {
            noNewsTextView.setVisibility(View.GONE);
            NewsAdapter adapter = new NewsAdapter(newsList, NewsFragment.this);
            ItemTouchHelper itemTouchHelper = adapter.getItemTouchHelper();
            newsRecView.setAdapter(adapter);
            itemTouchHelper.attachToRecyclerView(newsRecView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            newsRecView.setLayoutManager(linearLayoutManager);
        } else {
            noNewsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataRetrieveFailed(VolteemCommonException volteemCommonException) {
        Toast.makeText(getActivity(), volteemCommonException.getCause(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewsDeleted() {
        noNewsTextView.setVisibility(View.VISIBLE);
    }
}
