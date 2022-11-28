package use_cases.org_create_event_use_case;

import database.EventDsGateway;
import database.OrgDsGateway;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrgCreateEventInteractor implements OrgCreateEventInputBoundary {

    EventDsGateway eventDsGateway;
    OrgDsGateway orgDsGateway;
    OrgCreateEventOutputBoundary orgCreateEventOutputBoundary;

    /**This is the constructor for this class.
     *
     * @param eventDsGateway The database gateway for events
     * @param orgDsGateway The database gateway for organizers
     * @param orgCreateEventOutputBoundary The output boundary used to show whether event creation was successful
     */
    public OrgCreateEventInteractor(EventDsGateway eventDsGateway,
                                    OrgDsGateway orgDsGateway,
                                    OrgCreateEventOutputBoundary orgCreateEventOutputBoundary) {
        this.eventDsGateway = eventDsGateway;
        this.orgDsGateway = orgDsGateway;
        this.orgCreateEventOutputBoundary = orgCreateEventOutputBoundary;
    }

    /**Use the information contained in requestModel to create a new event and return a responseModel.
     * It checks if all entries are non-empty: title, description, year, month, day, hour, minute, and location.
     * It checks if the following entries are too long: title, description, and location.
     * It checks if the title already exists in the event database.
     * It checks if all time entries are valid: year, month, day, hour, and minute.
     * It checks if the time is set in the future.
     * If any check fails, return a failure response.
     * Otherwise, a success response is returned.
     *
     * @param requestModel The request model sent to the interactor
     * @return A responseModel representing whether the event creation is successful
     */
    @Override
    public OrgCreateEventResponseModel create(OrgCreateEventRequestModel requestModel) throws SQLException, ClassNotFoundException {
        // Checks if all entries are non-empty: title, description, year, month, day, hour, minute, location.
        if (requestModel.getTitle().isEmpty() || requestModel.getDescription().isEmpty()
                || requestModel.getYear().isEmpty() || requestModel.getMonth().isEmpty()
                || requestModel.getDay().isEmpty() || requestModel.getHour().isEmpty()
                || requestModel.getMinute().isEmpty() || requestModel.getLocation().isEmpty()) {
            return orgCreateEventOutputBoundary.prepareFailView("Entries cannot be empty.");
        }

        // Checks if title already exists.
        if (eventDsGateway.checkIfEventNameExist(requestModel.getTitle())) {
            return orgCreateEventOutputBoundary.prepareFailView("Title already exists.");
        }

        // Checks if title is too long (over 20 characters).
        if (requestModel.getTitle().length() > 20) {
            return orgCreateEventOutputBoundary.prepareFailView("Title should be no longer than 20 characters.");
        }

        // Checks if description is too long (over 200 characters).
        if (requestModel.getDescription().length() > 200) {
            return orgCreateEventOutputBoundary.prepareFailView("Description should be no longer than 200 characters.");
        }

        // Checks if location is too long (over 50 characters)
        if (requestModel.getLocation().length() > 50) {
            return orgCreateEventOutputBoundary.prepareFailView("Location should be no longer than 50 characters.");
        }

        String year = requestModel.getYear();
        String month = requestModel.getMonth();
        String day = requestModel.getDay();
        String hour = requestModel.getHour();
        String minute = requestModel.getMinute();

        // Checks if all time entries can be converted to integer
        if (isStringInt(year) && isStringInt(month) && isStringInt(day) && isStringInt(hour) && isStringInt(minute)) {

            // Checks if year is exactly 4 digits
            if (year.length() != 4) {
                return orgCreateEventOutputBoundary.prepareFailView("Year is not 4 digits.");
            }
            int y = Integer.parseInt(year);

            // Checks if month is valid (from 1 to 12, inclusive)
            int m = Integer.parseInt(month);
            if (m > 12 || m <= 0) {
                return orgCreateEventOutputBoundary.prepareFailView("Month is not within 1 to 12.");
            }

            // Checks is day is valid (from 1 to 31, inclusive)
            int d = Integer.parseInt(day);
            if (d > 31 || d <= 0) {
                return orgCreateEventOutputBoundary.prepareFailView("Day is not within 1 to 31.");
            }

            // Checks if hour is valid (from 1 to 23, inclusive)
            int h = Integer.parseInt(hour);
            if (h > 23 || h < 0) {
                return orgCreateEventOutputBoundary.prepareFailView("Hour is not within 0 to 23.");
            }

            // CHecks is minute is valid (from 0 to 59, inclusive)
            int min = Integer.parseInt(minute);
            if (min > 59 || min < 0) {
                return orgCreateEventOutputBoundary.prepareFailView("Minute is not within 0 to 59.");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime time = LocalDateTime.of(y, m, d, h, min);

            // Checks if the time is set in the future.
            if (time.isBefore(now)){
                return orgCreateEventOutputBoundary.prepareFailView("Time must be in future.");
            }

            // Call orgDsGateway to create the event and return the responseModel with the event's title
            else {
                orgDsGateway.createAnEvent(requestModel.getOrgUsername(), requestModel.getTitle(), 0,
                        requestModel.getDescription(), requestModel.getLocation(), y, m, d, h, min);
                OrgCreateEventResponseModel responseModel = new OrgCreateEventResponseModel(requestModel.getTitle());
                return orgCreateEventOutputBoundary.prepareSuccessView(responseModel);
            }
        }

        else { return orgCreateEventOutputBoundary.prepareFailView("Time entry/ies is/are not integer."); }
    }

    /**A method used to check time entries format
     *
     * @param s A string of a time entry.
     * @return A boolean representing whether the time entry is valid.
     */
    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}