public class ProfileService {

    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;

    public ProfileService(UserRepository userRepo, ProfileRepository profileRepo) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    public Profile getProfile(String userId) {
        return profileRepo.findByUserId(userId).orElseThrow();
    }

    public Profile updateProfile(String userId, Profile updatedProfile) {
        Profile existing = profileRepo.findByUserId(userId).orElseThrow();
        existing.setPhoneNumber(updatedProfile.getPhoneNumber());
        existing.setAddress(updatedProfile.getAddress());
        existing.setDateOfBirth(updatedProfile.getDateOfBirth());
        return profileRepo.save(existing);
    }

    public void deleteAccount(String userId) {
        userRepo.deleteById(userId);
        profileRepo.deleteByUserId(userId);
        // Notifikasi dan log audit bisa ditambahkan di sini
    }
}
