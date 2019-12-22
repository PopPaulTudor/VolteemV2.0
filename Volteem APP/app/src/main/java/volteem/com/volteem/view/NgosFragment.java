package volteem.com.volteem.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.NGOsAdapter;
import volteem.com.volteem.callback.ActionListener;
import volteem.com.volteem.model.entity.NGO;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.NgoFragmentPresenter;
import volteem.com.volteem.util.VolteemConstants;

public class NgosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        NgoFragmentPresenter.View,ActionListener.NGOAdapterListener {

    private NgoFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<NGO> ngoList;
    private int mediumAnimTime;
    private TextView noNGOs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ngos, container, false);

        presenter = new NgoFragmentPresenter(this);
        noNGOs = view.findViewById(R.id.no_ngos_text);
        recyclerView = view.findViewById(R.id.RecViewNgos);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshNGO);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        mediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        noNGOs.setVisibility(View.GONE);
        presenter.onCreate();


        return view;
    }

    @Override
    public void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        loadNGOs();

    }

    private void loadNGOs()
    {
        swipeRefreshLayout.setRefreshing(true);
        noNGOs.setVisibility(View.GONE);
        presenter.getNGOsList();
    }

    @Override
    public boolean isViewActive() {
        return (isAdded() && !isDetached() && !isRemoving());
    }

    @Override
    public void onNGOsLoadSuccessful(ArrayList arrayList) {

        if(arrayList.isEmpty())
        {
            noNGOs.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }else
        {
            NGOsAdapter adapter= new NGOsAdapter(arrayList,this);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    @Override
    public void onNGOsLoadFailed(VolteemCommonException volteemCommonException) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), volteemCommonException.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPicturesLoaded() {

    }

    @Override
    public void onClickNGO(NGO ngo) {

        Intent intent= new Intent(getActivity(),NGOActivity.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable(VolteemConstants.INTENT_EXTRA_NGO, ngo);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
