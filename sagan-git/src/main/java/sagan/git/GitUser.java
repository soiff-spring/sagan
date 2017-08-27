package sagan.git;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitUser implements Serializable {
    private Long id;
    private String url;
    private String login;
    private String avatarUrl;
    private String gravatarId;
    private String name;
    private String email;
    private Date date;
}
