package volteem.com.volteem.view;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import volteem.com.volteem.R;
import volteem.com.volteem.adapter.OrganiserEventViewPagerAdapter;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.RegisteredUser;
import volteem.com.volteem.presenter.OrganiserEventActivityPresenter;
import volteem.com.volteem.util.VolteemConstants;

public class OrganiserEventActivity extends AppCompatActivity implements OrganiserEventActivityPresenter.View {

    private OrganiserEventActivityPresenter presenter;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView acceptedTextView, registeredTextView;
    private ImageView mSquareImageView;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_event);
        presenter = new OrganiserEventActivityPresenter(getIntent().getExtras(), this);
        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);
        mToolbar = findViewById(R.id.toolbar);
        mSquareImageView = findViewById(R.id.collapsing_toolbar_image);
        acceptedTextView = findViewById(R.id.accept_number);
        registeredTextView = findViewById(R.id.reg_number);
        appBarLayout = findViewById(R.id.appbar);
        presenter.onCreate();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /*FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String userID = dataSnapshot1.getKey();
                    FirebaseDatabase.getInstance().getReference().child("events").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                FirebaseDatabase.getInstance().getReference().child("events").child(dataSnapshot2.getKey()).child("users").child(userID)
                                        .setValue(new RegisteredUser(userID, VolteemConstants.VOLUNTEER_EVENT_STATUS_PENDING, VolteemConstants.VOLUNTEER_EVENT_FLAG_PENDING));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void loadUI(Event event, Uri eventImage) {
        if (event.getAcceptedVolunteers().size() == 1) {
            acceptedTextView.setText("         " + event.getAcceptedVolunteers().size() +
                    "\nvolunteer");
        } else {
            acceptedTextView.setText("         " + event.getAcceptedVolunteers().size() +
                    acceptedTextView.getText());
        }
        if (event.getRegisteredVolunteers().size() == 1) {
            registeredTextView.setText("         " + event.getRegisteredVolunteers().size() +
                    "\nvolunteer");
        } else {
            registeredTextView.setText("         " + event.getRegisteredVolunteers().size() +
                    registeredTextView.getText());
        }
        Glide.with(mSquareImageView).load(eventImage).centerCrop().into(mSquareImageView);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R
                            .color.colorPrimary));
                } else {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R
                            .color.transparent));
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable(VolteemConstants.INTENT_EXTRA_EVENT, event);
        OrganiserEventInfoFragment fragmentInfo = new OrganiserEventInfoFragment();
        fragmentInfo.setArguments(bundle);

        OrganiserEventRegisteredUsersFragment fragmentRegistered = new OrganiserEventRegisteredUsersFragment();
        fragmentRegistered.setArguments(bundle);

        OrganiserEventAcceptedUsersFragment fragmentAccepted = new OrganiserEventAcceptedUsersFragment();
        fragmentAccepted.setArguments(bundle);

        OrganiserEventViewPagerAdapter mViewPagerAdapter = new OrganiserEventViewPagerAdapter
                (getSupportFragmentManager(), fragmentInfo, fragmentRegistered, fragmentAccepted);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
