package volteem.com.volteem.presenter;

import android.util.Log;

import java.util.ArrayList;

import volteem.com.volteem.model.entity.NGO;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.model.view.model.NgoFragmentModel;
import volteem.com.volteem.util.DatabaseUtils;
import volteem.com.volteem.util.VolteemUtils;

public class NgoFragmentPresenter  implements Presenter, DatabaseUtils.NGOsCallBack {

    private View view;
    private NgoFragmentModel model;
    private DatabaseUtils databaseUtils;

    public NgoFragmentPresenter(View view)
    {
        this.view=view;
        this.databaseUtils=new DatabaseUtils(this);
        this.model=new NgoFragmentModel(null);
    }

    @Override
    public void onCreate() {
        if(databaseUtils==null) {
            databaseUtils=new DatabaseUtils(this);
        }
        if(model==null){
            this.model=new NgoFragmentModel(null);

        }
        if(model.getArrayList()==null) {
            Log.e("NGOs","not in memory yet");
            getNGOsList();
        }else {
            Log.e("NGOs","In the memory");
            view.onNGOsLoadSuccessful(model.getArrayList());
        }
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

    public void getNGOsList()
    {
        if(VolteemUtils.isNetworkAvailable())
            databaseUtils.getNGOsList();
        else
            view.onNGOsLoadFailed(new VolteemCommonException("network_issue", "No internet connection."));

    }

    @Override
    public void onNGOsLoadSuccessful(ArrayList<NGO> ngos) {
        // TODO: 5/18/2019 sort when we decide the fields ngo
        model.setArrayList(ngos);
        if (view.isViewActive())
            view.onNGOsLoadSuccessful(ngos);
    }

    @Override
    public void onNGOsLoadFailed(VolteemCommonException exception) {
        view.onNGOsLoadFailed(exception);

    }


    public interface  View{
        boolean isViewActive();

        void  onNGOsLoadSuccessful(ArrayList arrayList);

        void onNGOsLoadFailed(VolteemCommonException volteemCommonException);

    }
}
