package org.beer30.leaf.web.rest;

import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.Cardholder;
import org.beer30.leaf.repository.CardholderRepository;
import org.beer30.leaf.service.CardholderService;
import org.beer30.leaf.web.rest.dto.CardholderDTO;
import org.beer30.leaf.web.rest.mapper.CardholderMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LeafApplication.class)
@AutoConfigureMockMvc
public class CardholderResourceTest {

    private static final Integer DEFAULT_ENV_ID = 1;
    private static final Integer UPDATED_ENV_ID = 2;
    private static final Long DEFAULT_EXTERNAL_ID = 1L;
    private static final Long UPDATED_EXTERNAL_ID = 2L;
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_MIDDLE_NAME = "AAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_SSN = "AAAAA";
    private static final String UPDATED_SSN = "BBBBB";
    private static final String DEFAULT_HOME_STREET1 = "AAAAA";
    private static final String UPDATED_HOME_STREET1 = "BBBBB";
    private static final String DEFAULT_HOME_STREET2 = "AAAAA";
    private static final String UPDATED_HOME_STREET2 = "BBBBB";
    private static final String DEFAULT_HOME_CITY = "AAAAA";
    private static final String UPDATED_HOME_CITY = "BBBBB";
    private static final String DEFAULT_HOME_STATE = "AAAAA";
    private static final String UPDATED_HOME_STATE = "BBBBB";
    private static final String DEFAULT_HOME_POSTAL_CODE = "AAAAA";
    private static final String UPDATED_HOME_POSTAL_CODE = "BBBBB";
    private static final String DEFAULT_SHIP_STREET1 = "AAAAA";
    private static final String UPDATED_SHIP_STREET1 = "BBBBB";
    private static final String DEFAULT_SHIP_STREET2 = "AAAAA";
    private static final String UPDATED_SHIP_STREET2 = "BBBBB";
    private static final String DEFAULT_SHIP_CITY = "AAAAA";
    private static final String UPDATED_SHIP_CITY = "BBBBB";
    private static final String DEFAULT_SHIP_STATE = "AAAAA";
    private static final String UPDATED_SHIP_STATE = "BBBBB";
    private static final String DEFAULT_SHIP_POSTAL_CODE = "AAAAA";
    private static final String UPDATED_SHIP_POSTAL_CODE = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    @Autowired
    CardholderRepository cardholderRepository;
    @Autowired
    CardholderMapper cardholderMapper;
    @Autowired
    CardholderService cardholderService;
    private MockMvc restCardholderMockMvc;
    private Cardholder cardholder;
    @Autowired
    private MockMvc mockMvc;

    /*  @Autowired
      private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  */
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardholderResource cardholderResource = new CardholderResource();
        ReflectionTestUtils.setField(cardholderResource, "cardholderService", cardholderService);
        ReflectionTestUtils.setField(cardholderResource, "cardholderMapper", cardholderMapper);
        this.restCardholderMockMvc = MockMvcBuilders.standaloneSetup(cardholderResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                //    .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        cardholder = new Cardholder();
        cardholder.setEnvId(DEFAULT_ENV_ID);
        cardholder.setExternalId(DEFAULT_EXTERNAL_ID);
        cardholder.setFirstName(DEFAULT_FIRST_NAME);
        cardholder.setMiddleName(DEFAULT_MIDDLE_NAME);
        cardholder.setLastName(DEFAULT_LAST_NAME);
        cardholder.setDob(DEFAULT_DOB);
        cardholder.setSsn(DEFAULT_SSN);
        cardholder.setHomeStreet1(DEFAULT_HOME_STREET1);
        cardholder.setHomeStreet2(DEFAULT_HOME_STREET2);
        cardholder.setHomeCity(DEFAULT_HOME_CITY);
        cardholder.setHomeState(DEFAULT_HOME_STATE);
        cardholder.setHomePostalCode(DEFAULT_HOME_POSTAL_CODE);
        cardholder.setShipStreet1(DEFAULT_SHIP_STREET1);
        cardholder.setShipStreet2(DEFAULT_SHIP_STREET2);
        cardholder.setShipCity(DEFAULT_SHIP_CITY);
        cardholder.setShipState(DEFAULT_SHIP_STATE);
        cardholder.setShipPostalCode(DEFAULT_SHIP_POSTAL_CODE);
        cardholder.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        cardholder.setEmail(DEFAULT_EMAIL);
    }

    @Test
    public void createCardholder() throws Exception {
        int databaseSizeBeforeCreate = cardholderRepository.findAll().size();

        // Create the Cardholder
        CardholderDTO cardholderDTO = cardholderMapper.cardholderToCardholderDTO(cardholder);

        restCardholderMockMvc.perform(post("/api/cardholders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardholderDTO)))
                .andExpect(status().isCreated());

        // Validate the Cardholder in the database
        List<Cardholder> cardholders = cardholderRepository.findAll();
        assertThat(cardholders).hasSize(databaseSizeBeforeCreate + 1);
        Cardholder testCardholder = cardholders.get(cardholders.size() - 1);
        assertThat(testCardholder.getEnvId()).isEqualTo(DEFAULT_ENV_ID);
        assertThat(testCardholder.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testCardholder.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCardholder.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testCardholder.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCardholder.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testCardholder.getSsn()).isEqualTo(DEFAULT_SSN);
        assertThat(testCardholder.getHomeStreet1()).isEqualTo(DEFAULT_HOME_STREET1);
        assertThat(testCardholder.getHomeStreet2()).isEqualTo(DEFAULT_HOME_STREET2);
        assertThat(testCardholder.getHomeCity()).isEqualTo(DEFAULT_HOME_CITY);
        assertThat(testCardholder.getHomeState()).isEqualTo(DEFAULT_HOME_STATE);
        assertThat(testCardholder.getHomePostalCode()).isEqualTo(DEFAULT_HOME_POSTAL_CODE);
        assertThat(testCardholder.getShipStreet1()).isEqualTo(DEFAULT_SHIP_STREET1);
        assertThat(testCardholder.getShipStreet2()).isEqualTo(DEFAULT_SHIP_STREET2);
        assertThat(testCardholder.getShipCity()).isEqualTo(DEFAULT_SHIP_CITY);
        assertThat(testCardholder.getShipState()).isEqualTo(DEFAULT_SHIP_STATE);
        assertThat(testCardholder.getShipPostalCode()).isEqualTo(DEFAULT_SHIP_POSTAL_CODE);
        assertThat(testCardholder.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCardholder.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    public void updateCardholder() {
        Assert.fail();

    }

    @Test
    public void getAllCardholders() {
        Assert.fail();

    }

    @Test
    public void getCardholder() {
        Assert.fail();

    }

    @Test
    public void deleteCardholder() {
        Assert.fail();

    }
}
