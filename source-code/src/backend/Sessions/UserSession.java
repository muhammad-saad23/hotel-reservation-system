package backend.Sessions;

import backend.Roles.*;

public class UserSession {
    private static int loggedUserId;
    private static String loggedUserName;
    private static String loggedUserEmail;
    private static String loggedUserPhone;
    private static String loggedUserRole;

    public static void setSession(int id,String name, String email,String phone, String role) {
        loggedUserId=id;
        loggedUserName = name;
        loggedUserEmail = email;
        loggedUserPhone=phone;
        loggedUserRole = role;
    }

    public static int getLoggedUserId() {
        return loggedUserId;
    }

    public static String getLoggedUserName() {
        return loggedUserName;
    }

    public static String getLoggedUserEmail() {
        return loggedUserEmail;
    }

    public static String getLoggedUserPhone() {
        return loggedUserPhone;
    }

    public static String getLoggedUserRole() {
        return loggedUserRole;
    }

    public static void clearSession(){
        loggedUserName=null;
        loggedUserEmail=null;
        loggedUserPhone=null;
        loggedUserRole=null;
    }
}
