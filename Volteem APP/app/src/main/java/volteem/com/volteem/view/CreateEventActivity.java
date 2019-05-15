package volteem.com.volteem.view;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.CreateEventActivityPresenter;
import volteem.com.volteem.util.PermissionUtil;
import volteem.com.volteem.util.VolteemConstants;
import volteem.com.volteem.util.VolteemUtils;

public class CreateEventActivity extends AppCompatActivity implements CreateEventActivityPresenter.View {

    private EditText mName, mLocation, mDescription, mDeadline, mSize, mStartDate, mFinishDate;
    private ImageView mImage;
    private Spinner mTypeSpinner; // TODO refactor spinner and use some popup dialog
    private long startDate = -1, finishDate = -1, deadline = -1;
    private Uri mUriPicture = null, mUriPDF = null;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private boolean mSelectedPicture = false;
    private boolean mSelectedPDF = false;
    private Button mLoadPdf, mDoneButton;
    //private ArrayList<InterviewQuestion> questionsList = new ArrayList<>();
    private Resources resources;
    private TextView questionText;
    private int longAnimTime;
    //private EventQuestionsAdapter eventQuestionsAdapter;
    private ScrollView createEventScrollView;
    private NestedScrollView questionsScrollView;
    private RecyclerView questionsRecyclerView;
    private CreateEventActivityPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new CreateEventActivityPresenter(this);

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
        createEventScrollView = findViewById(R.id.create_event);
        questionsScrollView = findViewById(R.id.questionsScrollView);
        questionsRecyclerView = findViewById(R.id.questions_recyclerView);
        questionsRecyclerView.setHasFixedSize(true);
        resources = getResources();
        longAnimTime = resources.getInteger(android.R.integer.config_longAnimTime);
        populateUriList();

        Glide.with(mImage).load(imageUris.get(0)).centerCrop().into(mImage);
        ArrayAdapter<Event.Type> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Event.Type.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mSelectedPicture) {
                    Glide.with(mImage).load(imageUris.get(mTypeSpinner.getSelectedItemPosition())).centerCrop().into(mImage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.isStorageReadPermissionGranted(CreateEventActivity.this)) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, VolteemConstants.GALLERY_INTENT);
                } else {
                    Snackbar.make(view, getString(R.string.allow_storge_permission), Snackbar.LENGTH_LONG).setAction(getString(R.string
                            .set_permission), new View
                            .OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(CreateEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    VolteemConstants.STORAGE_REQUEST_CODE);
                        }
                    }).show();
                }
            }
        });

        mStartDate.setOnClickListener(setOnClickListenerCalendar(mStartDate));
        mFinishDate.setOnClickListener(setOnClickListenerCalendar(mFinishDate));
        mDeadline.setOnClickListener(setOnClickListenerCalendar(mDeadline));
        findViewById(R.id.save_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(CreateEventActivity.this, "Creating your amazing event", "", true);
                String eventName = mName.getText().toString();
                String location = mLocation.getText().toString();
                Event.Type type = (Event.Type) mTypeSpinner.getSelectedItem();
                String description = mDescription.getText().toString();
                int volunteersNeeded = mSize.getText().toString().isEmpty() ? 0 : Integer.parseInt(mSize.getText().toString());
                presenter.createEvent(eventName, location, startDate, finishDate, type, description, deadline, volunteersNeeded, mUriPicture);
            }
        });
        findViewById(R.id.cancel_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLoadPdf = findViewById(R.id.upload_pdf);
        presenter.onCreate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == VolteemConstants.GALLERY_INTENT) {
                mUriPicture = data.getData();
                mSelectedPicture = true;
                Glide.with(mImage).load(mUriPicture).centerCrop().into(mImage);
            } else {
                /*if (requestCode == PICK_PDF) {
                    mUriPDF = data.getData();
                    mSelectedPDF = true;
                    mLoadPdf.setText(ImageUtils.getFileName(mUriPDF, CreateEventActivity.this));
                }*/
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog leaveAlertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.are_you_sure_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CreateEventActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing, dismiss dialog
                    }
                })
                .create();
        leaveAlertDialog.show();
    }

    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void populateUriList() {
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_no_type));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_sports));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_music));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_festival));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_charity));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_training));
        imageUris.add(VolteemUtils.parseUri(R.drawable.image_other));
    }

    private View.OnClickListener setOnClickListenerCalendar(final EditText editText) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++;
                        editText.setText(dayOfMonth + "/" + month + "/" + year);
                        month--;
                        myCalendar.set(year, month, dayOfMonth, 12, 15, 0);
                        if (editText.equals(mStartDate)) {
                            startDate = myCalendar.getTimeInMillis();
                        } else if (editText.equals(mFinishDate)) {
                            finishDate = myCalendar.getTimeInMillis();
                        } else if (editText.equals(mDeadline)) {
                            deadline = myCalendar.getTimeInMillis();
                        }

                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onCreateEventSuccessful() { finish();
    }

    @Override
    public void onCreateEventFailed(VolteemCommonException exception) {
        progressDialog.dismiss();
        String error = exception.getMessage();
        switch (exception.getCause()) {
            case VolteemConstants.EXCEPTION_EVENT_NAME:
                mName.setError(error);
                mName.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_LOCATION:
                mLocation.setError(error);
                mLocation.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_DESCRIPTION:
                mDescription.setError(error);
                mDescription.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_SIZE:
                mSize.setError(error);
                mSize.requestFocus();
                break;
            default:
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
