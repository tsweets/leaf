package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.repository.TransactionRepository;
import org.beer30.leaf.web.rest.dto.TransactionDTO;
import org.beer30.leaf.web.rest.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Transaction.
 */
@Slf4j
@Service
@Transactional
public class TransactionService {


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * Save a transaction.
     *
     * @return the persisted entity
     */
    public TransactionDTO save(TransactionDTO transactionDTO) {
        log.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.transactionDTOToTransaction(transactionDTO);
        transaction = transactionRepository.save(transaction);
        TransactionDTO result = transactionMapper.transactionToTransactionDTO(transaction);
        return result;
    }

    /**
     * get all the transactions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        Page<Transaction> result = transactionRepository.findAll(pageable);
        return result;
    }

    /**
     * get one transaction by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TransactionDTO findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        TransactionDTO transactionDTO = transactionMapper.transactionToTransactionDTO(transaction);
        return transactionDTO;
    }

    /**
     * delete the  transaction by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }

    /**
     * search for the transaction corresponding
     * to the query.
     */
   /* @Transactional(readOnly = true)
    public List<TransactionDTO> search(String query) {
        
        log.debug("REST request to search Transactions for query {}", query);
        return StreamSupport
            .stream(transactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(transactionMapper::transactionToTransactionDTO)
            .collect(Collectors.toList());
    }*/
}
