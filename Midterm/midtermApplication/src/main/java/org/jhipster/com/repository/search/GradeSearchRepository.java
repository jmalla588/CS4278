package org.jhipster.com.repository.search;

import org.jhipster.com.domain.Grade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Grade entity.
 */
public interface GradeSearchRepository extends ElasticsearchRepository<Grade, Long> {
}
