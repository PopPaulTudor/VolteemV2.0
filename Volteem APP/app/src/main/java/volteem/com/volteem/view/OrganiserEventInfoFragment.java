package volteem.com.volteem.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.OrganiserEventInfoFragmentPresenter;
import volteem.com.volteem.util.CalendarUtils;
import volteem.com.volteem.util.VolteemConstants;

public class OrganiserEventInfoFragment extends Fragment implements OrganiserEventInfoFragmentPresenter.View {

    private OrganiserEventInfoFragmentPresenter presenter;
    private static final int GALLERY_INTENT = 1;
    private static final int PICK_PDF = 2;
    private EditText mName, mLocation, mStartDate, mDescription, mDeadline, mSize, mFinishDate;
    private long currentStartDate, currentFinishDate, currentDeadline;
    private Spinner mType;
    private ImageView mImage;
    private ArrayList<String> typeList = new ArrayList<>();
    private boolean hasSelectedPDF = false;
    private boolean hasUserSelectedPicture = false;
    private Button changeContract, saveChanges, cancelChanges;
    private ProgressDialog progressDialog;
    private AlertDialog deleteEventDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organiser_event_info, container, false);
        presenter = new OrganiserEventInfoFragmentPresenter(getArguments(), this);
        populateSpinnerArray();

        saveChanges = view.findViewById(R.id.save_changes);
        cancelChanges = view.findViewById(R.id.cancel_changes);
        mName = view.findViewById(R.id.event_name);
        mLocation = view.findViewById(R.id.event_location);
        mStartDate = view.findViewById(R.id.event_date_start);
        mFinishDate = view.findViewById(R.id.event_date_finish);
        mDeadline = view.findViewById(R.id.event_deadline);
        mType = view.findViewById(R.id.event_type);
        mDescription = view.findViewById(R.id.event_description);
        mSize = view.findViewById(R.id.event_size);
        mImage = view.findViewById(R.id.event_org_image);
        changeContract = view.findViewById(R.id.event_contract);
        changeContract.setClickable(false);
        mImage.setClickable(false);
        mStartDate.setOnClickListener(setonClickListenerCalendar(mStartDate));
        mFinishDate.setOnClickListener(setonClickListenerCalendar(mFinishDate));
        mDeadline.setOnClickListener(setonClickListenerCalendar(mDeadline));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);
        setHasOptionsMenu(true);
        presenter.onCreate();
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(getActivity(), "Updating your amazing event", "", true);
                String currentName, currentLocation, currentDescription;
                int currentSize;
                Event.Type currentType;
                currentName = mName.getText().toString();
                currentLocation = mLocation.getText().toString();
                currentType = Event.Type.values()[mType.getSelectedItemPosition() + 1];
                currentDescription = mDescription.getText().toString();
                currentSize = TextUtils.isEmpty(mSize.getText().toString()) ? -1 : Integer.parseInt(mSize.getText().toString());
                presenter.onSaveButtonPressed(currentName, currentLocation, currentType, currentDescription, currentSize, currentStartDate,
                        currentFinishDate, currentDeadline);
            }
        });
        cancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onCancelButtonPressed();
            }
        });
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
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void populateSpinnerArray() {
        typeList.add("Sports");
        typeList.add("Music");
        typeList.add("Festival");
        typeList.add("Charity");
        typeList.add("Training");
        typeList.add("Other");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event_edit, menu);
        deleteEventDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this event? You will not be able to undo this action.")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog = ProgressDialog.show(getActivity(), "Deleting your event :(((", "", true);
                        presenter.onDeleteItemPressed();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                toggleEdit(true);
                return true;
            case R.id.action_deleteEvent:
                deleteEventDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    View.OnClickListener setonClickListenerCalendar(final EditText editText) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        editText.setText(dayOfMonth + "/" + month + "/" + year);
                        month--;
                        myCalendar.set(year, month, dayOfMonth);
                        if (editText.equals(mStartDate))
                            currentStartDate = myCalendar.getTimeInMillis();
                        else if (editText.equals(mFinishDate))
                            currentFinishDate = myCalendar.getTimeInMillis();
                        else if (editText.equals(mDeadline))
                            currentDeadline = myCalendar.getTimeInMillis();

                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
    }

    @Override
    public void loadUI(Event event, Uri eventImage) {
        mName.setText(event.getName());
        mLocation.setText(event.getLocation());
        mType.setSelection(typeList.indexOf(event.getType().toString()));
        mDescription.setText(event.getDescription());
        mDeadline.setText(CalendarUtils.getStringDateFromMM(event.getDeadline()));
        mStartDate.setText(CalendarUtils.getStringDateFromMM(event.getStartDate()));
        mFinishDate.setText(CalendarUtils.getStringDateFromMM(event.getFinishDate()));
        mSize.setText(event.getSize() + "");
        Glide.with(mImage).load(eventImage).centerCrop().into(mImage);
        currentStartDate = event.getStartDate();
        currentFinishDate = event.getFinishDate();
        currentDeadline = event.getDeadline();
        toggleEdit(false);
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onEditEventSuccessful() {
        progressDialog.dismiss();
        toggleEdit(false);
    }

    @Override
    public void onEditEventFailed(VolteemCommonException exception) {
        progressDialog.dismiss();
        String error = exception.getMessage();
        switch (exception.getCause()) {
            case VolteemConstants.EXCEPTION_EVENT_NAME:
                mName.setError(error);
                mName.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_DESCRIPTION:
                mDescription.setError(error);
                mDescription.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_SIZE:
                mSize.setError(error);
                mSize.requestFocus();
                break;
            case VolteemConstants.EXCEPTION_EVENT_LOCATION:
                mLocation.setError(error);
                mLocation.requestFocus();
                break;
            default:
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDeleteEventSuccessful() {
        getActivity().finish();
    }

    @Override
    public void onDeleteEventFailed(VolteemCommonException exception) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void toggleEdit(boolean bool) {
        mName.setEnabled(bool);
        mLocation.setEnabled(bool);
        mType.setEnabled(bool);
        mDescription.setEnabled(bool);
        mSize.setEnabled(bool);
        mStartDate.setEnabled(bool);
        mFinishDate.setEnabled(bool);
        mDeadline.setEnabled(bool);
        saveChanges.setVisibility(bool ? View.VISIBLE : View.GONE);
        cancelChanges.setVisibility(bool ? View.VISIBLE : View.GONE);
    }
}
