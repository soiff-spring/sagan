package sagan.team.support;

import sagan.git.GitClient;

interface TeamImporter {
    void importTeamMembers(GitClient gitHub);
}
