package controller_presenter_view.screens.org_upcoming_event;

import database.*;
import use_cases.notify_event_use_case.NotifyEventInputBoundary;
import use_cases.notify_event_use_case.NotifyEventInteractor;
import use_cases.notify_event_use_case.NotifyEventOutputBoundary;
import use_cases.notify_event_use_case.NotifyEventResponseModel;
import use_cases.org_delete_event_use_case.OrgDeleteEventInputBoundary;
import use_cases.org_delete_event_use_case.OrgDeleteEventInteractor;
import use_cases.org_delete_event_use_case.OrgDeleteEventOutputBoundary;
import use_cases.org_delete_event_use_case.OrgDeleteEventResponseModel;
import controller_presenter_view.common_view.EventDetailsPage;
import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventController;
import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventPresenter;
import controller_presenter_view.common_controller_presenter.org_delete_event.OrgDeleteEventController;
import controller_presenter_view.common_controller_presenter.org_delete_event.OrgDeleteEventPresenter;
import controller_presenter_view.screens.org_home.OrgHomePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OrgUpcomingEventActionListener implements ActionListener {
    public OrgUpcomingEventPage orgUpcomingEventPage;
    public String orgUsername;

    public OrgUpcomingEventActionListener(OrgUpcomingEventPage orgUpcomingEventPage, String orgUsername){
        this.orgUpcomingEventPage = orgUpcomingEventPage;
        this.orgUsername = orgUsername;
    }

    public void actionPerformed(ActionEvent arg0){
        String actionCommand = arg0.getActionCommand();

        if (actionCommand.equals("Back")) {
            this.orgUpcomingEventPage.dispose();
            new OrgHomePage(this.orgUpcomingEventPage.getOrgUsername());
        }
        else if (actionCommand.contains("Notify")) {
            EventDsGateway eventDsGateway = new EventFileUser();

            ParDsGateway parDsGateway = new ParFileUser();

            NotifyEventOutputBoundary notifyEventOutputBoundary = new NotifyEventPresenter();

            NotifyEventInputBoundary interactor = new NotifyEventInteractor(eventDsGateway, parDsGateway, notifyEventOutputBoundary);

            NotifyEventController notifyEventController = new NotifyEventController(interactor);

            String eventName = actionCommand.substring(0,actionCommand.length()-6);

            try {
                NotifyEventResponseModel responseModel =
                        notifyEventController.sendNotification("Future", eventName);
                JOptionPane.showMessageDialog(this.orgUpcomingEventPage, responseModel.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this.orgUpcomingEventPage, e.getMessage());
            }
        }
        else if (actionCommand.contains("Delete")) {
            EventDsGateway eventDsGateway = new EventFileUser();

            OrgDsGateway orgDsGateway = new OrgFileUser();

            ParDsGateway parDsGateway = new ParFileUser();

            OrgDeleteEventOutputBoundary orgDeleteEventOutputBoundary = new OrgDeleteEventPresenter();

            OrgDeleteEventInputBoundary interactor = new OrgDeleteEventInteractor(eventDsGateway, orgDsGateway,
                    parDsGateway, orgDeleteEventOutputBoundary);

            OrgDeleteEventController orgDeleteEventController = new OrgDeleteEventController(interactor);

            String eventName = actionCommand.substring(0,actionCommand.length()-6);

            try{
                OrgDeleteEventResponseModel responseModel = orgDeleteEventController.delete(eventName);
                JOptionPane.showMessageDialog(this.orgUpcomingEventPage, responseModel.getMessage());
            } catch(Exception e) {
                JOptionPane.showMessageDialog(this.orgUpcomingEventPage, e.getMessage());
            }
            this.orgUpcomingEventPage.dispose();
            try {
                new OrgUpcomingEventPage(this.orgUsername);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                new EventDetailsPage(actionCommand);
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}