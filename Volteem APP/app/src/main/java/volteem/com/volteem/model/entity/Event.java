package volteem.com.volteem.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

    private String createdBy;
    private String name;
    private String location;
    private String description;
    private String eventID;
    private String imageUri;
    private long startDate, finishDate, deadline;
    private long timestamp;
    private Type type;
    private int size;
    private ArrayList<String> registered_volunteers = new ArrayList<>();
    private ArrayList<String> accepted_volunteers = new ArrayList<>();
    private ArrayList<String> requiredQuestions = new ArrayList<>();

    public Event(String s, String s1, String s2, int i, int i1, String s3, String s4, int i2, int i3, Object o) {

    }

    public Event(){}

    /**
     * @param createdBy id of the user who created the event
     * @param name the name of the event
     * @param location the location where the event takes place
     * @param description the event's description
     * @param eventID the event's id
     * @param imageUri the event's image uri (can be null)
     * @param startDate the start date of the event
     * @param finishDate the finish date of the event
     * @param deadline the deadline of the event
     * @param timestamp the timestamp of the creation of the event
     * @param type the type of the event (it is an enum)
     * @param size the number of volunteers needed for this event
     * @param requiredQuestions the list of ids for the required questions of the event's form
     */
    public Event(String createdBy, String name, String location, String description, String eventID,
                 String imageUri, long startDate, long finishDate, long deadline, long timestamp, Type type, int size, ArrayList<String> requiredQuestions) {
        this.createdBy = createdBy;
        this.name = name;
        this.location = location;
        this.description = description;
        this.eventID = eventID;
        this.imageUri = imageUri;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.deadline = deadline;
        this.timestamp = timestamp;
        this.type = type;
        this.size = size;
        this.requiredQuestions = requiredQuestions;
    }

    public Event(String createdBy, String name, String location, long startDate, long finishDate, Type type, String eventID,
                 String description, long deadline, int size, ArrayList<String> registered_volunteers, ArrayList<String> accepted_volunteers) {
        this.createdBy = createdBy;
        this.name = name;
        this.location = location;
        this.type = type;
        this.finishDate = finishDate;
        this.startDate = startDate;
        this.description = description;
        this.deadline = deadline;
        this.size = size;
        this.eventID = eventID;
        this.registered_volunteers = registered_volunteers;
        this.accepted_volunteers = accepted_volunteers;
        this.requiredQuestions = new ArrayList<>();
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public enum Type {
        NIL("Type"),
        SPORTS("Sports"),
        MUSIC("Music"),
        FESTIVAL("Festival"),
        CHARITY("Charity"),
        TRAINING("Training"),
        OTHER("Other");

        private String typeText;

        Type(String name){
            this.typeText = name;
        }

        @Override
        public String toString() {
            return this.typeText;
        }
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public ArrayList<String> getRegistered_volunteers() {
        return registered_volunteers;
    }

    public void setRegistered_volunteers(ArrayList<String> registered_volunteers) {
        this.registered_volunteers = registered_volunteers;
    }

    public ArrayList<String> getAccepted_volunteers() {
        return accepted_volunteers;
    }

    public void setAccepted_volunteers(ArrayList<String> accepted_volunteers) {
        this.accepted_volunteers = accepted_volunteers;
    }

    public ArrayList<String> getRequiredQuestions() {
        return requiredQuestions;
    }

    public void setRequiredQuestions(ArrayList<String> requiredQuestions) {
        this.requiredQuestions = requiredQuestions;
    }
}
