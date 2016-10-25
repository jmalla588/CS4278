package org.jhipster.com.web.rest;

import org.jhipster.com.MidtermApplicationApp;

import org.jhipster.com.domain.Submission;
import org.jhipster.com.repository.SubmissionRepository;
import org.jhipster.com.repository.search.SubmissionSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SubmissionResource REST controller.
 *
 * @see SubmissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MidtermApplicationApp.class)
public class SubmissionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    @Inject
    private SubmissionRepository submissionRepository;

    @Inject
    private SubmissionSearchRepository submissionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubmissionResource submissionResource = new SubmissionResource();
        ReflectionTestUtils.setField(submissionResource, "submissionSearchRepository", submissionSearchRepository);
        ReflectionTestUtils.setField(submissionResource, "submissionRepository", submissionRepository);
        this.restSubmissionMockMvc = MockMvcBuilders.standaloneSetup(submissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
                .date(DEFAULT_DATE);
        return submission;
    }

    @Before
    public void initTest() {
        submissionSearchRepository.deleteAll();
        submission = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubmission() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // Create the Submission

        restSubmissionMockMvc.perform(post("/api/submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(submission)))
                .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).hasSize(databaseSizeBeforeCreate + 1);
        Submission testSubmission = submissions.get(submissions.size() - 1);
        assertThat(testSubmission.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Submission in ElasticSearch
        Submission submissionEs = submissionSearchRepository.findOne(testSubmission.getId());
        assertThat(submissionEs).isEqualToComparingFieldByField(testSubmission);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = submissionRepository.findAll().size();
        // set the field null
        submission.setDate(null);

        // Create the Submission, which fails.

        restSubmissionMockMvc.perform(post("/api/submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(submission)))
                .andExpect(status().isBadRequest());

        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissions
        restSubmissionMockMvc.perform(get("/api/submissions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc.perform(get("/api/submissions/{id}", submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get("/api/submissions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        submissionSearchRepository.save(submission);
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findOne(submission.getId());
        updatedSubmission
                .date(UPDATED_DATE);

        restSubmissionMockMvc.perform(put("/api/submissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubmission)))
                .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissions.get(submissions.size() - 1);
        assertThat(testSubmission.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Submission in ElasticSearch
        Submission submissionEs = submissionSearchRepository.findOne(testSubmission.getId());
        assertThat(submissionEs).isEqualToComparingFieldByField(testSubmission);
    }

    @Test
    @Transactional
    public void deleteSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        submissionSearchRepository.save(submission);
        int databaseSizeBeforeDelete = submissionRepository.findAll().size();

        // Get the submission
        restSubmissionMockMvc.perform(delete("/api/submissions/{id}", submission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean submissionExistsInEs = submissionSearchRepository.exists(submission.getId());
        assertThat(submissionExistsInEs).isFalse();

        // Validate the database is empty
        List<Submission> submissions = submissionRepository.findAll();
        assertThat(submissions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);
        submissionSearchRepository.save(submission);

        // Search the submission
        restSubmissionMockMvc.perform(get("/api/_search/submissions?query=id:" + submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }
}
