package sagan.git.github;

import org.springframework.social.github.api.GitHubUser;
import sagan.git.GitUser;

/**
 * Created by soiff on 27/08/2017.
 *
 * @author : soiff
 * @version : 1.0
 * @since : 1.8
 */
public class GitHubUserToGitUser {
    static GitUser convert(final GitHubUser gitHubUser) {
        final GitUser gitUser = new GitUser();
        gitUser.setAvatarUrl(gitHubUser.getAvatarUrl());
        gitUser.setDate(gitHubUser.getDate());
        gitUser.setEmail(gitHubUser.getEmail());
        gitUser.setGravatarId(gitHubUser.getGravatarId());
        gitUser.setId(gitHubUser.getId());
        gitUser.setLogin(gitHubUser.getLogin());
        gitUser.setName(gitHubUser.getName());
        gitUser.setUrl(gitHubUser.getUrl());
        return gitUser;
    }

    static GitUser[] convert(final GitHubUser[] gitHubUsers) {
        if (null == gitHubUsers)
            return null;
        final GitUser[] users = new GitUser[gitHubUsers.length];
        for (int i = 0; i < gitHubUsers.length; ++i)
            users[i] = convert(gitHubUsers[i]);
        return users;
    }
}
