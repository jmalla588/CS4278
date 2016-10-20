package com.josephstahl.jhipster.web.rest;

import com.josephstahl.jhipster.MyapplicationApp;

import com.josephstahl.jhipster.domain.PreferredContact;
import com.josephstahl.jhipster.repository.PreferredContactRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PreferredContactResource REST controller.
 *
 * @see PreferredContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyapplicationApp.class)
public class PreferredContactResourceIntTest {

    private static final String DEFAULT_NAME_OF_CHOICE = "AAAAA";
    private static final String UPDATED_NAME_OF_CHOICE = "BBBBB";

    @Inject
    private PreferredContactRepository preferredContactRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPreferredContactMockMvc;

    private PreferredContact preferredContact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PreferredContactResource preferredContactResource = new PreferredContactResource();
        ReflectionTestUtils.setField(preferredContactResource, "preferredContactRepository", preferredContactRepository);
        this.restPreferredContactMockMvc = MockMvcBuilders.standaloneSetup(preferredContactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PreferredContact createEntity(EntityManager em) {
        PreferredContact preferredContact = new PreferredContact()
                .nameOfChoice(DEFAULT_NAME_OF_CHOICE);
        return preferredContact;
    }

    @Before
    public void initTest() {
        preferredContact = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreferredContact() throws Exception {
        int databaseSizeBeforeCreate = preferredContactRepository.findAll().size();

        // Create the PreferredContact

        restPreferredContactMockMvc.perform(post("/api/preferred-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preferredContact)))
                .andExpect(status().isCreated());

        // Validate the PreferredContact in the database
        List<PreferredContact> preferredContacts = preferredContactRepository.findAll();
        assertThat(preferredContacts).hasSize(databaseSizeBeforeCreate + 1);
        PreferredContact testPreferredContact = preferredContacts.get(preferredContacts.size() - 1);
        assertThat(testPreferredContact.getNameOfChoice()).isEqualTo(DEFAULT_NAME_OF_CHOICE);
    }

    @Test
    @Transactional
    public void getAllPreferredContacts() throws Exception {
        // Initialize the database
        preferredContactRepository.saveAndFlush(preferredContact);

        // Get all the preferredContacts
        restPreferredContactMockMvc.perform(get("/api/preferred-contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(preferredContact.getId().intValue())))
                .andExpect(jsonPath("$.[*].nameOfChoice").value(hasItem(DEFAULT_NAME_OF_CHOICE.toString())));
    }

    @Test
    @Transactional
    public void getPreferredContact() throws Exception {
        // Initialize the database
        preferredContactRepository.saveAndFlush(preferredContact);

        // Get the preferredContact
        restPreferredContactMockMvc.perform(get("/api/preferred-contacts/{id}", preferredContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preferredContact.getId().intValue()))
            .andExpect(jsonPath("$.nameOfChoice").value(DEFAULT_NAME_OF_CHOICE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPreferredContact() throws Exception {
        // Get the preferredContact
        restPreferredContactMockMvc.perform(get("/api/preferred-contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreferredContact() throws Exception {
        // Initialize the database
        preferredContactRepository.saveAndFlush(preferredContact);
        int databaseSizeBeforeUpdate = preferredContactRepository.findAll().size();

        // Update the preferredContact
        PreferredContact updatedPreferredContact = preferredContactRepository.findOne(preferredContact.getId());
        updatedPreferredContact
                .nameOfChoice(UPDATED_NAME_OF_CHOICE);

        restPreferredContactMockMvc.perform(put("/api/preferred-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPreferredContact)))
                .andExpect(status().isOk());

        // Validate the PreferredContact in the database
        List<PreferredContact> preferredContacts = preferredContactRepository.findAll();
        assertThat(preferredContacts).hasSize(databaseSizeBeforeUpdate);
        PreferredContact testPreferredContact = preferredContacts.get(preferredContacts.size() - 1);
        assertThat(testPreferredContact.getNameOfChoice()).isEqualTo(UPDATED_NAME_OF_CHOICE);
    }

    @Test
    @Transactional
    public void deletePreferredContact() throws Exception {
        // Initialize the database
        preferredContactRepository.saveAndFlush(preferredContact);
        int databaseSizeBeforeDelete = preferredContactRepository.findAll().size();

        // Get the preferredContact
        restPreferredContactMockMvc.perform(delete("/api/preferred-contacts/{id}", preferredContact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PreferredContact> preferredContacts = preferredContactRepository.findAll();
        assertThat(preferredContacts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
