public interface ProfileRepository {
    Optional<Profile> findByUserId(String userId);
    Profile save(Profile profile);
    void deleteByUserId(String userId);
}
