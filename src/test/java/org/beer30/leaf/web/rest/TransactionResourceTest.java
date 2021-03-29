package org.beer30.leaf.web.rest;

import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.domain.config.JacksonConfiguration;
import org.beer30.leaf.domain.enumeration.TransactionCode;
import org.beer30.leaf.repository.TransactionRepository;
import org.beer30.leaf.service.TransactionService;
import org.beer30.leaf.web.rest.dto.TransactionDTO;
import org.beer30.leaf.web.rest.mapper.TransactionMapper;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransactionResource REST controller.
 *
 * @see TransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LeafApplication.class)
@AutoConfigureMockMvc
public class TransactionResourceTest {

    //private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;

    private static final Integer DEFAULT_ENV_ID = 1;
    private static final Integer UPDATED_ENV_ID = 2;


    private static final TransactionCode DEFAULT_CODE = TransactionCode.ACH_ADD_FUNDS;
    private static final TransactionCode UPDATED_CODE = TransactionCode.CARD_TO_CARD;
    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now();
    /*private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    */
    //  private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_DATE_STR = "0.0";

    private static final Money DEFAULT_AMOUNT = Money.of(CurrencyUnit.USD, 1.00);
    private static final Money UPDATED_AMOUNT = Money.of(CurrencyUnit.USD, 2);
    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;
    /*

         @Autowired
         private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    */
    @Autowired
    private JacksonConfiguration jacksonConfiguration;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionResource transactionResource = new TransactionResource();
        ReflectionTestUtils.setField(transactionResource, "transactionService", transactionService);
        ReflectionTestUtils.setField(transactionResource, "transactionMapper", transactionMapper);
        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonConfiguration).build();
    }

    @Before
    public void initTest() {
        transaction = new Transaction();
        transaction.setEnvId(DEFAULT_ENV_ID);
        transaction.setTransactionCode(DEFAULT_CODE);
        transaction.setDate(DEFAULT_DATE);
        transaction.setAmount(DEFAULT_AMOUNT);
        transaction.setNote(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);


        byte[] jsonBytes = TestUtil.convertObjectToJsonBytes(transactionDTO);

        restTransactionMockMvc.perform(post("/api/transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(jsonBytes))
                .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactions.get(transactions.size() - 1);
        assertThat(testTransaction.getEnvId()).isEqualTo(DEFAULT_ENV_ID);
        assertThat(testTransaction.getTransactionCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTransaction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransaction.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void checkEnvIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setEnvId(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
                .andExpect(status().isBadRequest());

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionCode(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
                .andExpect(status().isBadRequest());

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setDate(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
                .andExpect(status().isBadRequest());

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactions

        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].envId").value(hasItem(DEFAULT_ENV_ID)))
                // .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_CODE.toString())))
                //      .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                //       .andExpect(jsonPath("$.[*].amount").value(hasItem("<{amount=1.0, currency=USD}>")))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
                .andExpect(jsonPath("$.envId").value(DEFAULT_ENV_ID))
                .andExpect(jsonPath("$.transactionCode").value(DEFAULT_CODE.toString()))
                // .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
                //TODO       .andExpect(jsonPath("$.amount.amount").value(DEFAULT_AMOUNT.getAmount()))
                .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        transaction.setEnvId(UPDATED_ENV_ID);
        transaction.setTransactionCode(UPDATED_CODE);
        transaction.setDate(UPDATED_DATE);
        transaction.setAmount(UPDATED_AMOUNT);
        transaction.setNote(UPDATED_NOTE);
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);

        restTransactionMockMvc.perform(put("/api/transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
                .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactions.get(transactions.size() - 1);
        assertThat(testTransaction.getEnvId()).isEqualTo(UPDATED_ENV_ID);
        assertThat(testTransaction.getTransactionCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransaction.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Get the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
