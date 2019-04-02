package volteem.com.volteem.model.entity;

public abstract class Message {
    private String sentBy;
    private String receivedBy;
    private String id;
    private String content;
    private long timestamp;

    public Message() {
    }

    public Message(String sentBy, String receivedBy, String id, String content, long timestamp) {
        this.sentBy = sentBy;
        this.receivedBy = receivedBy;
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
