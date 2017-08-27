package sagan.git.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUser;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.web.util.UriTemplate;
import sagan.git.GitClient;
import sagan.git.GitUser;
import sagan.git.GitUserProfile;
import sagan.git.MarkdownHtml;
import sagan.projects.Project;
import sagan.util.CachedRestClient;
import sagan.util.GithubService;

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

    private static final String API_URL_BASE = GithubService.API_URL_BASE;
    private static final String IS_MEMBER_URL = "https://api.github.com/teams/{team}/members/{user}";

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
    public GitUserProfile getGitUserProfile() {
        final GitHubUserProfile profile = gitHub.userOperations().getUserProfile();
        return new GitUserProfile(profile.getId(), profile.getUsername(), profile.getName(),
            profile.getLocation(), profile.getCompany(), profile.getBlog(), profile.getEmail(),
            profile.getProfileImageUrl(), profile.getCreatedDate());
    }

    @Override
    public boolean isMember(String team, String user) {
        return gitHub.restOperations().getForEntity(IS_MEMBER_URL, Void.class, team, user)
            .getStatusCode() == HttpStatus.NO_CONTENT;
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

    @Override
    public boolean hasPagesBranch(Project project) {
        if (project.hasSite() && project.getSiteUrl().startsWith("http://projects.spring.io")) {
            String ghPagesBranchUrl = format("%s/repos/%s/%s/branches/gh-pages",
                API_URL_BASE, "spring-projects", project.getId());
            try {
                HttpHeaders headers = gitHub.restOperations().headForHeaders(ghPagesBranchUrl);
                return "200 OK".equals(headers.getFirst("Status"));
            } catch (Exception ex) {
                // RestTemplate call above logs at WARN level if anything goes wrong
            }
        }
        return false;
    }

    public String sendRequestForJson(String path, Object... uriVariables) {
        String url = resolveUrl(path, uriVariables);
        return restClient.get(gitHub.restOperations(), url, String.class);
    }

    public byte[] sendRequestForDownload(String path, Object... uriVariables) {
        String url = resolveUrl(path, uriVariables);
        return restClient.get(gitHub.restOperations(), url, byte[].class);
    }

    public String sendRequestForHtml(String path, Object... uriVariables) {
        String url = resolveUrl(path, uriVariables);
        MarkdownHtml markdownHtml = restClient.get(gitHub.restOperations(), url, MarkdownHtml.class);
        return markdownHtml.toString();
    }

    public String sendPostRequestForHtml(String path, String body, Object... uriVariables) {
        String url = resolveUrl(path, uriVariables);
        return restClient.post(gitHub.restOperations(), url, String.class, body);
    }

    private String resolveUrl(String path, Object[] uriVariables) {
        String expandedPath = new UriTemplate(path).expand(uriVariables).toString();
        return API_URL_BASE + expandedPath;
    }
}
