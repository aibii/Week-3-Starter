package comp74.week2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Model {
    private static final Logger logger = LoggerFactory.getLogger(Model.class);

    HashMap<Integer, Profile> profiles;
    HashMap<Integer, Posting> postings;
    HashMap<String, Profile> userMap;

    private static Integer nextProfileId = 100;
    private static Integer nextPostingId = 100;

    public Model() {
        super();
        profiles = new HashMap<>();
        userMap = new HashMap<>();
        postings = new HashMap<>();
    }

    // POST
    public Posting addPosting(Profile profile, Posting posting) { //peofile that is gonna create posting and posting itself
        posting.setPostingId(nextPostingId++); //auto increment posting id
        posting.setDate(LocalDateTime.now());
        posting.setUserName(profile.getUserName()); //set the username of posting
        postings.put(posting.getPostingId(), posting); //then we add posting to collection of postings
        profile.addPosting(posting);
        return posting;
    }

    public Profile addProfile(Profile profile) { //public ResponseEntity<Profile> addProfile(Profile profile)
        Profile newProfile = null;
        if (userMap.get(profile.getUserName()) == null) { //if we get null, - username is new
            newProfile = profile;
            newProfile.setProfileId(nextProfileId++);
            profiles.put(newProfile.getProfileId(), newProfile); //we add profile to collection of profiles
            userMap.put(newProfile.getUserName(), newProfile);
        }
        return newProfile;  //return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
    }

    // GET ALL

    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles.values());
    }

    public List<Posting> getPostings() {
        return new ArrayList<>(postings.values());
    }

    // GET BY ???

    public Profile getProfileById(Integer id) {
        Profile profile = profiles.get(id);
        return profile;
    }

    public List<Profile> getProfilesByUserName(String userName) {
        Profile profile = userMap.get(userName);
        return new ArrayList<>(Arrays.asList(profile));
    }

    public Profile getProfileByUserName(String userName) {
        Profile profile = userMap.get(userName);
        return profile;
    }

    //PUT 
    public Posting updatePosting(Profile profile, Integer postingId, Posting posting) {
        Posting newPosting = null;
        if (postings.get(postingId) != null) { //checks if posting exists
            newPosting = posting; //newPosting is the posting that we get from the request
            newPosting.setPostingId(postingId); //set posting id
            newPosting.setDate(LocalDateTime.now()); //set date
            newPosting.setUserName(profile.getUserName()); //set username
            postings.put(postingId, newPosting); //add posting to collection of postings
        }
        return newPosting; //return new ResponseEntity<>(newPosting, HttpStatus.OK);
    }

    /*
     * This method deletes a posting with given postingId from the model.
     * Retrieve the profile using the provided profileId.
     * If the profile exists, iterate through the profile's postings.
     * Remove all postings that match the given postingId from the profile's postings list.
     * Also remove the posting from the global postings collection if any postings were removed.
     * Log the action, indicating either successful deletion or that the posting/profile was not found.
     * Return true if any postings were removed, indicating success; otherwise, return false.
     */
    public Boolean deletePosting(Integer profileId, Integer postingId) {
        Profile profile = profiles.get(profileId);
        boolean deleted = false;
        if (profile != null) {
            List<Posting> profilePostings = profile.getPostings();
            deleted = profilePostings.removeIf(posting -> posting.getPostingId().equals(postingId));

            if (deleted) {
                postings.remove(postingId); // Remove from global postings map
                logger.info("All postings with id: {} removed from profile with id: {}", postingId, profileId);
            } else {
                logger.warn("No postings with id: {} found in profile with id: {}", postingId, profileId);
            }
        } else {
            logger.warn("Profile with id: {} not found", profileId);
        }
        return deleted;
    }

    /* public Boolean deletePostingById(Integer postingId) {
        Posting posting = getPostingById(postingId);
        if(posting != null) {
            String userName = posting.getUserName();
            Profile profile = getProfileByUserName(userName).get(0);
            List<Posting> profilePostings = profile.getPostings();
            profilePostings.remove(posting);
            postings.remove(postingId);
            return true;
        } else
            return false;
    }
     * 
     * 
     * @DeleteMapping("/postings/{postingId}")
     * public ResponseEntity<Void> putMethodName(@PathVariable Integer postingId) {
     * Boolean 
     * 
     * 
     * 
     * This method deletes a posting with given postingId from the model.
     * If the posting existss then:
     * 1) Use the userName 
     */

    public List<Posting> deletePostings(Profile profile) {
        List<Posting> postings = profile.getPostings();
        for (Posting posting : postings) {
            this.postings.remove(posting.getPostingId());
        }
        profile.setPostings(new ArrayList<>());
        return postings;
    }

    public void deleteProfile(Profile profile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProfile'");
    }
}
