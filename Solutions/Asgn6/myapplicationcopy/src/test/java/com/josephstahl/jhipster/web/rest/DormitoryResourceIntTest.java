package com.josephstahl.jhipster.web.rest;

import com.josephstahl.jhipster.MyapplicationApp;

import com.josephstahl.jhipster.domain.Dormitory;
import com.josephstahl.jhipster.repository.DormitoryRepository;

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
 * Test class for the DormitoryResource REST controller.
 *
 * @see DormitoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyapplicationApp.class)
public class DormitoryResourceIntTest {

    private static final String DEFAULT_DORM_NAME = "AAAAA";
    private static final String UPDATED_DORM_NAME = "BBBBB";

    @Inject
    private DormitoryRepository dormitoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDormitoryMockMvc;

    private Dormitory dormitory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DormitoryResource dormitoryResource = new DormitoryResource();
        ReflectionTestUtils.setField(dormitoryResource, "dormitoryRepository", dormitoryRepository);
        this.restDormitoryMockMvc = MockMvcBuilders.standaloneSetup(dormitoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dormitory createEntity(EntityManager em) {
        Dormitory dormitory = new Dormitory()
                .dormName(DEFAULT_DORM_NAME);
        return dormitory;
    }

    @Before
    public void initTest() {
        dormitory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDormitory() throws Exception {
        int databaseSizeBeforeCreate = dormitoryRepository.findAll().size();

        // Create the Dormitory

        restDormitoryMockMvc.perform(post("/api/dormitories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dormitory)))
                .andExpect(status().isCreated());

        // Validate the Dormitory in the database
        List<Dormitory> dormitories = dormitoryRepository.findAll();
        assertThat(dormitories).hasSize(databaseSizeBeforeCreate + 1);
        Dormitory testDormitory = dormitories.get(dormitories.size() - 1);
        assertThat(testDormitory.getDormName()).isEqualTo(DEFAULT_DORM_NAME);
    }

    @Test
    @Transactional
    public void getAllDormitories() throws Exception {
        // Initialize the database
        dormitoryRepository.saveAndFlush(dormitory);

        // Get all the dormitories
        restDormitoryMockMvc.perform(get("/api/dormitories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dormitory.getId().intValue())))
                .andExpect(jsonPath("$.[*].dormName").value(hasItem(DEFAULT_DORM_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDormitory() throws Exception {
        // Initialize the database
        dormitoryRepository.saveAndFlush(dormitory);

        // Get the dormitory
        restDormitoryMockMvc.perform(get("/api/dormitories/{id}", dormitory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dormitory.getId().intValue()))
            .andExpect(jsonPath("$.dormName").value(DEFAULT_DORM_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDormitory() throws Exception {
        // Get the dormitory
        restDormitoryMockMvc.perform(get("/api/dormitories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDormitory() throws Exception {
        // Initialize the database
        dormitoryRepository.saveAndFlush(dormitory);
        int databaseSizeBeforeUpdate = dormitoryRepository.findAll().size();

        // Update the dormitory
        Dormitory updatedDormitory = dormitoryRepository.findOne(dormitory.getId());
        updatedDormitory
                .dormName(UPDATED_DORM_NAME);

        restDormitoryMockMvc.perform(put("/api/dormitories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDormitory)))
                .andExpect(status().isOk());

        // Validate the Dormitory in the database
        List<Dormitory> dormitories = dormitoryRepository.findAll();
        assertThat(dormitories).hasSize(databaseSizeBeforeUpdate);
        Dormitory testDormitory = dormitories.get(dormitories.size() - 1);
        assertThat(testDormitory.getDormName()).isEqualTo(UPDATED_DORM_NAME);
    }

    @Test
    @Transactional
    public void deleteDormitory() throws Exception {
        // Initialize the database
        dormitoryRepository.saveAndFlush(dormitory);
        int databaseSizeBeforeDelete = dormitoryRepository.findAll().size();

        // Get the dormitory
        restDormitoryMockMvc.perform(delete("/api/dormitories/{id}", dormitory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dormitory> dormitories = dormitoryRepository.findAll();
        assertThat(dormitories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
