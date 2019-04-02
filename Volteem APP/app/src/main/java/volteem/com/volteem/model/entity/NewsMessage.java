package volteem.com.volteem.model.entity;

public class NewsMessage extends Message {

    private Type type;
    private boolean notified;
    private boolean starred;
    private String eventID;

    public NewsMessage(){

    }

    public NewsMessage(String sentBy, String receivedBy, String id, String content, long timestamp, Type type, boolean notified, boolean starred, String eventID) {
        super(sentBy, receivedBy, id, content, timestamp);
        this.type = type;
        this.notified = notified;
        this.starred = starred;
        this.eventID = eventID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public enum Type {
        ACCEPT, FEEDBACK, EVENT_DELETED, REGISTERED, VOLUNTEER_LEFT;
    }
}
