package org.beer30.leaf.web.rest;


import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.Card;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.repository.CardRepository;
import org.beer30.leaf.service.CardService;
import org.beer30.leaf.web.rest.dto.CardDTO;
import org.beer30.leaf.web.rest.mapper.CardMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LeafApplication.class)
@AutoConfigureMockMvc
public class CardResourceTest {


    private static final Integer DEFAULT_ENV_ID = 1;
    private static final Integer UPDATED_ENV_ID = 2;

    private static final Long DEFAULT_EXTERNAL_ID = 1L;
    private static final Long UPDATED_EXTERNAL_ID = 2L;
    private static final String DEFAULT_CARD_NUMBER = "AAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBB";
    private static final String DEFAULT_DDA_ACCOUNT_NUMBER = "AAAAA";
    private static final String UPDATED_DDA_ACCOUNT_NUMBER = "BBBBB";


    private static final CardStatus DEFAULT_CARD_STATUS = CardStatus.PRE_ACTIVE;
    private static final CardStatus UPDATED_CARD_STATUS = CardStatus.ACTIVE;
    private static final String DEFAULT_IMPRINTED_NAME = "AAAAA";
    private static final String UPDATED_IMPRINTED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

/*
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
*/

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCardMockMvc;

    private Card card;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardResource cardResource = new CardResource();
        ReflectionTestUtils.setField(cardResource, "cardService", cardService);
        ReflectionTestUtils.setField(cardResource, "cardMapper", cardMapper);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
                .setCustomArgumentResolvers(pageableArgumentResolver).build();
        //        .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        card = new Card();
        card.setEnvId(DEFAULT_ENV_ID);
        card.setExternalId(DEFAULT_EXTERNAL_ID);
        card.setCardNumber(DEFAULT_CARD_NUMBER);
        card.setDdaAccountNumber(DEFAULT_DDA_ACCOUNT_NUMBER);
        card.setCardStatus(DEFAULT_CARD_STATUS);
        card.setImprintedName(DEFAULT_IMPRINTED_NAME);
        card.setBalance(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
                .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getEnvId()).isEqualTo(DEFAULT_ENV_ID);
        assertThat(testCard.getExternalId()).isEqualTo(DEFAULT_EXTERNAL_ID);
        assertThat(testCard.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testCard.getDdaAccountNumber()).isEqualTo(DEFAULT_DDA_ACCOUNT_NUMBER);
        assertThat(testCard.getCardStatus()).isEqualTo(DEFAULT_CARD_STATUS);
        assertThat(testCard.getImprintedName()).isEqualTo(DEFAULT_IMPRINTED_NAME);
        assertThat(testCard.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void checkCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setCardNumber(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
                .andExpect(status().isBadRequest());

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setCardStatus(null);

        // Create the Card, which fails.
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(post("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
                .andExpect(status().isBadRequest());

        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cards
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
                .andExpect(jsonPath("$.[*].envId").value(hasItem(DEFAULT_ENV_ID)))
                .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID.intValue())))
                .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
                .andExpect(jsonPath("$.[*].ddaAccountNumber").value(hasItem(DEFAULT_DDA_ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$.[*].cardStatus").value(hasItem(DEFAULT_CARD_STATUS.toString())))
                .andExpect(jsonPath("$.[*].imprintedName").value(hasItem(DEFAULT_IMPRINTED_NAME)))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId().intValue()))
                .andExpect(jsonPath("$.envId").value(DEFAULT_ENV_ID))
                .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.intValue()))
                .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
                .andExpect(jsonPath("$.ddaAccountNumber").value(DEFAULT_DDA_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.cardStatus").value(DEFAULT_CARD_STATUS.toString()))
                .andExpect(jsonPath("$.imprintedName").value(DEFAULT_IMPRINTED_NAME))
                .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        card.setEnvId(UPDATED_ENV_ID);
        card.setExternalId(UPDATED_EXTERNAL_ID);
        card.setCardNumber(UPDATED_CARD_NUMBER);
        card.setDdaAccountNumber(UPDATED_DDA_ACCOUNT_NUMBER);
        card.setCardStatus(UPDATED_CARD_STATUS);
        card.setImprintedName(UPDATED_IMPRINTED_NAME);
        card.setBalance(UPDATED_BALANCE);
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);

        restCardMockMvc.perform(put("/api/cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardDTO)))
                .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cards.get(cards.size() - 1);
        assertThat(testCard.getEnvId()).isEqualTo(UPDATED_ENV_ID);
        assertThat(testCard.getExternalId()).isEqualTo(UPDATED_EXTERNAL_ID);
        assertThat(testCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testCard.getDdaAccountNumber()).isEqualTo(UPDATED_DDA_ACCOUNT_NUMBER);
        assertThat(testCard.getCardStatus()).isEqualTo(UPDATED_CARD_STATUS);
        assertThat(testCard.getImprintedName()).isEqualTo(UPDATED_IMPRINTED_NAME);
        assertThat(testCard.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cards = cardRepository.findAll();
        assertThat(cards).hasSize(databaseSizeBeforeDelete - 1);
    }
}
