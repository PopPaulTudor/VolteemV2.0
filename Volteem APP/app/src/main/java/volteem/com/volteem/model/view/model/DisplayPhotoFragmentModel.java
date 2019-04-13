package volteem.com.volteem.model.view.model;

public class DisplayPhotoFragmentModel {

    private String userID;

    public DisplayPhotoFragmentModel(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
