package volteem.com.volteem.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.NewsMessage;
import volteem.com.volteem.model.entity.SelectedEventsCategory;
import volteem.com.volteem.model.entity.VolteemCommonException;
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

        mLeaveEvent = findViewById(R.id.event_leave);
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
        if (presenter.getFlag() == SelectedEventsCategory.UNREGISTERED_EVENTS) {
            ///Update View for a user who hasn't registered to this event
            mSignupForEventFloatingButton.setVisibility(View.VISIBLE);
        } else {
            ///Update View for a user who has registered to this event
            mLeaveEvent.setVisibility(View.VISIBLE);
        }

        mSignupForEventFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog
                        (EventActivity.this);
                View parentView = getLayoutInflater().inflate(R.layout
                        .event_register_bottom_sheet_design, null);
                mBottomSheetDialog.setContentView(parentView);
                BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View)
                        parentView.getParent());
                mBottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension
                        (TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics()));
                mBottomSheetDialog.show();

                ///TODO: find why bottom sheet does not show
                parentView.findViewById(R.id.registerForEvent).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        presenter.onSignUpForEventButtonPressed();
                        mBottomSheetDialog.dismiss();
                        Toast.makeText(EventActivity.this, "Signing up for event...", Toast.LENGTH_SHORT).show();
                    }
                });

                parentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                    }
                });
            }
        });

        presenter.onCreate();
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
    public void onRegisterToEventSuccessful() {
        finish();
    }

    @Override
    public void onRegisterToEventFailed(VolteemCommonException exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
