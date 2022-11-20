package par_join_event_use_case;

public class ParJoinEventRequestModel {
    private String par_username;
    private String event_name;

    public ParJoinEventRequestModel(String par_username, String event_name) {
        this.par_username = par_username;
        this.event_name = event_name;
    }

    public String getPar_username() {
        return par_username;
    }

    public void setPar_username(String par_username) {
        this.par_username = par_username;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
}