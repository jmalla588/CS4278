package org.jhipster.com.repository;

import org.jhipster.com.domain.Submission;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Submission entity.
 */
@SuppressWarnings("unused")
public interface SubmissionRepository extends JpaRepository<Submission,Long> {

}
