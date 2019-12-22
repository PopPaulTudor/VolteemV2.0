package volteem.com.volteem.model.entity;

public class RegisteredUser {
    private String id;
    private String status;
    private String flag;

    public RegisteredUser(String id, String status, String flag) {
        this.id = id;
        this.status = status;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
