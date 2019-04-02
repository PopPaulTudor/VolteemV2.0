package volteem.com.volteem.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import volteem.com.volteem.R;
import volteem.com.volteem.model.entity.LoginException;
import volteem.com.volteem.presenter.ProfileFragmentPresenter;


public class ProfileFragment extends Fragment implements ProfileFragmentPresenter.View {


    private TextView ageTextView,userNameTextView, emailTextView, addressTextView, phoneTextView;
    private CircleImageView profileCircleImage;
    private ProfileFragmentPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        presenter= new ProfileFragmentPresenter(this);
        presenter.onCreate();

        ageTextView= v.findViewById(R.id.profile_age);
        userNameTextView= v.findViewById(R.id.profile_username);
        emailTextView= v.findViewById(R.id.profile_email);
        addressTextView= v.findViewById(R.id.profile_adress);
        profileCircleImage= v.findViewById(R.id.profile_circle_image);
        phoneTextView=v.findViewById(R.id.profile_phone);

        presenter.getProfileInformation();
        presenter.getProfilePicture();

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

        userNameTextView.setText(username);
        emailTextView.setText(email);
        addressTextView.setText(address);
        ageTextView.setText(age);
        phoneTextView.setText(phone);

    }

    @Override
    public void onProfileInformationFailed(LoginException loginException) {

    }
}

