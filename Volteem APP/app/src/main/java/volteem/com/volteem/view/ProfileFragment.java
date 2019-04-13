package volteem.com.volteem.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import volteem.com.volteem.R;
import volteem.com.volteem.adapter.ProfileEventAdapter;
import volteem.com.volteem.model.entity.Event;
import volteem.com.volteem.model.entity.Feedback;
import volteem.com.volteem.model.entity.VolteemCommonException;
import volteem.com.volteem.presenter.ProfileFragmentPresenter;
import volteem.com.volteem.util.PermissionUtil;
import volteem.com.volteem.util.VolteemConstants;


public class ProfileFragment extends Fragment implements ProfileFragmentPresenter.View {

    private TextView emailTextView;
    private EditText ageEditText, userNameEditText, addressEditText, phoneEditText;
    private TextView noEventsTextView;
    private CircleImageView profileCircleImage;
    private ProfileFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView saveData, discardData;
    private String oldAge, oldUserName, oldAdress, oldPhone;
    private long newAge;
    private InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        presenter = new ProfileFragmentPresenter(this);

        ageEditText = v.findViewById(R.id.profile_age);
        userNameEditText = v.findViewById(R.id.profile_username);
        emailTextView = v.findViewById(R.id.profile_email);
        addressEditText = v.findViewById(R.id.profile_address);
        phoneEditText = v.findViewById(R.id.profile_phone);
        profileCircleImage = v.findViewById(R.id.profile_circle_image);
        noEventsTextView = v.findViewById(R.id.profile_no_events);
        recyclerView = v.findViewById(R.id.profile_rec_view);
        progressBar = v.findViewById(R.id.profile_progress_bar);
        saveData = v.findViewById(R.id.profile_save_new_data);
        discardData = v.findViewById(R.id.profile_discard_new_data);

        imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        stopEditing();

        progressBar.setVisibility(View.VISIBLE);
        presenter.onCreate();

        profileCircleImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.view_image));
                arrayAdapter.add(getString(R.string.change_image));

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String choice = arrayAdapter.getItem(which);
                        assert choice != null;
                        if (choice.contains("Change")) {
                            if (PermissionUtil.isStorageReadPermissionGranted(getContext())) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, VolteemConstants.GALLERY_INTENT);
                            } else {
                                Snackbar.make(getView(), "Please allow storage permission", Snackbar.LENGTH_LONG).setAction("Set " +
                                        "Permission", new
                                        View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                            }
                                        }).show();
                            }
                        } else {
                            DisplayPhotoFragment displayPhotoFragment = new DisplayPhotoFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "user");
                            displayPhotoFragment.setArguments(bundle);
                            assert getFragmentManager() != null;
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.content_frame, displayPhotoFragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                });
                builderSingle.show();
            }
        });

        ageEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startEditing(ageEditText);
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++;
                        String birthDateString = dayOfMonth + "/" + month + "/" + year;
                        ageEditText.setText(birthDateString);
                        month--;
                        myCalendar.set(year, month, dayOfMonth, 12, 15, 0);
                        newAge = myCalendar.getTimeInMillis();

                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                return false;
            }
        });

        phoneEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startEditing(phoneEditText);
                return false;
            }
        });

        userNameEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startEditing(userNameEditText);
                return false;
            }
        });

        addressEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startEditing(addressEditText);
                return false;
            }
        });

        discardData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressEditText.setText(oldAdress);
                userNameEditText.setText(oldUserName);
                ageEditText.setText(oldAge);
                phoneEditText.setText(oldPhone);
                stopEditing();
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.changeData(userNameEditText.getText().toString(), phoneEditText.getText().toString(), addressEditText.getText().toString(), newAge);
                stopEditing();
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }
        });

        return v;
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

    @Override
    public void onProfileInformationSucceeded(String username, String email, String address, String age, String phone) {
        Toast.makeText(getActivity(), username, Toast.LENGTH_SHORT).show();
        userNameEditText.setText(username);
        emailTextView.setText(email);
        addressEditText.setText(address);
        ageEditText.setText(age);
        phoneEditText.setText(phone);
        oldAdress = address;
        oldAge = age;
        oldPhone = phone;
        oldUserName = username;
    }

    @Override
    public void onProfileInformationFailed(VolteemCommonException exception) {
        Log.e(exception.getCause(), exception.getMessage());
    }

    @Override
    public void onProfilePictureSucceeded(Uri uri) {
        Glide.with(this).load(uri).into(profileCircleImage);
    }

    @Override
    public void onProfilePictureFailed(VolteemCommonException volteemCommonException) {
        Glide.with(this).load(Uri.parse("android.resource://volteem.com.volteem/drawable/ic_profile_default")).into(profileCircleImage);
    }

    @Override
    public void onEventsSucceeded(ArrayList<Event> events, ArrayList<Feedback> feedbacks) {

        ArrayList<Event> events1 = new ArrayList<>();
        events1.add(new Event("1", "2", "3", 4, 5, "1", "6", 7, 8, null));
        if (!events1.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noEventsTextView.setVisibility(View.GONE);

            ProfileEventAdapter adapter = new ProfileEventAdapter(events1, feedbacks);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        } else {
            recyclerView.setVisibility(View.GONE);
            noEventsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEventsFailed(VolteemCommonException volteemCommonException) {
        progressBar.setVisibility(View.GONE);

        if (volteemCommonException.getMessage().equals("There are no events")) {
            recyclerView.setVisibility(View.GONE);
            noEventsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProfilePhotoChangedSucceeded(String message) {
        Snackbar.make(Objects.requireNonNull(getView()), "Photo changed", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChangedSucceeded(String message) {
        Snackbar.make(Objects.requireNonNull(getView()), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChangedFailed() {
        Snackbar.make(Objects.requireNonNull(getView()), "Error at changing data", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VolteemConstants.GALLERY_INTENT && (data != null)) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(profileCircleImage);
            presenter.changePhoto(uri);
        }
    }

    void startEditing(EditText editText) {
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        if (editText.equals(ageEditText)) editText.setInputType(InputType.TYPE_CLASS_DATETIME);
        else editText.setInputType(InputType.TYPE_CLASS_TEXT);
        saveData.setVisibility(View.VISIBLE);
        discardData.setVisibility(View.VISIBLE);
    }

    void stopEditing() {

        userNameEditText.setInputType(0);
        ageEditText.setInputType(0);
        addressEditText.setInputType(0);
        phoneEditText.setInputType(0);

        saveData.setVisibility(View.GONE);
        discardData.setVisibility(View.GONE);
    }

}