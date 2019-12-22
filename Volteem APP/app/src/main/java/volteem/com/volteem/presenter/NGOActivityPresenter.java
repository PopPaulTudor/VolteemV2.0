package volteem.com.volteem.presenter;

import android.os.Bundle;

import volteem.com.volteem.model.entity.NGO;
import volteem.com.volteem.model.view.model.NGOActivityModel;
import volteem.com.volteem.util.VolteemConstants;

public class NGOActivityPresenter implements Presenter {

    private boolean hasActionHappened;
    private View view;
    private NGOActivityModel model;
    private Bundle bundleExtras;


    public NGOActivityPresenter(View view, Bundle bundle)
    {
        this.view = view;
        this.bundleExtras = bundle;
        this.model =new NGOActivityModel((NGO) bundle.getSerializable(VolteemConstants.INTENT_EXTRA_NGO));
    }

    @Override
    public void onCreate() {

        if(model == null)
            model = new NGOActivityModel((NGO) bundleExtras.getSerializable(VolteemConstants.INTENT_EXTRA_NGO));
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

    public void onSignUpForNGOButtonPressed() {
        hasActionHappened = true;
    }


    public interface View{
        void loadNGO(NGO ngo);
    }
}
