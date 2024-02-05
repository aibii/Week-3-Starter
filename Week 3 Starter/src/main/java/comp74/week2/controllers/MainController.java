package comp74.week2.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import comp74.week2.model.Model;
import comp74.week2.model.Posting;
import comp74.week2.model.Profile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    Model model;

    public MainController(Model model) {
        this.model = model;
    }

    @GetMapping("/profiles")
    public List<Profile> getProfileByUserName(
            @RequestParam(name = "userName", required = false, defaultValue = "") String userName) {
        System.out.println("Username: " + userName);
        if (userName.isEmpty())
            return model.getProfiles();
        else
            return model.getProfilesByUserName(userName);
    }

    @GetMapping("/profiles/{id}")
    public Profile getProfileById(@PathVariable Integer id) {
        return model.getProfileById(id);
    }

    @GetMapping("/profiles/{id}/postings")
    public List<Posting> getProfilePostings(@PathVariable Integer id) {
        List<Posting> postings = null;
        Profile profile = model.getProfileById(id);
        if (profile != null)
            postings = profile.getPostings();
        return postings;
    }

    @PostMapping("/profiles")
    public Profile addProfile(@RequestBody Profile profile) {
        
        Profile newProfile = model.addProfile(profile);
        
        return profile;
    }

    @PostMapping("/profiles/{id}/add-postings")
    public Posting addPosting(@PathVariable Integer id, @RequestBody Posting posting) {
        Posting newPosting = null;
        Profile profile = model.getProfileById(id);
        if (profile != null)
            newPosting = model.addPosting(profile, posting);
        return newPosting;
    }

    @PostMapping("profiles/{userName}/postings")
    public Posting addPosting(@PathVariable String userName, @RequestBody Posting posting) {
        Profile profile = model.getProfileByUserName(userName);
        if (profile != null)
            posting = model.addPosting(profile, posting);
        return posting;
    }


    //Update a posting belonging to a given profile
    //updatePosting takes id parameter, and Posting object from URL of the request.
    //Posting object is the body of the request, it's annotated with @RequestBody

    //"api/profiles/100/postings/100"
    // If profile & posting exist - STATUS 200 OK
    // If profile exists but posting does not exist - STATUS 404 NOT FOUND
    // If profile does not exist - STATUS 404 NOT FOUND
    
    @PutMapping("/profiles/{id}/postings/{postingId}")
    public Posting updatePosting(@PathVariable Integer id, @PathVariable Integer postingId,
            @RequestBody Posting posting) {
        Posting newPosting = null;
        Profile profile = model.getProfileById(id);
        if (profile != null) //if profile exists we update posting with the given posting id
            newPosting = model.updatePosting(profile, postingId, posting);
        return newPosting;
    }


    @DeleteMapping("/profiles/{id}/postings/{postingId}")
    public ResponseEntity<?> deletePosting(@PathVariable Integer id, @PathVariable Integer postingId) {
        Profile profile = model.getProfileById(id);
        if (profile == null) {
            logger.info("Profile with id: {} not found", id);
            return ResponseEntity.notFound().build(); // 404 Not Found if the profile doesn't exist
        }
        
        boolean deleted = model.deletePosting(id, postingId);
        if (deleted) {
            logger.info("Deleted posting with id: {} from profile with id: {}", postingId, id);
            return ResponseEntity.noContent().build(); // 204 No Content if the posting was successfully deleted
        } else {
            logger.info("Posting with id: {} not found in profile with id: {}", postingId, id);
            return ResponseEntity.notFound().build(); // 404 Not Found if the posting wasn't found in the profile
        }
    }

    //Delete all postings belonging to a given profile.
    @DeleteMapping("/profiles/{id}/postings")
        public ResponseEntity<?> deletePostings(@PathVariable Integer id) {
        Profile profile = model.getProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found if profile doesn't exist
        }
        
        List<Posting> deletedPostings = model.deletePostings(profile);
        if (deletedPostings.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no postings to delete
        }
        
        return ResponseEntity.ok(deletedPostings); // Return deleted postings with 200 OK
    }

    //DELETE a given profile only if all postings have been deleted
    @DeleteMapping("/profiles/{id}")
        public ResponseEntity<?> deleteProfile(@PathVariable Integer id) {
        Profile profile = model.getProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found if profile doesn't exist
        }
        
        if (!profile.getPostings().isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Profile still has postings."); // 409 Conflict if postings exist
        }
        
        model.deleteProfile(profile);
        return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
    }
}
