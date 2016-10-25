package org.jhipster.com.repository.search;

import org.jhipster.com.domain.Homework;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Homework entity.
 */
public interface HomeworkSearchRepository extends ElasticsearchRepository<Homework, Long> {
}
