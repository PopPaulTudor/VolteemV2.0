package volteem.com.volteem.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.NGO;
import volteem.com.volteem.presenter.NGOActivityPresenter;

public class NGOActivity extends AppCompatActivity implements NGOActivityPresenter.View {

    private TextView nameNGO, descriptionNGO, locationNGO;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageNGO;
    private NGOActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        presenter = new NGOActivityPresenter(this, getIntent().getExtras());
        Toolbar toolbar =  findViewById(R.id.toolbarNGO);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        else Log.e("actionbar", "null");

        descriptionNGO = findViewById(R.id.description_NGO);


    }

    @Override
    public void loadNGO(NGO ngo) {
        //Glide.with()

    }
}
