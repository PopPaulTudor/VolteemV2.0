package volteem.com.volteem.model.entity;

public class Feedback {

    private String eventId;
    private String eventFeedback;

    public Feedback(String eventId, String eventFeedback) {
        this.eventId = eventId;
        this.eventFeedback = eventFeedback;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventFeedback() {
        return eventFeedback;
    }

    public void setEventFeedback(String eventFeedback) {
        this.eventFeedback = eventFeedback;
    }
}
