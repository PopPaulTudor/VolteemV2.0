package volteem.com.volteem.model.view.model;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivityModel {

    private ModelCallback modelCallback;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MainActivityModel(ModelCallback modelCallback) {
        this.modelCallback = modelCallback;
    }

    public void logOut() {
        mAuth.signOut();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public interface ModelCallback {

    }
}
