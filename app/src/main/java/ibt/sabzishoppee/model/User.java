package ibt.sabzishoppee.model;


import ibt.sabzishoppee.model.login_responce.LoginModel;

/**
 * Created by Natraj on 7/11/2017.
 */

public class User {

    public static LoginModel user;

    public static LoginModel getUser() {
        return user;
    }

    public static void setUser(LoginModel user) {
        User.user = user;
    }
}