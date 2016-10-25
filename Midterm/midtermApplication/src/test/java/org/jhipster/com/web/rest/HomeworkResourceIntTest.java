package org.jhipster.com.web.rest;

import org.jhipster.com.MidtermApplicationApp;

import org.jhipster.com.domain.Homework;
import org.jhipster.com.repository.HomeworkRepository;
import org.jhipster.com.repository.search.HomeworkSearchRepository;

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
 * Test class for the HomeworkResource REST controller.
 *
 * @see HomeworkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MidtermApplicationApp.class)
public class HomeworkResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final ZonedDateTime DEFAULT_DUE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DUE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DUE_STR = dateTimeFormatter.format(DEFAULT_DUE);

    @Inject
    private HomeworkRepository homeworkRepository;

    @Inject
    private HomeworkSearchRepository homeworkSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHomeworkMockMvc;

    private Homework homework;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HomeworkResource homeworkResource = new HomeworkResource();
        ReflectionTestUtils.setField(homeworkResource, "homeworkSearchRepository", homeworkSearchRepository);
        ReflectionTestUtils.setField(homeworkResource, "homeworkRepository", homeworkRepository);
        this.restHomeworkMockMvc = MockMvcBuilders.standaloneSetup(homeworkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Homework createEntity(EntityManager em) {
        Homework homework = new Homework()
                .title(DEFAULT_TITLE)
                .due(DEFAULT_DUE);
        return homework;
    }

    @Before
    public void initTest() {
        homeworkSearchRepository.deleteAll();
        homework = createEntity(em);
    }

    @Test
    @Transactional
    public void createHomework() throws Exception {
        int databaseSizeBeforeCreate = homeworkRepository.findAll().size();

        // Create the Homework

        restHomeworkMockMvc.perform(post("/api/homework")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(homework)))
                .andExpect(status().isCreated());

        // Validate the Homework in the database
        List<Homework> homework = homeworkRepository.findAll();
        assertThat(homework).hasSize(databaseSizeBeforeCreate + 1);
        Homework testHomework = homework.get(homework.size() - 1);
        assertThat(testHomework.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHomework.getDue()).isEqualTo(DEFAULT_DUE);

        // Validate the Homework in ElasticSearch
        Homework homeworkEs = homeworkSearchRepository.findOne(testHomework.getId());
        assertThat(homeworkEs).isEqualToComparingFieldByField(testHomework);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeworkRepository.findAll().size();
        // set the field null
        homework.setTitle(null);

        // Create the Homework, which fails.

        restHomeworkMockMvc.perform(post("/api/homework")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(homework)))
                .andExpect(status().isBadRequest());

        List<Homework> homework = homeworkRepository.findAll();
        assertThat(homework).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDueIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeworkRepository.findAll().size();
        // set the field null
        homework.setDue(null);

        // Create the Homework, which fails.

        restHomeworkMockMvc.perform(post("/api/homework")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(homework)))
                .andExpect(status().isBadRequest());

        List<Homework> homework = homeworkRepository.findAll();
        assertThat(homework).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHomework() throws Exception {
        // Initialize the database
        homeworkRepository.saveAndFlush(homework);

        // Get all the homework
        restHomeworkMockMvc.perform(get("/api/homework?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(homework.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE_STR)));
    }

    @Test
    @Transactional
    public void getHomework() throws Exception {
        // Initialize the database
        homeworkRepository.saveAndFlush(homework);

        // Get the homework
        restHomeworkMockMvc.perform(get("/api/homework/{id}", homework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(homework.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.due").value(DEFAULT_DUE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHomework() throws Exception {
        // Get the homework
        restHomeworkMockMvc.perform(get("/api/homework/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHomework() throws Exception {
        // Initialize the database
        homeworkRepository.saveAndFlush(homework);
        homeworkSearchRepository.save(homework);
        int databaseSizeBeforeUpdate = homeworkRepository.findAll().size();

        // Update the homework
        Homework updatedHomework = homeworkRepository.findOne(homework.getId());
        updatedHomework
                .title(UPDATED_TITLE)
                .due(UPDATED_DUE);

        restHomeworkMockMvc.perform(put("/api/homework")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHomework)))
                .andExpect(status().isOk());

        // Validate the Homework in the database
        List<Homework> homework = homeworkRepository.findAll();
        assertThat(homework).hasSize(databaseSizeBeforeUpdate);
        Homework testHomework = homework.get(homework.size() - 1);
        assertThat(testHomework.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHomework.getDue()).isEqualTo(UPDATED_DUE);

        // Validate the Homework in ElasticSearch
        Homework homeworkEs = homeworkSearchRepository.findOne(testHomework.getId());
        assertThat(homeworkEs).isEqualToComparingFieldByField(testHomework);
    }

    @Test
    @Transactional
    public void deleteHomework() throws Exception {
        // Initialize the database
        homeworkRepository.saveAndFlush(homework);
        homeworkSearchRepository.save(homework);
        int databaseSizeBeforeDelete = homeworkRepository.findAll().size();

        // Get the homework
        restHomeworkMockMvc.perform(delete("/api/homework/{id}", homework.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean homeworkExistsInEs = homeworkSearchRepository.exists(homework.getId());
        assertThat(homeworkExistsInEs).isFalse();

        // Validate the database is empty
        List<Homework> homework = homeworkRepository.findAll();
        assertThat(homework).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHomework() throws Exception {
        // Initialize the database
        homeworkRepository.saveAndFlush(homework);
        homeworkSearchRepository.save(homework);

        // Search the homework
        restHomeworkMockMvc.perform(get("/api/_search/homework?query=id:" + homework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homework.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].due").value(hasItem(DEFAULT_DUE_STR)));
    }
}
