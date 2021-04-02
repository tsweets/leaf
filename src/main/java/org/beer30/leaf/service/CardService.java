package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.beer30.leaf.domain.Card;
import org.beer30.leaf.repository.CardRepository;
import org.beer30.leaf.web.rest.dto.CardDTO;
import org.beer30.leaf.web.rest.mapper.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Card.
 */
@Slf4j
@Service
@Transactional
public class CardService {


    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;


    public String generateCardNumber(String prefix, String extension) {
        StringBuffer cardNumber = new StringBuffer(prefix);
        cardNumber.append(extension);
        // Need 7 more digits
        String cardRestDigits = RandomStringUtils.randomNumeric(7);
        cardNumber.append(cardRestDigits);

        log.info("Generated Card Number: {}", cardNumber);
        return cardNumber.toString();
    }

    /**
     * Save a card.
     *
     * @return the persisted entity
     */
    public CardDTO save(CardDTO cardDTO) {
        log.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.cardDTOToCard(cardDTO);
        card = cardRepository.save(card);
        CardDTO result = cardMapper.cardToCardDTO(card);

        return result;
    }

    /**
     * get all the cards.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Card> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        Page<Card> result = cardRepository.findAll(pageable);
        return result;
    }

    /**
     * get one card by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CardDTO findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        Card card = cardRepository.findById(id).orElse(null);
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);
        return cardDTO;
    }

    @Transactional(readOnly = true)
    public CardDTO findByExternalId(Integer envId, Long externalId) {
        log.debug("Request to get Card by External ID: {} - ENV {}", externalId, envId);
        Card card = cardRepository.findByExternalIdAndEnvId(externalId, envId);
        CardDTO cardDTO = cardMapper.cardToCardDTO(card);
        return cardDTO;
    }

    /**
     * delete the  card by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }

    /**
     * search for the card corresponding
     * to the query.
     */
    /*@Transactional(readOnly = true)
    public List<CardDTO> search(String query) {

        log.debug("REST request to search Cards for query {}", query);
        return StreamSupport
            .stream(cardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(cardMapper::cardToCardDTO)
            .collect(Collectors.toList());
    }*/
}
