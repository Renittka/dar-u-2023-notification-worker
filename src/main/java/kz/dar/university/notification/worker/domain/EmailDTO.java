package kz.dar.university.notification.worker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    private String recipient;
    private String title;
    private String content;

}
