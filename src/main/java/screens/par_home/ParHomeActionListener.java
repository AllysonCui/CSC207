package screens.par_home;

import database.*;
import org_search_use_case.*;
import event_search_use_case.*;
import screens.LoginPage;
import screens.org_home.OrgHomeResponseFormatter;
import screens.UserLoginController;
import screens.UserLoginResponseFormatter;
import screens.par_account.ParAccountPage;
import screens.par_followed_org.ParFollowedOrgPage;
import screens.par_past_event.ParPastEventPage;
import screens.par_upcoming_event.ParUpcomingEventPage;
import screens.upcoming_to_past.UpcomingToPastController;
import screens.upcoming_to_past.UpcomingToPastResponseFormatter;
import upcoming_to_past_use_case.UpcomingToPastInputBoundary;
import upcoming_to_past_use_case.UpcomingToPastInteractor;
import upcoming_to_past_use_case.UpcomingToPastPresenter;
import upcoming_to_past_use_case.UpcomingToPastResponseModel;
import user_login_use_case.*;
import screens.par_search.*;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ParHomeActionListener implements ActionListener {
    public ParHomePage parHomePage;

    public ParHomeActionListener(ParHomePage parHomePage) {
        this.parHomePage = parHomePage;
    }


    public void actionPerformed(ActionEvent arg0) {
        String page = arg0.getActionCommand();

        if (page.equals("Account")) {
            this.parHomePage.dispose();
            new ParAccountPage(this.parHomePage.getParUsername());
        } else if (page.equals("Upcoming Event")) {
            this.parHomePage.dispose();
            try {
                new ParUpcomingEventPage(this.parHomePage.getParUsername());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            ParDsGateway parDsGateway = new ParFileUser();
            OrgDsGateway orgDsGateway = new OrgFileUser();
            EventDsGateway eventDsGateway = new EventFileUser();
            UpcomingToPastPresenter upcomingToPastPresenter = new UpcomingToPastResponseFormatter();
            UpcomingToPastInputBoundary interactor = new UpcomingToPastInteractor(parDsGateway, orgDsGateway,
                    eventDsGateway, upcomingToPastPresenter);
            UpcomingToPastController controller = new UpcomingToPastController(interactor);
            UpcomingToPastResponseModel responseModel = null;
            try {
                responseModel = controller.convertToPast("P",
                        this.parHomePage.getParUsername());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (!responseModel.getEventsToPast().isEmpty()) {
                JOptionPane.showMessageDialog(this.parHomePage, responseModel.getMessage());
            }
        } else if (page.equals("Past Event")) {
            this.parHomePage.dispose();
            try {
                new ParPastEventPage(this.parHomePage.getParUsername());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            ParDsGateway parDsGateway = new ParFileUser();
            OrgDsGateway orgDsGateway = new OrgFileUser();
            EventDsGateway eventDsGateway = new EventFileUser();
            UpcomingToPastPresenter upcomingToPastPresenter = new UpcomingToPastResponseFormatter();
            UpcomingToPastInputBoundary interactor = new UpcomingToPastInteractor(parDsGateway, orgDsGateway,
                    eventDsGateway, upcomingToPastPresenter);
            UpcomingToPastController controller = new UpcomingToPastController(interactor);
            UpcomingToPastResponseModel responseModel = null;
            try {
                responseModel = controller.convertToPast("P",
                        this.parHomePage.getParUsername());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (!responseModel.getEventsToPast().isEmpty()) {
                JOptionPane.showMessageDialog(this.parHomePage, responseModel.getMessage());
            }
        } else if (page.equals("Followed Org")) {
            this.parHomePage.dispose();
            try {
                new ParFollowedOrgPage(this.parHomePage.getParUsername());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (page.equals("Search")) {
            if (this.parHomePage.org.isSelected()) {
                try {
                    OrgDsGateway org = new OrgFileUser();
                    OrgSearchOutputBoundary presenter = new OrgSearchPresenter();
                    OrgSearchInputBoundary interactor = new OrgSearchInteractor(org, presenter);
                    OrgSearchController controller = new OrgSearchController(interactor);
                    String query = this.parHomePage.searchBox.getText();
                    String parUserName = this.parHomePage.getParUsername();
                    try {
                        controller.orgSearch(query, parUserName); //draw screen
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    this.parHomePage.dispose();
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(this.parHomePage, error.getMessage());
                }

            } else {
                try {
                    EventDsGateway eve = new EventFileUser();
                    EventSearchOutputBoundary presenter = new EventSearchPresenter(); //minor issue
                    EventSearchInputBoundary interactor = new EventSearchInteractor(eve, presenter);
                    EventSearchController controller = new EventSearchController(interactor);
                    String query = this.parHomePage.searchBox.getText();
                    String parUserName = this.parHomePage.getParUsername();
                    try {
                        controller.eventSearch(query, parUserName);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    this.parHomePage.dispose();
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(this.parHomePage, error.getMessage());
                }
            }


        } else {
            this.parHomePage.dispose();
            UserLoginPresenter userLoginPresenter = new UserLoginResponseFormatter();

            ParDsGateway parDsGateway = new ParFileUser();

            ParHomePresenter parHomePresenter = new ParHomeResponseFormatter();

            OrgDsGateway orgDsGateway = new OrgFileUser();

            OrgHomePresenter orgHomePresenter = new OrgHomeResponseFormatter();

            UserLoginInputBoundary interactor = new UserLoginInteractor(
                    userLoginPresenter, parDsGateway, parHomePresenter, orgDsGateway, orgHomePresenter);

            UserLoginController userLoginController = new UserLoginController(interactor);

            new LoginPage(userLoginController);
        }
    }
}

