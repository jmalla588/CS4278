package com.josephstahl.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.josephstahl.jhipster.domain.Dormitory;

import com.josephstahl.jhipster.repository.DormitoryRepository;
import com.josephstahl.jhipster.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dormitory.
 */
@RestController
@RequestMapping("/api")
public class DormitoryResource {

    private final Logger log = LoggerFactory.getLogger(DormitoryResource.class);
        
    @Inject
    private DormitoryRepository dormitoryRepository;

    /**
     * POST  /dormitories : Create a new dormitory.
     *
     * @param dormitory the dormitory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dormitory, or with status 400 (Bad Request) if the dormitory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dormitories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dormitory> createDormitory(@RequestBody Dormitory dormitory) throws URISyntaxException {
        log.debug("REST request to save Dormitory : {}", dormitory);
        if (dormitory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dormitory", "idexists", "A new dormitory cannot already have an ID")).body(null);
        }
        Dormitory result = dormitoryRepository.save(dormitory);
        return ResponseEntity.created(new URI("/api/dormitories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dormitory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dormitories : Updates an existing dormitory.
     *
     * @param dormitory the dormitory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dormitory,
     * or with status 400 (Bad Request) if the dormitory is not valid,
     * or with status 500 (Internal Server Error) if the dormitory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dormitories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dormitory> updateDormitory(@RequestBody Dormitory dormitory) throws URISyntaxException {
        log.debug("REST request to update Dormitory : {}", dormitory);
        if (dormitory.getId() == null) {
            return createDormitory(dormitory);
        }
        Dormitory result = dormitoryRepository.save(dormitory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dormitory", dormitory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dormitories : get all the dormitories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dormitories in body
     */
    @RequestMapping(value = "/dormitories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dormitory> getAllDormitories() {
        log.debug("REST request to get all Dormitories");
        List<Dormitory> dormitories = dormitoryRepository.findAll();
        return dormitories;
    }

    /**
     * GET  /dormitories/:id : get the "id" dormitory.
     *
     * @param id the id of the dormitory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dormitory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dormitories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dormitory> getDormitory(@PathVariable Long id) {
        log.debug("REST request to get Dormitory : {}", id);
        Dormitory dormitory = dormitoryRepository.findOne(id);
        return Optional.ofNullable(dormitory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dormitories/:id : delete the "id" dormitory.
     *
     * @param id the id of the dormitory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dormitories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDormitory(@PathVariable Long id) {
        log.debug("REST request to delete Dormitory : {}", id);
        dormitoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dormitory", id.toString())).build();
    }

}
