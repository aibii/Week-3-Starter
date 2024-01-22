package comp74.week2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Posting {

    private Integer postingId;
    private String postingText;
    private LocalDate date;
    private LocalTime time;
    private String userName;

    public void setDate(LocalDateTime datetime) {
        this.date = datetime.toLocalDate();
        this.time = datetime.toLocalTime();
    }

}
