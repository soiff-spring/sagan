package sagan.git.github;

import org.springframework.social.github.api.GitHub;
import sagan.git.GitClient;
import sagan.git.GitClientBuilder;
import sagan.util.CachedRestClient;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public class GithubClientBuilder implements GitClientBuilder<GitHub> {
    private final CachedRestClient client;

    public GithubClientBuilder(CachedRestClient client) {
        this.client = client;
    }

    @Override
    public GitClient build(GitHub api) {
        return new GithubClient(api, client);
    }
}
