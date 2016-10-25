package org.jhipster.com.repository;

import org.jhipster.com.domain.Instructor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Instructor entity.
 */
@SuppressWarnings("unused")
public interface InstructorRepository extends JpaRepository<Instructor,Long> {

}
