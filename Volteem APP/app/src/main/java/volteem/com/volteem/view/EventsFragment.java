package volteem.com.volteem.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.EventsAdapter;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.EventsFragmentPresenter;
import volteem.com.volteem.util.VolteemConstants;

public class EventsFragment extends Fragment implements SwipeRefreshLayout
        .OnRefreshListener, ActionListener.EventAdapterListener, EventsFragmentPresenter.View {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView noEventsTextView;
    private int mMediumAnimTime;
    private EventsFragmentPresenter presenter;
    private FloatingActionButton addEventsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        presenter = new EventsFragmentPresenter(this);
        addEventsButton = view.findViewById(R.id.add_event);
        recyclerView = view.findViewById(R.id.RecViewVolEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addEventsButton.isShown()) {
                    addEventsButton.hide();
                } else {
                    if (dy < 0 && !addEventsButton.isShown()) {
                        addEventsButton.show();
                    }
                }
            }
        });
        noEventsTextView = view.findViewById(R.id.no_events_text);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mMediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mSwipeRefreshLayout.setRefreshing(true);
        noEventsTextView.setVisibility(View.GONE);
        addEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });
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
    public void onRefresh() {
        loadEvents();
    }

    @Override
    public void onPicturesLoaded() {
        recyclerView.setAlpha(0f);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.animate()
                .alpha(1f)
                .setDuration(mMediumAnimTime)
                .setListener(null);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClickEvent(Event event) {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VolteemConstants.INTENT_EXTRA_EVENT, event);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadEvents() {
        mSwipeRefreshLayout.setRefreshing(true);
        noEventsTextView.setVisibility(View.GONE);
        presenter.getEventsList();
    }

    @Override
    public void onEventsLoadFailed(VolteemCommonException exception) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isViewActive() {
        return (isAdded() && !isDetached() && !isRemoving());
    }

    @Override
    public void onEventsLoadSuccessful(ArrayList<Event> eventsList) {
        if (eventsList.isEmpty()) {
            noEventsTextView.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            EventsAdapter adapter = new EventsAdapter(eventsList, this);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
