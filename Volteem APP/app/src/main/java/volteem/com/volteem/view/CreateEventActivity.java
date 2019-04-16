package volteem.com.volteem.view;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import volteem.com.volteem.R;

public class CreateEventActivity extends AppCompatActivity {

    private EditText mName, mLocation, mDescription, mDeadline, mSize, mStartDate, mFinishDate;
    private ImageView mImage;
    private Spinner mTypeSpinner; // TODO refactor spinner and use some popup dialog
    private long startDate = -1, finishDate, deadline;
    private Uri mUriPicture = null, mUriPDF = null;
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private boolean mSelectedPicture = false;
    private boolean mSelectedPDF = false;
    private Button mLoadPdf, mDoneButton;
    //private ArrayList<InterviewQuestion> questionsList = new ArrayList<>();
    private Resources resources;
    private TextView questionText;
    private int longAnimTime;
   // private EventQuestionsAdapter eventQuestionsAdapter;
    private ScrollView createEventScrollView;
    private NestedScrollView questionsScrollView;
    private RecyclerView questionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mName = findViewById(R.id.event_deadline);
        mLocation = findViewById(R.id.event_location);
        mStartDate = findViewById(R.id.event_date_start_create);
        mFinishDate = findViewById(R.id.event_date_finish_create);
        mTypeSpinner = findViewById(R.id.event_type);
        mDescription = findViewById(R.id.event_description);
        mDeadline = findViewById(R.id.event_deadline_create);
        mImage = findViewById(R.id.event_image);
        mSize = findViewById(R.id.event_size);
        mDoneButton = findViewById(R.id.questions_done);
        questionText = findViewById(R.id.question_text);
        createEventScrollView = (ScrollView) findViewById(R.id.create_event);
        questionsScrollView = (NestedScrollView) findViewById(R.id.questionsScrollView);
        questionsRecyclerView = (RecyclerView) findViewById(R.id.questions_recyclerView);
        questionsRecyclerView.setHasFixedSize(true);
        resources = getResources();
        longAnimTime = resources.getInteger(android.R.integer.config_longAnimTime);
        populateUriList();
        populateSpinnerArray();

        /*Glide.with()
        Picasso.get().load(imageUris.get(NUMBER_OF_EVENTS_ON_PAGE)).fit().centerCrop().into(mImage);
        Button mSaveEvent = findViewById(R.id.save_event);
        Button mCancel = findViewById(R.id.cancel_event);
        mLoadPdf = findViewById(R.id.upload_pdf);*/
    }

    private void populateSpinnerArray() {
        typeList.add("Type");
        //typeList.addAll(EventType.getAllAsList());
    }

    private Uri parseUri(int ID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(ID)
                + '/' + getResources().getResourceTypeName(ID) + '/' + getResources().getResourceEntryName(ID));

    }

    private void populateUriList() {
        imageUris.add(parseUri(R.drawable.image_no_type));
        imageUris.add(parseUri(R.drawable.image_sports));
        imageUris.add(parseUri(R.drawable.image_music));
        imageUris.add(parseUri(R.drawable.image_festival));
        imageUris.add(parseUri(R.drawable.image_charity));
        imageUris.add(parseUri(R.drawable.image_training));
        imageUris.add(parseUri(R.drawable.image_other));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
