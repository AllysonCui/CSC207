package controller_presenter_view.common_view;


import controller_presenter_view.common_controller_presenter.extract_information.ExtractInfoController;
import database.EventDsGateway;
import database.EventFileUser;
import use_cases.extract_information_use_case.ExtractInfoInputBoundary;
import use_cases.extract_information_use_case.ExtractInfoInteractor;
import use_cases.extract_information_use_case.ExtractInfoResponseModel;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static tutorial.HelloWorld.getConstantX;
import static tutorial.HelloWorld.getConstantY;




public class EventDetailsPage extends JFrame {

    String eventTitle;

    /** When this constructor is called it will generate an event details page for the event of interest using the
     * eventTitle parameter. The page will contain information such as the event's name, description, time, and location.
     *
     * @param eventTitle The title for the event we want to get the details page for
     */
    public EventDetailsPage(String eventTitle) throws SQLException, ClassNotFoundException {

        EventDsGateway e= new EventFileUser();
        this.eventTitle = eventTitle;
        this.setSize(getConstantX() - 300, getConstantY() - 500);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        //Creates title
        JLabel title = new JLabel("Event Details");
        title.setBounds(0, 0, getConstantX() - 300, 50);
        title.setHorizontalAlignment(JLabel.CENTER);


        //Adds text for event name
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel eventName = new JLabel("Event Title: " + this.eventTitle);

        ExtractInfoInputBoundary interactor = new ExtractInfoInteractor(e);
        ExtractInfoController controller = new ExtractInfoController(interactor);
        ExtractInfoResponseModel<String> response1= controller.extractEvent("getDescription",this.eventTitle);

        //Adds text for event description
        JLabel description = new JLabel("Description: " + response1.getStr());

        ExtractInfoResponseModel<Integer> response2= controller.extractEventTime(this.eventTitle);

        //Adds text for event time
        ArrayList<Integer> times = response2.getAl();
        JLabel time = new JLabel("Time: " + times.get(0) + " " + times.get(1) + "-" +
                times.get(2) +" "+times.get(3) + ":" + times.get(4));

        ExtractInfoInputBoundary interactor3= new ExtractInfoInteractor(e);
        ExtractInfoController controller3= new ExtractInfoController(interactor3);
        ExtractInfoResponseModel<String> response3= controller3.extractEvent("getLocation",this.eventTitle);

        //Adds text for event location
        JLabel location = new JLabel("Location: "+response3.getStr());
        panel.add(eventName);
        panel.add(description);
        panel.add(time);
        panel.add(location);
        panel.setBounds(50, 70, getConstantX() - 300, getConstantY() - 500);


        this.add(title);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

    }


}