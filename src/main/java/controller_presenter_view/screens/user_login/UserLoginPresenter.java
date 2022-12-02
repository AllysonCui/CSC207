package controller_presenter_view.screens.user_login;

import controller_presenter_view.common_view.ShowMessageView;
import use_cases.user_login_use_case.UserLoginOutputBoundary;
import use_cases.user_login_use_case.UserLoginResponseModel;

// Interface adapters layer

public class UserLoginPresenter implements UserLoginOutputBoundary {
    @Override
    public UserLoginResponseModel prepareFailView(String error) {
        System.out.println("a");
        throw new ShowMessageView(error);
    }
}