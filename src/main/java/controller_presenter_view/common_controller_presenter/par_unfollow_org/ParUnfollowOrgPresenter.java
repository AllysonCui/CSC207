package controller_presenter_view.common_controller_presenter.par_unfollow_org;

import use_cases.par_unfollow_org_use_case.ParUnfollowOrgOutputBoundary;
import use_cases.par_unfollow_org_use_case.ParUnfollowOrgResponseModel;

public class ParUnfollowOrgPresenter implements ParUnfollowOrgOutputBoundary {

    public ParUnfollowOrgResponseModel prepareSuccessPage(ParUnfollowOrgResponseModel responseModel) {

        responseModel.setMessage("You have unfollowed " +responseModel.getOrgName() + ".");
        return responseModel;

    }
}
