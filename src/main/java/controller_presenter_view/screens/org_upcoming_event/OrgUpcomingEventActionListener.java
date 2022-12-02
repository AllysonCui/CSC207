package controller_presenter_view.screens.org_upcoming_event;

import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventController;
import controller_presenter_view.common_controller_presenter.notify_event.NotifyEventPresenter;
import controller_presenter_view.common_controller_presenter.org_delete_event.OrgDeleteEventController;
import controller_presenter_view.common_view.EventDetailsPage;
import controller_presenter_view.screens.org_home.OrgHomePage;
import database.EventDsGateway;
import database.EventFileUser;
import database.ParDsGateway;
import database.ParFileUser;
import use_cases.Util_Method;
import use_cases.notify_event_use_case.NotifyEventInputBoundary;
import use_cases.notify_event_use_case.NotifyEventInteractor;
import use_cases.notify_event_use_case.NotifyEventOutputBoundary;
import use_cases.notify_event_use_case.NotifyEventResponseModel;
import use_cases.org_delete_event_use_case.OrgDeleteEventResponseModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrgUpcomingEventActionListener implements ActionListener {
    public final OrgUpcomingEventPage orgUpcomingEventPage;
    public final String orgUsername;

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
        else {
            final String substring = actionCommand.substring(0, actionCommand.length() - 6);
            if (actionCommand.contains("Notify")) {
                EventDsGateway eventDsGateway = new EventFileUser();
    
                ParDsGateway parDsGateway = new ParFileUser();
    
                NotifyEventOutputBoundary notifyEventOutputBoundary = new NotifyEventPresenter();
    
                NotifyEventInputBoundary interactor = new NotifyEventInteractor(eventDsGateway, parDsGateway, notifyEventOutputBoundary);
    
                NotifyEventController notifyEventController = new NotifyEventController(interactor);

                try {
                    NotifyEventResponseModel responseModel =
                            notifyEventController.sendNotification("Future", substring);
                    JOptionPane.showMessageDialog(this.orgUpcomingEventPage, responseModel.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this.orgUpcomingEventPage, e.getMessage());
                }
            }
            else if (actionCommand.contains("Delete")) {
                OrgDeleteEventController orgDeleteEventController = Util_Method.utilGetDeleteEventControllerHelper();

                try{
                    OrgDeleteEventResponseModel responseModel = orgDeleteEventController.delete(substring);
                    JOptionPane.showMessageDialog(this.orgUpcomingEventPage, responseModel.getMessage());
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(this.orgUpcomingEventPage, e.getMessage());
                }
                this.orgUpcomingEventPage.dispose();
                try {
                    new OrgUpcomingEventPage(this.orgUsername);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    new EventDetailsPage(actionCommand);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}