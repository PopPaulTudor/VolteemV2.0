package volteem.com.volteem.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.presenter.RegisterActivityPresenter;

public class RegisterActivity extends AppCompatActivity implements RegisterActivityPresenter.View {

    private EditText mEmail, mPassword, mPhone, mCity, mBirthDate, mFirstName, mLastname, mConfirmPass;
    private long birthdate;
    private Spinner spinner;
    private List<String> gender = new ArrayList<>();
    private String mGender = "Gender";
    private RegisterActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterActivityPresenter(this);
        presenter.onCreate();
        spinner = findViewById(R.id.register_gender);
        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mPhone = findViewById(R.id.register_phone);
        mCity = findViewById(R.id.register_city);
        mBirthDate = findViewById(R.id.register_birthdate);
        mFirstName = findViewById(R.id.register_first_name);
        mLastname = findViewById(R.id.register_last_name);
        mConfirmPass = findViewById(R.id.register_password_confirm);

        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++;
                        String birthDateString = dayOfMonth + "/" + month + "/" + year;
                        mBirthDate.setText(birthDateString);
                        month--;
                        myCalendar.set(year, month, dayOfMonth, 12, 15, 0);
                        birthdate = myCalendar.getTimeInMillis();

                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        gender.add("Gender");
        gender.add("Male");
        gender.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner.getSelectedItem().toString();
                if (!TextUtils.equals(selected, "Gender")) {
                    mGender = selected;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPass.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastname.getText().toString();
                String city = mCity.getText().toString();
                String phone = mPhone.getText().toString();
                presenter.onRegisterButtonPressed(eMail, password, confirmPassword, firstName, lastName, birthdate, city, phone, mGender);
            }
        });

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityByClass(LoginActivity.class);
            }
        });
    }

    private void startActivityByClass(Class activity) {
        Intent intent = new Intent(RegisterActivity.this, activity);
        startActivity(intent);
        finish();
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
    public void onRegisterSuccessful() {
        Toast.makeText(this, "Account successfully created. A verification email has been sent to your email address.", Toast.LENGTH_LONG).show();
        startActivityByClass(MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        startActivityByClass(LoginActivity.class);
    }

    @Override
    public void onRegisterFailed(@NonNull LoginException loginException) {
        String error = loginException.getMessage();
        switch (loginException.getCause()) {
            case "email":
                mEmail.setError(error);
                mEmail.requestFocus();
                break;
            case "password":
                mPassword.setError(error);
                mPassword.requestFocus();
                break;
            case "confirm_password":
                mConfirmPass.setError(error);
                mConfirmPass.requestFocus();
                break;
            case "firstname":
                mFirstName.setError(error);
                mFirstName.requestFocus();
                break;
            case "lastname":
                mLastname.setError(error);
                mLastname.requestFocus();
                break;
            case "city":
                mCity.setError(error);
                mCity.requestFocus();
                break;
            case "phone":
                mPhone.setError(error);
                mPhone.requestFocus();
                break;
            case "birthdate":
                mBirthDate.setError(error);
                mBirthDate.requestFocus();
                break;
            case "gender":
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
