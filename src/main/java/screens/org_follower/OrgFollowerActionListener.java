package screens.org_follower;

import screens.org_home.OrgHomePage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrgFollowerActionListener implements ActionListener {
    public OrgFollowerPage orgFollowerPage;

    public OrgFollowerActionListener(OrgFollowerPage orgFollowerPage){
        //Set the follower page as instance
        this.orgFollowerPage = orgFollowerPage;
    }

    public void actionPerformed(ActionEvent arg0){
        String actionCommand = arg0.getActionCommand();
        //Situations if the clicked button is "Back"
        if (actionCommand.equals("Back")) {

            this.orgFollowerPage.dispose();
            new OrgHomePage(this.orgFollowerPage.getOrgUsername());
        }
        else {
        }
    }
}
