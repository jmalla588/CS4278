package com.josephstahl.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.josephstahl.jhipster.domain.PreferredContact;

import com.josephstahl.jhipster.repository.PreferredContactRepository;
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
 * REST controller for managing PreferredContact.
 */
@RestController
@RequestMapping("/api")
public class PreferredContactResource {

    private final Logger log = LoggerFactory.getLogger(PreferredContactResource.class);
        
    @Inject
    private PreferredContactRepository preferredContactRepository;

    /**
     * POST  /preferred-contacts : Create a new preferredContact.
     *
     * @param preferredContact the preferredContact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preferredContact, or with status 400 (Bad Request) if the preferredContact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/preferred-contacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferredContact> createPreferredContact(@RequestBody PreferredContact preferredContact) throws URISyntaxException {
        log.debug("REST request to save PreferredContact : {}", preferredContact);
        if (preferredContact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("preferredContact", "idexists", "A new preferredContact cannot already have an ID")).body(null);
        }
        PreferredContact result = preferredContactRepository.save(preferredContact);
        return ResponseEntity.created(new URI("/api/preferred-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("preferredContact", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferred-contacts : Updates an existing preferredContact.
     *
     * @param preferredContact the preferredContact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preferredContact,
     * or with status 400 (Bad Request) if the preferredContact is not valid,
     * or with status 500 (Internal Server Error) if the preferredContact couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/preferred-contacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferredContact> updatePreferredContact(@RequestBody PreferredContact preferredContact) throws URISyntaxException {
        log.debug("REST request to update PreferredContact : {}", preferredContact);
        if (preferredContact.getId() == null) {
            return createPreferredContact(preferredContact);
        }
        PreferredContact result = preferredContactRepository.save(preferredContact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("preferredContact", preferredContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferred-contacts : get all the preferredContacts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of preferredContacts in body
     */
    @RequestMapping(value = "/preferred-contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PreferredContact> getAllPreferredContacts() {
        log.debug("REST request to get all PreferredContacts");
        List<PreferredContact> preferredContacts = preferredContactRepository.findAll();
        return preferredContacts;
    }

    /**
     * GET  /preferred-contacts/:id : get the "id" preferredContact.
     *
     * @param id the id of the preferredContact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preferredContact, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/preferred-contacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PreferredContact> getPreferredContact(@PathVariable Long id) {
        log.debug("REST request to get PreferredContact : {}", id);
        PreferredContact preferredContact = preferredContactRepository.findOne(id);
        return Optional.ofNullable(preferredContact)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /preferred-contacts/:id : delete the "id" preferredContact.
     *
     * @param id the id of the preferredContact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/preferred-contacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePreferredContact(@PathVariable Long id) {
        log.debug("REST request to delete PreferredContact : {}", id);
        preferredContactRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preferredContact", id.toString())).build();
    }

}
