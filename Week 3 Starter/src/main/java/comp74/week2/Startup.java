package comp74.week2;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import comp74.week2.model.Model;
import comp74.week2.model.Posting;
import comp74.week2.model.Profile;

@Component
public class Startup implements CommandLineRunner {

    final Integer MIN_PROFILES = 2;
    final Integer MAX_PROFILES = 10;
    final Integer MIN_POSTS = 2;
    final Integer MAX_POSTS = 8;
    Model model;
    Lorem lorem = LoremIpsum.getInstance();

    public Startup(Model model) {
        this.model = model;
    }

    @Override
    public void run(String... args) throws Exception {

        Random random = new Random();

        Integer nProfiles = MIN_PROFILES + random.nextInt(MAX_PROFILES - MIN_PROFILES);

        for (int i = 0; i < nProfiles; i++) {
            Profile profile = new Profile(lorem.getFirstName());
            Integer nPosts = MIN_POSTS + random.nextInt(MAX_POSTS - MIN_POSTS);
            for (int j = 0; j < nPosts; j++) {
                Integer nWords = 10 + random.nextInt(MAX_POSTS - MIN_POSTS);
                Posting posting = new Posting();
                posting.setUserName(profile.getUserName());
                posting.setPostingText(lorem.getWords(nWords));
                model.addPosting(profile, posting);
                profile.addPosting(posting);

            }
            model.addProfile(profile);
        }

    }

}
