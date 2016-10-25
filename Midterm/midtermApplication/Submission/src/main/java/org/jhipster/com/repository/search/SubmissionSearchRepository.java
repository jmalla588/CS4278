package org.jhipster.com.repository.search;

import org.jhipster.com.domain.Submission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Submission entity.
 */
public interface SubmissionSearchRepository extends ElasticsearchRepository<Submission, Long> {
}
