package com.josephstahl.jhipster.repository;

import com.josephstahl.jhipster.domain.PreferredContact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PreferredContact entity.
 */
@SuppressWarnings("unused")
public interface PreferredContactRepository extends JpaRepository<PreferredContact,Long> {

}
