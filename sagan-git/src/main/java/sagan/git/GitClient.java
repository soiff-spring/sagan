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
    void createIssue(final String org, Project project, ObjectMapper jsonMapper,
                     Map<String, Object> root, Expression spel);
    boolean hasPagesBranch(final Project project);
}
