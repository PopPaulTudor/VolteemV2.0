package volteem.com.volteem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.EventsAdapter;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.SelectedEventsCategory;
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
    private BottomSheetDialog filtersBottomSheetDialog;

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
        noEventsTextView.setVisibility(View.GONE);
        addEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
            }
        });
        setHasOptionsMenu(true);
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
        mSwipeRefreshLayout.setRefreshing(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_events, menu);

        /*ComponentName cn = new ComponentName(getActivity(), VolunteerSearchableActivity.class);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();

        MenuItem searchMenu = menu.findItem(R.id.app_bar_search);*/
        /* beginning of implementation of filters */
        /* beginning of implementation of event category filtering */
        filtersBottomSheetDialog = new BottomSheetDialog(getActivity());
        View parentView = getLayoutInflater().inflate(R.layout.event_filters_bottom_sheet, null);
        filtersBottomSheetDialog.setContentView(parentView);
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        mBottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics()));
        final RadioGroup selectedEventsGroup = parentView.findViewById(R.id.selectedEventsRadioGroup);
        switch (presenter.getSelectedEventsCategory()) {
            case UNREGISTERED_EVENTS:
                selectedEventsGroup.check(R.id.unregisteredEvents);
                break;
            case REGISTERED_EVENTS:
                selectedEventsGroup.check(R.id.registeredEvents);
                break;
            case OWN_EVENTS:
                selectedEventsGroup.check(R.id.myEvents);
                break;
        }

        /* beginning of implementation of event type filtering */
        ArrayList<String> typeList = populateTypeList();
        final AppCompatSpinner typeFilterSpinner = parentView.findViewById(R.id.typeFilterSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, typeList);
        typeFilterSpinner.setAdapter(arrayAdapter);
        typeFilterSpinner.setSelection(presenter.getSelectedEventsType().ordinal());

        /* implementation of applying the filters (what happens in the view) */
        parentView.findViewById(R.id.applyFiltersButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersBottomSheetDialog.dismiss();
                SelectedEventsCategory selectedEventsCategory = null;
                Event.Type selectedEventsType = Event.Type.values()[typeFilterSpinner.getSelectedItemPosition()];
                switch (selectedEventsGroup.getCheckedRadioButtonId()) {
                    case R.id.unregisteredEvents:
                        selectedEventsCategory = SelectedEventsCategory.UNREGISTERED_EVENTS;
                        break;
                    case R.id.registeredEvents:
                        selectedEventsCategory = SelectedEventsCategory.REGISTERED_EVENTS;
                        break;
                    case R.id.myEvents:
                        selectedEventsCategory = SelectedEventsCategory.OWN_EVENTS;
                        break;
                }
                presenter.onApplyFiltersButtonPressed(selectedEventsCategory, selectedEventsType);
            }
        });

        ///TODO: add more filters
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Toast.makeText(getActivity(), "Pressed search", Toast.LENGTH_SHORT).show();
                //getActivity().onSearchRequested();
                return true;
            case R.id.action_filters:
                filtersBottomSheetDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<String> populateTypeList() {
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("All events");
        typeList.add("Sports");
        typeList.add("Music");
        typeList.add("Festival");
        typeList.add("Charity");
        typeList.add("Training");
        typeList.add("Other");
        return typeList;
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
        if (presenter.getSelectedEventsCategory() == SelectedEventsCategory.OWN_EVENTS) {
            Toast.makeText(getActivity(), "Event Activity for organisers not yet available.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), EventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(VolteemConstants.INTENT_EXTRA_EVENT, event);
        bundle.putSerializable(VolteemConstants.INTENT_EXTRA_FLAG, presenter.getSelectedEventsCategory());
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
    public void updateUIForEventsLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
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
            recyclerView.setVisibility(View.GONE);
        } else {
            noEventsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            EventsAdapter adapter = new EventsAdapter(eventsList, this, presenter.getSelectedEventsCategory());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
