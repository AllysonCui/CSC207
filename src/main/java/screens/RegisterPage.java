package screens;

import database.OrgDsGateway;
import database.OrgFileUser;
import database.ParDsGateway;
import database.ParFileUser;
import screens.org_home.OrgHomePresenter;
import screens.par_home.ParHomePresenter;
import user_login_use_case.*;
import user_register_use_case.UserRegisterResponseModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JFrame implements ActionListener {

    JTextField username = new JTextField(15);

    JPasswordField password = new JPasswordField(15);
    
    JPasswordField retypePassword = new JPasswordField(15);

    UserRegisterController userRegisterController;

    boolean P = false;
    boolean O = false;

    int x = 500;
    int y = 500;

    /**The method generate a registration page.
     * It contains button to choose the user type for registration.
     * It allows user to input username, password and second-input password.
     * The controller process the information after use clicks 'Register' button, it calls actionPerformed method.
     *
     * @param controller UserRegisterController that takes information got from the page
     */
    public RegisterPage(UserRegisterController controller) {

        this.setLayout(null);

        this.setSize(x,y);

        this.setLocationRelativeTo(null);

        this.userRegisterController = controller;
        JLabel title = new JLabel("Register Screen");
        title.setBounds (0,0, x, 50);
        title.setHorizontalAlignment(JLabel.CENTER);

        JRadioButton parButton = new JRadioButton("Participant");
        parButton.setActionCommand("P");
        JRadioButton orgButton = new JRadioButton("Organization");
        orgButton.setActionCommand("O");

        parButton.addActionListener(actionEvent -> {
            if (O) { P = !P; O = false; }
            else {P = true;}
        });
        orgButton.addActionListener(actionEvent -> {
            if (P) { O = !O; P = false; }
            else {O = true;}
        });

        JButton register = new JButton("Register");
        JButton cancel = new JButton("Cancel");
        JButton login = new JButton("To Login Page");

        ButtonGroup userType = new ButtonGroup();
        userType.add(parButton);
        userType.add(orgButton);

        JPanel typeInfo = new JPanel();
        typeInfo.add(parButton);
        typeInfo.add(orgButton);
        typeInfo.setBounds (0,50, x, 50);

        LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), username);
        usernameInfo.setBounds (0,100, x, 50);

        LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), password);
        passwordInfo.setBounds (0,150, x, 50);

        LabelTextPanel retypePasswordInfo = new LabelTextPanel(
                new JLabel("Retype Password"), retypePassword);
        retypePasswordInfo.setBounds (0,200, x, 50);

        JPanel buttons = new JPanel();

        buttons.add(login);
        buttons.add(register);
        buttons.add(cancel);
        buttons.setBounds (0,250, x, 50);

        register.addActionListener(this);

        login.addActionListener(new RegisterPageActionListener(this));

        cancel.addActionListener(new RegisterPageActionListener(this));

        this.add(title);
        this.add(typeInfo);
        this.add(usernameInfo);
        this.add(passwordInfo);
        this.add(retypePasswordInfo);
        this.add(buttons);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);

    }

    /**The method got information needed to input from the page.
     * The method generate Presenter , DsGateWays, interactor and controller to process them.
     * Once the information are passed to the above components, jump to login page.
     *
     * @param e the registration event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            UserRegisterResponseModel responseModel = userRegisterController.create(
                    P?"P":"",
                    O?"O":"",
                    username.getText(),
                    String.valueOf(password.getPassword()),
                    String.valueOf(retypePassword.getPassword()));
            JOptionPane.showMessageDialog(this, responseModel.getMessage());
            this.dispose();
            UserLoginOutputBoundary userLoginOutputBoundary =  new UserLoginPresenter();

            ParDsGateway parDsGateway = new ParFileUser();

            ParHomeOutputBoundary parHomeOutputBoundary =  new ParHomePresenter();

            OrgDsGateway orgDsGateway= new OrgFileUser();

            OrgHomeOutputBoundary orgHomeOutputBoundary =  new OrgHomePresenter();

            UserLoginInputBoundary interactor = new UserLoginInteractor(
                    userLoginOutputBoundary, parDsGateway, parHomeOutputBoundary, orgDsGateway, orgHomeOutputBoundary);

            UserLoginController userLoginController = new UserLoginController(interactor);

            new LoginPage(userLoginController);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

}
