package user_reset_password_use_case;

import java.sql.SQLException;

public interface UserResetPasswordInputBoundary {

    /** Use the information contained in the requestmodel can let user reset their password and responsemodel.
     *
     * @param requestModel The request model sent to the input boundary
     * @return A responsemodel representing whether the user resetPassword is successful.
     */
    UserResetPasswordResponseModel resetPassword(UserResetPasswordRequestModel requestModel) throws SQLException, ClassNotFoundException;
}