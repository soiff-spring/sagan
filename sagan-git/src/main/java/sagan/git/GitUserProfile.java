package sagan.git;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
@Slf4j
@Data
@NoArgsConstructor
public class GitUserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String username;
    private String location;
    private String company;
    private String blog;
    private String email;
    private Date createdDate;
    private String profileImageUrl;

    public GitUserProfile(long id, String username, String name, String location, String company, String blog,
                          String email, String profileImageUrl, Date createdDate) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.location = location;
        this.company = company;
        this.blog = blog;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.createdDate = createdDate;
    }
}
