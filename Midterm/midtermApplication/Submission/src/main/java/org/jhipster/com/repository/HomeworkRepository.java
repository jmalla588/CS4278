package org.jhipster.com.repository;

import org.jhipster.com.domain.Homework;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Homework entity.
 */
@SuppressWarnings("unused")
public interface HomeworkRepository extends JpaRepository<Homework,Long> {

}
