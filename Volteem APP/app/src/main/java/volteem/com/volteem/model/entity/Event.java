package volteem.com.volteem.model.entity;

import java.util.ArrayList;

public class Event {

    private String created_by;
    private String name;
    private String location;
    private String type;
    private String description;
    private String eventID;
    private long startDate, finishDate, deadline;
    private int size;
    private ArrayList<String> registered_volunteers;
    private ArrayList<String> accepted_volunteers;
    private ArrayList<String> requiredQuestions;


    public Event(String s, String s1, String s2, int i, int i1, String s3, String s4, int i2, int i3, Object o) {

    }

    public Event(String created_by, String name, String location, long startDate, long finishDate, String type, String eventID,
                 String description, long deadline, int size, ArrayList<String> requiredQuestions) {
        this.created_by = created_by;
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.type = type;
        this.description = description;
        this.deadline = deadline;
        this.size = size;
        this.eventID = eventID;
        this.requiredQuestions = requiredQuestions;
        this.registered_volunteers = new ArrayList<>();
        this.accepted_volunteers = new ArrayList<>();
    }

    public Event(String created_by, String name, String location, long startDate, long finishDate, String type, String eventID,
                 String description, long deadline, int size, ArrayList<String> registered_volunteers, ArrayList<String> accepted_volunteers) {
        this.created_by = created_by;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
