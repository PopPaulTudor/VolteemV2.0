package volteem.com.volteem.model.entity;

public class User {
    private String id;
    private String firstName, lastName, eMail, city, phone, gender;
    private long birthDate;
    private int experience;
    private long creationTimestamp;

    public User() { ///constructor is empty to be able to call dataSnapshot on this class

    }

    public User(String id, String firstName, String lastName, String eMail, String city, String phone, String gender, long birthDate,
                int experience, long creationTimestamp) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.city = city;
        this.phone = phone;
        this.gender = gender;
        this.birthDate = birthDate;
        this.experience = experience;
        this.creationTimestamp = creationTimestamp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
