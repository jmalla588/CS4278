package org.jhipster.com.web.rest;

import org.jhipster.com.MidtermApplicationApp;

import org.jhipster.com.domain.Instructor;
import org.jhipster.com.repository.InstructorRepository;
import org.jhipster.com.repository.search.InstructorSearchRepository;

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
 * Test class for the InstructorResource REST controller.
 *
 * @see InstructorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MidtermApplicationApp.class)
public class InstructorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private InstructorRepository instructorRepository;

    @Inject
    private InstructorSearchRepository instructorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInstructorMockMvc;

    private Instructor instructor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstructorResource instructorResource = new InstructorResource();
        ReflectionTestUtils.setField(instructorResource, "instructorSearchRepository", instructorSearchRepository);
        ReflectionTestUtils.setField(instructorResource, "instructorRepository", instructorRepository);
        this.restInstructorMockMvc = MockMvcBuilders.standaloneSetup(instructorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL);
        return instructor;
    }

    @Before
    public void initTest() {
        instructorSearchRepository.deleteAll();
        instructor = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().size();

        // Create the Instructor

        restInstructorMockMvc.perform(post("/api/instructors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instructor)))
                .andExpect(status().isCreated());

        // Validate the Instructor in the database
        List<Instructor> instructors = instructorRepository.findAll();
        assertThat(instructors).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructors.get(instructors.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Instructor in ElasticSearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToComparingFieldByField(testInstructor);
    }

    @Test
    @Transactional
    public void getAllInstructors() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get all the instructors
        restInstructorMockMvc.perform(get("/api/instructors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);

        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instructor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstructor() throws Exception {
        // Get the instructor
        restInstructorMockMvc.perform(get("/api/instructors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeUpdate = instructorRepository.findAll().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findOne(instructor.getId());
        updatedInstructor
                .name(UPDATED_NAME)
                .email(UPDATED_EMAIL);

        restInstructorMockMvc.perform(put("/api/instructors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstructor)))
                .andExpect(status().isOk());

        // Validate the Instructor in the database
        List<Instructor> instructors = instructorRepository.findAll();
        assertThat(instructors).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructors.get(instructors.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Instructor in ElasticSearch
        Instructor instructorEs = instructorSearchRepository.findOne(testInstructor.getId());
        assertThat(instructorEs).isEqualToComparingFieldByField(testInstructor);
    }

    @Test
    @Transactional
    public void deleteInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);
        int databaseSizeBeforeDelete = instructorRepository.findAll().size();

        // Get the instructor
        restInstructorMockMvc.perform(delete("/api/instructors/{id}", instructor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instructorExistsInEs = instructorSearchRepository.exists(instructor.getId());
        assertThat(instructorExistsInEs).isFalse();

        // Validate the database is empty
        List<Instructor> instructors = instructorRepository.findAll();
        assertThat(instructors).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstructor() throws Exception {
        // Initialize the database
        instructorRepository.saveAndFlush(instructor);
        instructorSearchRepository.save(instructor);

        // Search the instructor
        restInstructorMockMvc.perform(get("/api/_search/instructors?query=id:" + instructor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instructor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
