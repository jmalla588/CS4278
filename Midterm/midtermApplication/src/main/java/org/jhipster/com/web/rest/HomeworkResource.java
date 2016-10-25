package org.jhipster.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.com.domain.Homework;

import org.jhipster.com.repository.HomeworkRepository;
import org.jhipster.com.repository.search.HomeworkSearchRepository;
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
 * REST controller for managing Homework.
 */
@RestController
@RequestMapping("/api")
public class HomeworkResource {

    private final Logger log = LoggerFactory.getLogger(HomeworkResource.class);
        
    @Inject
    private HomeworkRepository homeworkRepository;

    @Inject
    private HomeworkSearchRepository homeworkSearchRepository;

    /**
     * POST  /homework : Create a new homework.
     *
     * @param homework the homework to create
     * @return the ResponseEntity with status 201 (Created) and with body the new homework, or with status 400 (Bad Request) if the homework has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/homework",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Homework> createHomework(@Valid @RequestBody Homework homework) throws URISyntaxException {
        log.debug("REST request to save Homework : {}", homework);
        if (homework.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("homework", "idexists", "A new homework cannot already have an ID")).body(null);
        }
        Homework result = homeworkRepository.save(homework);
        homeworkSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/homework/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("homework", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /homework : Updates an existing homework.
     *
     * @param homework the homework to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated homework,
     * or with status 400 (Bad Request) if the homework is not valid,
     * or with status 500 (Internal Server Error) if the homework couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/homework",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Homework> updateHomework(@Valid @RequestBody Homework homework) throws URISyntaxException {
        log.debug("REST request to update Homework : {}", homework);
        if (homework.getId() == null) {
            return createHomework(homework);
        }
        Homework result = homeworkRepository.save(homework);
        homeworkSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("homework", homework.getId().toString()))
            .body(result);
    }

    /**
     * GET  /homework : get all the homework.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of homework in body
     */
    @RequestMapping(value = "/homework",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Homework> getAllHomework() {
        log.debug("REST request to get all Homework");
        List<Homework> homework = homeworkRepository.findAll();
        return homework;
    }

    /**
     * GET  /homework/:id : get the "id" homework.
     *
     * @param id the id of the homework to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the homework, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/homework/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Homework> getHomework(@PathVariable Long id) {
        log.debug("REST request to get Homework : {}", id);
        Homework homework = homeworkRepository.findOne(id);
        return Optional.ofNullable(homework)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /homework/:id : delete the "id" homework.
     *
     * @param id the id of the homework to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/homework/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHomework(@PathVariable Long id) {
        log.debug("REST request to delete Homework : {}", id);
        homeworkRepository.delete(id);
        homeworkSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("homework", id.toString())).build();
    }

    /**
     * SEARCH  /_search/homework?query=:query : search for the homework corresponding
     * to the query.
     *
     * @param query the query of the homework search 
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/homework",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Homework> searchHomework(@RequestParam String query) {
        log.debug("REST request to search Homework for query {}", query);
        return StreamSupport
            .stream(homeworkSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
