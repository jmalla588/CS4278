package org.jhipster.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.com.domain.Submission;

import org.jhipster.com.repository.SubmissionRepository;
import org.jhipster.com.repository.search.SubmissionSearchRepository;
import org.jhipster.com.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Submission.
 */
@RestController
@RequestMapping("/api")
public class SubmissionResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionResource.class);
        
    @Inject
    private SubmissionRepository submissionRepository;

    @Inject
    private SubmissionSearchRepository submissionSearchRepository;

    /**
     * POST  /submissions : Create a new submission.
     *
     * @param submission the submission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new submission, or with status 400 (Bad Request) if the submission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/submissions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Submission> createSubmission(@Valid @RequestBody Submission submission) throws URISyntaxException {
        log.debug("REST request to save Submission : {}", submission);
        if (submission.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("submission", "idexists", "A new submission cannot already have an ID")).body(null);
        }
        Submission result = submissionRepository.save(submission);
        submissionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("submission", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /submissions : Updates an existing submission.
     *
     * @param submission the submission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated submission,
     * or with status 400 (Bad Request) if the submission is not valid,
     * or with status 500 (Internal Server Error) if the submission couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/submissions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Submission> updateSubmission(@Valid @RequestBody Submission submission) throws URISyntaxException {
        log.debug("REST request to update Submission : {}", submission);
        if (submission.getId() == null) {
            return createSubmission(submission);
        }
        Submission result = submissionRepository.save(submission);
        submissionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("submission", submission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /submissions : get all the submissions.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of submissions in body
     */
    @RequestMapping(value = "/submissions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Submission> getAllSubmissions(@RequestParam(required = false) String filter) {
        if ("grade-is-null".equals(filter)) {
            log.debug("REST request to get all Submissions where grade is null");
            return StreamSupport
                .stream(submissionRepository.findAll().spliterator(), false)
                .filter(submission -> submission.getGrade() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Submissions");
        List<Submission> submissions = submissionRepository.findAll();
        return submissions;
    }

    /**
     * GET  /submissions/:id : get the "id" submission.
     *
     * @param id the id of the submission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the submission, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/submissions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
        log.debug("REST request to get Submission : {}", id);
        Submission submission = submissionRepository.findOne(id);
        return Optional.ofNullable(submission)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /submissions/:id : delete the "id" submission.
     *
     * @param id the id of the submission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/submissions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        log.debug("REST request to delete Submission : {}", id);
        submissionRepository.delete(id);
        submissionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("submission", id.toString())).build();
    }

    /**
     * SEARCH  /_search/submissions?query=:query : search for the submission corresponding
     * to the query.
     *
     * @param query the query of the submission search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/submissions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Submission> searchSubmissions(@RequestParam String query) {
        log.debug("REST request to search Submissions for query {}", query);
        return StreamSupport
            .stream(submissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
