package org.jhipster.com.repository.search;

import org.jhipster.com.domain.Instructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Instructor entity.
 */
public interface InstructorSearchRepository extends ElasticsearchRepository<Instructor, Long> {
}
