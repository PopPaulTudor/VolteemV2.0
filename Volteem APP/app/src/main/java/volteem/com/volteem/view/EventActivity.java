package volteem.com.volteem.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.presenter.EventActivityPresenter;
import volteem.com.volteem.util.CalendarUtils;

public class EventActivity extends AppCompatActivity implements EventActivityPresenter.View {

    private TextView mEventName, mEventLocation, mEventType, mEventDescription, mEventDeadline,
            mEventSize, mStatus, mEventStartDate, mEventFinishDate;
    private FloatingActionButton mSignupForEventFloatingButton;
    private Button mLeaveEvent, mDownloadContract;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EventActivityPresenter presenter;
    private ImageView collapsingToolbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        presenter = new EventActivityPresenter(this, getIntent().getExtras());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else Log.e("actionbar", "null");

        mEventName = findViewById(R.id.event_name);
        mEventLocation = findViewById(R.id.event_location);
        mEventStartDate = findViewById(R.id.event_start_date);
        mEventFinishDate = findViewById(R.id.event_finish_date);
        mEventType = findViewById(R.id.event_type);
        mEventDescription = findViewById(R.id.event_description);
        mEventDeadline = findViewById(R.id.event_deadline);
        mEventSize = findViewById(R.id.event_size);
        mStatus = findViewById(R.id.event_status);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarImage = findViewById(R.id.collapsing_toolbar_image);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mSignupForEventFloatingButton = findViewById(R.id.fab);
        presenter.onCreate();
        mSignupForEventFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSignUpForEventButtonPressed();
            }
        });
        mLeaveEvent = findViewById(R.id.event_leave);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void loadUI(Event currentEvent, Uri uri) {
        Glide.with(collapsingToolbarImage).load(uri).centerCrop().into(collapsingToolbarImage);
        collapsingToolbarLayout.setTitle(currentEvent.getName());
        mEventName.setText(currentEvent.getName());
        mEventLocation.setText(currentEvent.getLocation());
        mEventStartDate.setText(CalendarUtils.getStringDateFromMM(currentEvent.getStartDate()));
        mEventFinishDate.setText(CalendarUtils.getStringDateFromMM(currentEvent.getFinishDate()));
        mEventType.setText(currentEvent.getType().toString());
        mEventDescription.setText(currentEvent.getDescription());
        String deadline = CalendarUtils.getStringDateFromMM(currentEvent.getDeadline());
        mEventSize.setText(currentEvent.getSize() + " volunteers");

        int index = deadline.lastIndexOf("/");
        deadline = deadline.substring(0, index) + deadline.substring(index + 1);
        mEventDeadline.setText(deadline);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
