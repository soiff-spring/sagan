import org.springframework.social.github.api.GitHub;
import sagan.git.GitClient;
import sagan.git.GitUser;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public class GithubClient implements GitClient {

    public static final String API_URL_BASE = "https://api.github.com";

    private final GitHub gitHub;
    private final CachedRestClient restClient;

    @Override
    public GitUser[] getGitUsers(String teamId) {
        return new GitUser[0];
    }
}
