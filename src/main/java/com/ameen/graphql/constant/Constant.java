package com.ameen.graphql.constant;

public class Constant {


    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final long ACCESS_TOKEN_EXPIRATION = 15 * 60L * 1000;
    public static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60L * 1000;
    public static final String SUCCESS = "Success.";

    public static final String USER_EMAIL_ALREADY_EXIST = "Email already exists.";
    public static final String USER_NOT_FOUND = "User Id Not Found.";
    public static final String PASSWORD_UPDATED = "Password has been updated successfully.";
    public static final String USER_ID_FOUND ="User id found successfully." ;
    public static final String USER_DELETED = "USer Id Deleted Successfully.";
    public static final String INVALID_PASSWORD = "Invalid password.";
    public static final String INVALID_OLD_PASSWORD = "Invalid old password.";
    public static final String INVALID_EMAIL = "Invalid Email.";
    public static final String PASSWORD_RESET = "Password reset successfully.";

    public static final String ROLE_ALREADY_EXIST = "Role Name Already Exist.";
    public static final String ROLE_ID_FOUND = "Role Id found Successfully.";
    public static final String ROLE_NOT_FOUND = "Role Id Not Found.";
    public static final String ROLE_UPDATED = "Role Updated Successfully.";
    public static final String ROLE_CREATED = "Role Created Successfully.";
    public static final String ROLE_DELETED = "Role Deleted Successfully.";
    public static final String ROLE_ALREADY_INACTIVE = "Role is Already Inactive.";
    public static final String ROLE_VALID_ID = "Please Enter Valid Role Id.";

    public static final String MESSAGE_UPDATED = "Message Updated Successfully";
    public static final String MESSAGE_NOT_FOUND = "Message Not found";
    public static final String EDITED_TIME_EXCEEDED = "Message Edited time Exceeded";

}
