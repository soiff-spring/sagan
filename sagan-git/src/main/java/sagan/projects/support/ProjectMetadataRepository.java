package sagan.projects.support;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sagan.projects.Project;

import java.util.List;

@Repository
public interface ProjectMetadataRepository extends JpaRepository<Project, String> {
    List<Project> findByCategory(String category);

    @Query("SELECT DISTINCT p FROM Project p JOIN FETCH p.releaseList")
    List<Project> findAllWithReleases(Sort sort);
}
