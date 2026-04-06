package library_db;

public class Session {
    public static int loggedInUserId = -1;
    public static String loggedInUsername = null;
    public static String loggedInRole = null;

    public static void clear() {
        loggedInUserId = -1;
        loggedInUsername = null;
        loggedInRole = null;
    }
}
