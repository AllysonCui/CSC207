package database;

import java.util.ArrayList;

public interface EventDsGateway {

    String getStatus(String title);

    String getDescription(String title);

    String getLocation(String title);

    ArrayList<Integer> getTime(String title);

    ArrayList<String> getParticipants(String title);

    String getOrganization(String title);

    void unPublishedToUpcoming(String title);

    void upcomingToPast(String title);

    ArrayList<String> eventSearch(String about_name);

    boolean checkIfEventNameExist(String eventName);

    void deleteEvent(String event_title);

    void editEvent(String title, int status, String description, String location, int year, int month, int day, int hour, int minute);
}
