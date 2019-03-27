package volteem.com.volteem.model.view.model;


import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import volteem.com.volteem.model.entity.LoginException;

public class MainActivityModel {

    private ModelCallback modelCallback;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();





    public MainActivityModel(ModelCallback modelCallback)
    {
        this.modelCallback=modelCallback;
    }


    public void logOut()
    {
        mAuth.signOut();

    }


    public interface ModelCallback {

    }
}
