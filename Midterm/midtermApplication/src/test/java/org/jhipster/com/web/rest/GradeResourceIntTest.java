package org.jhipster.com.web.rest;

import org.jhipster.com.MidtermApplicationApp;

import org.jhipster.com.domain.Grade;
import org.jhipster.com.repository.GradeRepository;
import org.jhipster.com.repository.search.GradeSearchRepository;

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
 * Test class for the GradeResource REST controller.
 *
 * @see GradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MidtermApplicationApp.class)
public class GradeResourceIntTest {


    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    @Inject
    private GradeRepository gradeRepository;

    @Inject
    private GradeSearchRepository gradeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGradeMockMvc;

    private Grade grade;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GradeResource gradeResource = new GradeResource();
        ReflectionTestUtils.setField(gradeResource, "gradeSearchRepository", gradeSearchRepository);
        ReflectionTestUtils.setField(gradeResource, "gradeRepository", gradeRepository);
        this.restGradeMockMvc = MockMvcBuilders.standaloneSetup(gradeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grade createEntity(EntityManager em) {
        Grade grade = new Grade()
                .score(DEFAULT_SCORE);
        return grade;
    }

    @Before
    public void initTest() {
        gradeSearchRepository.deleteAll();
        grade = createEntity(em);
    }

    @Test
    @Transactional
    public void createGrade() throws Exception {
        int databaseSizeBeforeCreate = gradeRepository.findAll().size();

        // Create the Grade

        restGradeMockMvc.perform(post("/api/grades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(grade)))
                .andExpect(status().isCreated());

        // Validate the Grade in the database
        List<Grade> grades = gradeRepository.findAll();
        assertThat(grades).hasSize(databaseSizeBeforeCreate + 1);
        Grade testGrade = grades.get(grades.size() - 1);
        assertThat(testGrade.getScore()).isEqualTo(DEFAULT_SCORE);

        // Validate the Grade in ElasticSearch
        Grade gradeEs = gradeSearchRepository.findOne(testGrade.getId());
        assertThat(gradeEs).isEqualToComparingFieldByField(testGrade);
    }

    @Test
    @Transactional
    public void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeRepository.findAll().size();
        // set the field null
        grade.setScore(null);

        // Create the Grade, which fails.

        restGradeMockMvc.perform(post("/api/grades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(grade)))
                .andExpect(status().isBadRequest());

        List<Grade> grades = gradeRepository.findAll();
        assertThat(grades).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGrades() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get all the grades
        restGradeMockMvc.perform(get("/api/grades?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
                .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())));
    }

    @Test
    @Transactional
    public void getGrade() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);

        // Get the grade
        restGradeMockMvc.perform(get("/api/grades/{id}", grade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(grade.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGrade() throws Exception {
        // Get the grade
        restGradeMockMvc.perform(get("/api/grades/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGrade() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);
        gradeSearchRepository.save(grade);
        int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

        // Update the grade
        Grade updatedGrade = gradeRepository.findOne(grade.getId());
        updatedGrade
                .score(UPDATED_SCORE);

        restGradeMockMvc.perform(put("/api/grades")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGrade)))
                .andExpect(status().isOk());

        // Validate the Grade in the database
        List<Grade> grades = gradeRepository.findAll();
        assertThat(grades).hasSize(databaseSizeBeforeUpdate);
        Grade testGrade = grades.get(grades.size() - 1);
        assertThat(testGrade.getScore()).isEqualTo(UPDATED_SCORE);

        // Validate the Grade in ElasticSearch
        Grade gradeEs = gradeSearchRepository.findOne(testGrade.getId());
        assertThat(gradeEs).isEqualToComparingFieldByField(testGrade);
    }

    @Test
    @Transactional
    public void deleteGrade() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);
        gradeSearchRepository.save(grade);
        int databaseSizeBeforeDelete = gradeRepository.findAll().size();

        // Get the grade
        restGradeMockMvc.perform(delete("/api/grades/{id}", grade.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean gradeExistsInEs = gradeSearchRepository.exists(grade.getId());
        assertThat(gradeExistsInEs).isFalse();

        // Validate the database is empty
        List<Grade> grades = gradeRepository.findAll();
        assertThat(grades).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGrade() throws Exception {
        // Initialize the database
        gradeRepository.saveAndFlush(grade);
        gradeSearchRepository.save(grade);

        // Search the grade
        restGradeMockMvc.perform(get("/api/_search/grades?query=id:" + grade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())));
    }
}
