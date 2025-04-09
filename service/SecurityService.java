public class SecurityService {
    public boolean authenticate(String token) {
        // cek token
        return true;
    }

    public boolean authorize(String userId, String tokenUserId) {
        return userId.equals(tokenUserId);
    }
}
