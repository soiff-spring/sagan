package sagan.git.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.http.ResponseEntity;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUser;
import sagan.git.GitClient;
import sagan.git.GitUser;
import sagan.projects.Project;
import sagan.util.CachedRestClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
@Slf4j
public class GithubClient implements GitClient {

    private static final String API_URL_BASE = "https://api.github.com";

    private final GitHub gitHub;
    private final CachedRestClient restClient;

    public GithubClient(GitHub gitHub, CachedRestClient restClient) {
        this.gitHub = gitHub;
        this.restClient = restClient;
    }

    @Override
    public GitUser[] getGitUsers(String teamId) {
        String membersUrl = API_URL_BASE + "/teams/{teamId}/members?per_page=100";
        ResponseEntity<GitHubUser[]> entity =
            gitHub.restOperations().getForEntity(membersUrl, GitHubUser[].class, teamId);
        return GitHubUserToGitUser.convert(entity.getBody());
    }

    @Override
    public String getUserName(String username) {
        return gitHub.restOperations()
            .getForObject(API_URL_BASE + "/users/{user}", GitHubUser.class, username)
            .getName();
    }

    @Override
    public void createIssue(String org, Project project, ObjectMapper jsonMapper, Map<String, Object> root, Expression spel) {
        root.put("project", project);
        Map<String, String> newIssue = new HashMap<>();
        newIssue.put("title", "Please merge the latest changes to common gh-pages");
        newIssue.put("body", spel.getValue(root, String.class));
        String projectIssuesUrl =
            format("%s/repos/%s/%s/issues", API_URL_BASE, org, project.getId());
        try {
            URI newIssueUrl =
                gitHub.restOperations().postForLocation(projectIssuesUrl,
                    jsonMapper.writeValueAsString(newIssue));
            log.info("Notification of new gh-pages changes created at " + newIssueUrl);
        } catch (RuntimeException | JsonProcessingException ex) {
            log.warn("Unable to POST new issue to " + projectIssuesUrl);
        }
    }


}
