package session;


public final class UserSession {
    private static Integer currentUserId;
    private static String currentUserEmail;

    private UserSession() {
    }

    public static void login(int userId, String email) {
        currentUserId = userId;
        currentUserEmail = email;
    }

    public static void logout() {
        currentUserId = null;
        currentUserEmail = null;
    }

    public static boolean isLoggedIn() {
        return currentUserId != null;
    }

    public static int requireUserId() {
        if (currentUserId == null) {
            throw new IllegalStateException("Tidak ada akun yang sedang login.");
        }
        return currentUserId;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail == null ? "-" : currentUserEmail;
    }
}
