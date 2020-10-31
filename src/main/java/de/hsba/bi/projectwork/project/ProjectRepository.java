package de.hsba.bi.projectwork.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAll();

    Optional<Project> findById(Long id);

    Optional<Project> findByName(String name);

}
