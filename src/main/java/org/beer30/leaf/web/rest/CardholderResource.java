package org.beer30.leaf.web.rest;


import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.Cardholder;
import org.beer30.leaf.service.CardholderService;
import org.beer30.leaf.web.rest.dto.CardholderDTO;
import org.beer30.leaf.web.rest.mapper.CardholderMapper;
import org.beer30.leaf.web.rest.util.HeaderUtil;
import org.beer30.leaf.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Cardholder.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CardholderResource {


    @Autowired
    private CardholderService cardholderService;

    @Autowired
    private CardholderMapper cardholderMapper;

    /**
     * POST  /cardholders -> Create a new cardholder.
     */
    @RequestMapping(value = "/cardholders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<CardholderDTO> createCardholder(@Valid @RequestBody CardholderDTO cardholderDTO) throws URISyntaxException {
        log.debug("REST request to save Cardholder : {}", cardholderDTO);
        if (cardholderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cardholder", "idexists", "A new cardholder cannot already have an ID")).body(null);
        }
        CardholderDTO result = cardholderService.save(cardholderDTO);
        return ResponseEntity.created(new URI("/api/cardholders/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("cardholder", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /cardholders -> Updates an existing cardholder.
     */
    @RequestMapping(value = "/cardholders",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<CardholderDTO> updateCardholder(@Valid @RequestBody CardholderDTO cardholderDTO) throws URISyntaxException {
        log.debug("REST request to update Cardholder : {}", cardholderDTO);
        if (cardholderDTO.getId() == null) {
            return createCardholder(cardholderDTO);
        }
        CardholderDTO result = cardholderService.save(cardholderDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("cardholder", cardholderDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET  /cardholders -> get all the cardholders.
     */
    @RequestMapping(value = "/cardholders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Transactional(readOnly = true)
    public ResponseEntity<List<CardholderDTO>> getAllCardholders(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Cardholders");
        Page<Cardholder> page = cardholderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cardholders");
        return new ResponseEntity<>(page.getContent().stream()
                .map(cardholderMapper::cardholderToCardholderDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /cardholders/:id -> get the "id" cardholder.
     */
    @RequestMapping(value = "/cardholders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<CardholderDTO> getCardholder(@PathVariable Long id) {
        log.debug("REST request to get Cardholder : {}", id);
        CardholderDTO cardholderDTO = cardholderService.findOne(id);
        return Optional.ofNullable(cardholderDTO)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cardholders/:id -> delete the "id" cardholder.
     */
    @RequestMapping(value = "/cardholders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Void> deleteCardholder(@PathVariable Long id) {
        log.debug("REST request to delete Cardholder : {}", id);
        cardholderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cardholder", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cardholders/:query -> search for the cardholder corresponding
     * to the query.                                            F
     */
   /* @RequestMapping(value = "/_search/cardholders/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    
    public List<CardholderDTO> searchCardholders(@PathVariable String query) {
        log.debug("Request to search Cardholders for query {}", query);
        return cardholderService.search(query);
    }*/
}
