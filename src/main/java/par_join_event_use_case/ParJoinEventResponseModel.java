package par_join_event_use_case;

public class ParJoinEventResponseModel {
    String eventName;
    String message;

    /** This is the construct method of EventLeaveResponseModel.
     *
     * @param eventName  The event name that a participant want to join.
     */
    public ParJoinEventResponseModel(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
    public String getMessage() {
        return this.message;
    }

    public void setEventname(String eventName) {
        this.eventName = eventName;
    }
    public void setMessage(String message) {
        this.message = message;
    }



}
