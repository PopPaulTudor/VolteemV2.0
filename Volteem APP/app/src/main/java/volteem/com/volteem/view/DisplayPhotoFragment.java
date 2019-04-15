package volteem.com.volteem.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import volteem.com.volteem.R;
import volteem.com.volteem.presenter.DisplayPhotoFragmentPresenter;


public class DisplayPhotoFragment extends Fragment implements DisplayPhotoFragmentPresenter.View {

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.detailed_photo_profile, container, false);
        imageView = view.findViewById(R.id.display_photo_image_view);
        ImageView button = view.findViewById(R.id.display_photo_back_button);
        DisplayPhotoFragmentPresenter presenter = new DisplayPhotoFragmentPresenter(this);
        presenter.onCreate();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) getFragmentManager().popBackStackImmediate();
            }
        });
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }

    @Override
    public void getUserIdSuccessful(String userID) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("Photos").child("User").child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Objects.requireNonNull(getView())).load(uri).centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(imageView).load(Uri.parse("android.resource://volteem.com.volteem/drawable/ic_profile_default")).into(imageView);

            }
        });
    }

}