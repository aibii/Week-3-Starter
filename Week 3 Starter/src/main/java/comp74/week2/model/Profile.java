package comp74.week2.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {

    private Integer profileId;
    String userName;
    List<Posting> postings;

    public Profile(String userName) {
        this.userName = userName;
        postings = new ArrayList<>();
    }

    public void addPosting(Posting posting) {
        postings.add(posting);
    }

}
