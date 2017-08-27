package sagan.team.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sagan.git.GitClient;
import sagan.git.GitUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTeamImporter implements TeamImporter {

    private final TeamService teamService;
    private final GitClient client;
    private final String gitHubTeamId;

    @Autowired
    public DefaultTeamImporter(TeamService teamService, GitClient client,
                               @Value("${github.team.id}") String gitHubTeamId) {
        this.teamService = teamService;
        this.client = client;
        this.gitHubTeamId = gitHubTeamId;
    }

    @Transactional
    public void importTeamMembers(GitClient gitHub) {
        GitUser[] users = client.getGitUsers(gitHubTeamId);
        List<Long> userIds = new ArrayList<>();
        for (GitUser user : users) {
            userIds.add(user.getId());
            String userName = client.getUserName(user.getName());

            teamService.createOrUpdateMemberProfile(user.getId(), user.getLogin(), user.getAvatarUrl(), userName);
        }
        teamService.showOnlyTeamMembersWithIds(userIds);
    }
}
