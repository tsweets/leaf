package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.Cardholder;
import org.beer30.leaf.repository.CardholderRepository;
import org.beer30.leaf.web.rest.dto.CardholderDTO;
import org.beer30.leaf.web.rest.mapper.CardholderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CardholderService {

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardholderMapper cardholderMapper;

    /**
     * Save a cardholder.
     *
     * @return the persisted entity
     */
    public CardholderDTO save(CardholderDTO cardholderDTO) {
        log.debug("Request to save Cardholder : {}", cardholderDTO);
        Cardholder cardholder = cardholderMapper.cardholderDTOToCardholder(cardholderDTO);
        cardholder = cardholderRepository.save(cardholder);
        CardholderDTO result = cardholderMapper.cardholderToCardholderDTO(cardholder);
        return result;
    }

    /**
     * get all the cardholders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cardholder> findAll(Pageable pageable) {
        log.debug("Request to get all Cardholders");
        Page<Cardholder> result = cardholderRepository.findAll(pageable);
        return result;
    }

    /**
     * get one cardholder by id.
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CardholderDTO findOne(Long id) {
        log.debug("Request to get Cardholder : {}", id);
        Cardholder cardholder = cardholderRepository.findById(id).orElse(null);
        CardholderDTO cardholderDTO = cardholderMapper.cardholderToCardholderDTO(cardholder);
        return cardholderDTO;
    }

    @Transactional(readOnly = true)
    public CardholderDTO findOneByExternalId(Integer envId, Long externalId) {
        log.debug("Request to get Cardholder External ID: {} ENV: {}", externalId, envId);
        Cardholder cardholder = cardholderRepository.findByExternalIdAndEnvId(externalId, envId);
        CardholderDTO cardholderDTO = cardholderMapper.cardholderToCardholderDTO(cardholder);
        return cardholderDTO;
    }

    /**
     * delete the  cardholder by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cardholder : {}", id);
        cardholderRepository.deleteById(id);
    }

    /**
     * search for the cardholder corresponding
     * to the query.
     */
   /* @Transactional(readOnly = true)
    public List<CardholderDTO> search(String query) {

        log.debug("REST request to search Cardholders for query {}", query);
        return StreamSupport
                .stream(cardholderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .map(cardholderMapper::cardholderToCardholderDTO)
                .collect(Collectors.toList());
    }*/
}
