package user_login_use_case;


public class UserLoginInteractor implements UserLoginInputBoundary {

    private UserLoginPresenter userLoginPresenter;
    private ParDsGateway parDsGateway;
    private ParHomePresenter parHomePresenter;
    private OrgDsGateway orgDsGateway;
    private OrgHomePresenter orgHomePresenter;

    public UserLoginInteractor(ParDsGateway parDsGateway, ParHomePresenter parHomePresenter) {
        this.parDsGateway = parDsGateway;
        this.parHomePresenter = parHomePresenter;
    }

    public UserLoginInteractor(OrgDsGateway orgDsGateway, OrgHomePresenter orgHomePresenter) {
        this.orgDsGateway = orgDsGateway;
        this.orgHomePresenter = orgHomePresenter;
    }
    
    public UserLoginResponseModel login(UserLoginRequestModel requestModel) {
        if (requestModel.getUserType().equals("P")) {
            String username = requestModel.getUsername();
            if (!parDsGateway.existsByUsername(username)) {
                return userLoginPresenter.prepareFailView("Participant does not exist.");
            } else if (!parDsGateway.getPassword(username).equals(requestModel.getPassword())) {
                return userLoginPresenter.prepareFailView("Password doesn't match.");
            }

            UserLoginResponseModel accountResponseModel = new UserLoginResponseModel(username,
                    parDsGateway.getNotification(username));
            return parHomePresenter.prepareHomePageView(accountResponseModel);
        }
        else if (requestModel.getUserType().equals("O")) {
            String username = requestModel.getUsername();
            if (!orgDsGateway.existsByUsername(username)) {
                return userLoginPresenter.prepareFailView("Organization does not exist.");
            } else if (!orgDsGateway.getPassword(username).equals(requestModel.getPassword())) {
                return userLoginPresenter.prepareFailView("Password doesn't match.");
            }

            UserLoginResponseModel accountResponseModel = new UserLoginResponseModel(username);
            return orgHomePresenter.prepareHomePageView(accountResponseModel);
        }
        else {
            return userLoginPresenter.prepareFailView("Please select your account type.");
        }
    }
}