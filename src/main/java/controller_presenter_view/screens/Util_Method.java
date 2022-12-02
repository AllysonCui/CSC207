package controller_presenter_view.screens;

import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventController;
import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventPresenter;
import controller_presenter_view.common_controller_presenter.org_delete_event.OrgDeleteEventController;
import controller_presenter_view.common_controller_presenter.org_delete_event.OrgDeleteEventPresenter;
import controller_presenter_view.common_controller_presenter.upcoming_to_past.UpcomingToPastController;
import controller_presenter_view.common_controller_presenter.upcoming_to_past.UpcomingToPastPresenter;
import database.*;
import use_cases.notify_event_use_case.NotifyEventInputBoundary;
import use_cases.notify_event_use_case.NotifyEventInteractor;
import use_cases.notify_event_use_case.NotifyEventOutputBoundary;
import use_cases.org_delete_event_use_case.OrgDeleteEventInputBoundary;
import use_cases.org_delete_event_use_case.OrgDeleteEventInteractor;
import use_cases.org_delete_event_use_case.OrgDeleteEventOutputBoundary;
import use_cases.upcoming_to_past_use_case.UpcomingToPastInputBoundary;
import use_cases.upcoming_to_past_use_case.UpcomingToPastInteractor;
import use_cases.upcoming_to_past_use_case.UpcomingToPastOutputBoundary;
import use_cases.upcoming_to_past_use_case.UpcomingToPastResponseModel;

public class Util_Method {

    public static void convertAndNotify(String userType, String orgUsername){
        //convert upcoming events to past events if their time is in the past
        ParDsGateway parDsGateway = new ParFileUser();
        OrgDsGateway orgDsGateway = new OrgFileUser();
        EventDsGateway eventDsGateway = new EventFileUser();
        UpcomingToPastOutputBoundary upcomingToPastOutputBoundary = new UpcomingToPastPresenter();
        UpcomingToPastInputBoundary interactor = new UpcomingToPastInteractor(parDsGateway, orgDsGateway,
                eventDsGateway, upcomingToPastOutputBoundary);
        UpcomingToPastController controller = new UpcomingToPastController(interactor);
        UpcomingToPastResponseModel responseModel;
        try {
            responseModel = controller.convertToPast(userType, orgUsername);
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }

        //notify the relevant participant of the events
        if (!responseModel.getEventsToPast().isEmpty()) {
            NotifyEventOutputBoundary notifyEventOutputBoundary = new NotifyEventPresenter();
            NotifyEventInputBoundary interactor2 = new NotifyEventInteractor(eventDsGateway, parDsGateway,
                    notifyEventOutputBoundary);
            NotifyEventController notifyEventController = new NotifyEventController(interactor2);
            for (String event : responseModel.getEventsToPast()){
                try {
                    notifyEventController.sendNotification("Past", event);
                } catch (ClassNotFoundException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
    }

    public static OrgDeleteEventController utilGetDeleteEventControllerHelper(){
        EventDsGateway eventDsGateway = new EventFileUser();

        ParDsGateway parDsGateway = new ParFileUser();

        OrgDeleteEventOutputBoundary orgDeleteEventOutputBoundary = new OrgDeleteEventPresenter();

        OrgDeleteEventInputBoundary interactor = new OrgDeleteEventInteractor(eventDsGateway,
                parDsGateway, orgDeleteEventOutputBoundary);

        return new OrgDeleteEventController(interactor);
    }
}
