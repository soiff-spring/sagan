package sagan.team.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;
import sagan.git.GitClient;
import sagan.git.GitUserProfile;
import sagan.team.MemberProfile;

@Service
public class SignInService {

    private final TeamService teamService;
    private final String gitHubTeamId;

    @Autowired
    public SignInService(TeamService teamService, @Value("${github.team.id}") String gitHubTeamId) {
        this.teamService = teamService;
        this.gitHubTeamId = gitHubTeamId;
    }

    public MemberProfile getOrCreateMemberProfile(Long githubId, GitClient gitHub) {
        GitUserProfile profile = gitHub.getGitUserProfile();
        GitHubUserProfile remoteProfile = new GitHubUserProfile(profile.getId(), profile.getUsername(),
            profile.getName(), profile.getLocation(), profile.getCompany(), profile.getBlog(),
            profile.getEmail(), profile.getProfileImageUrl(), profile.getCreatedDate());

        return teamService.createOrUpdateMemberProfile(githubId, remoteProfile.getUsername(), remoteProfile
            .getProfileImageUrl(), remoteProfile.getName());
    }

    public boolean isSpringMember(String userId, GitClient gitHub) {
        return gitHub.isMember(gitHubTeamId, userId);
    }
}
