package sagan.git;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.expression.Expression;
import sagan.projects.Project;

import java.util.Map;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public interface GitClient {
    GitUser[] getGitUsers(final String teamId);

    String getUserName(final String username);

    void createIssue(final String org, Project project, final ObjectMapper jsonMapper,
                     final Map<String, Object> root, final Expression spel);

    boolean hasPagesBranch(final Project project);

    String sendRequestForJson(String path, Object... uriVariables);

    byte[] sendRequestForDownload(String path, Object... uriVariables);

    String sendRequestForHtml(String path, Object... uriVariables);

    String sendPostRequestForHtml(String path, String body, Object... uriVariables);

}
