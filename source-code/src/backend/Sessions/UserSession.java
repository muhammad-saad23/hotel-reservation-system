package backend.Sessions;

public class UserSession {
    private static int loggedUserId = 0;
    private static String loggedUserName;
    private static String loggedUserEmail;
    private static String loggedUserPhone;
    private static String loggedUserRole;
    private static String loggedUserPermissions;

    public static void setSession(int id, String name, String email, String phone, String role, String permissions) {
        loggedUserId = id;
        loggedUserName = name;
        loggedUserEmail = email;
        loggedUserPhone = phone;
        loggedUserRole = role;
        loggedUserPermissions = permissions;
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

    public static String getLoggedUserPermissions() {
        return loggedUserPermissions;
    }

    public static boolean isLoggedIn() {
        return loggedUserId > 0;
    }

    public static void clearSession() {
        loggedUserId = 0;
        loggedUserName = null;
        loggedUserEmail = null;
        loggedUserPhone = null;
        loggedUserRole = null;
        loggedUserPermissions = null;
    }
}