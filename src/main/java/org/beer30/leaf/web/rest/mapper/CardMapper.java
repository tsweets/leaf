package org.beer30.leaf.web.rest.mapper;

import org.beer30.leaf.domain.Card;
import org.beer30.leaf.domain.Cardholder;
import org.beer30.leaf.repository.CardholderRepository;
import org.beer30.leaf.web.rest.dto.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardMapper {
    /*
    Long id;
    private Integer envId;
    private Long externalId;
    private String cardNumber;
    private String ddaAccountNumber;
    private CardStatus cardStatus;
    private String imprintedName;
    private BigDecimal balance;
    private cardholder cardholder;
     */

    @Autowired
    CardholderRepository cardholderRepository;

    public List<CardDTO> cardsToCardDTOs(List<Card> cards) {
        return cards.stream().filter(Objects::nonNull).map(this::cardToCardDTO).collect(Collectors.toList());
    }

    public List<Card> cardDTOsToCards(List<CardDTO> CardDTOs) {
        return CardDTOs.stream().filter(Objects::nonNull).map(this::cardDTOToCard).collect(Collectors.toList());
    }

    public CardDTO cardToCardDTO(Card card) {
        if (card == null) {
            return null;
        } else {
            CardDTO dto = new CardDTO();
            dto.setId(card.getId());
            dto.setEnvId(card.getEnvId());
            dto.setExternalId(card.getExternalId());
            dto.setCardNumber(card.getCardNumber());
            dto.setDdaAccountNumber(card.getDdaAccountNumber());
            dto.setCardStatus(card.getCardStatus());
            dto.setImprintedName(card.getImprintedName());
            dto.setBalance(card.getBalance());
            if (card.getCardholder() != null) {
                dto.setCardholderId(card.getCardholder().getId());
            }

            return dto;
        }
    }

    public Card cardDTOToCard(CardDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Card card = new Card();
            card.setId(dto.getId());
            card.setEnvId(dto.getEnvId());
            card.setExternalId(dto.getExternalId());
            card.setCardNumber(dto.getCardNumber());
            card.setDdaAccountNumber(dto.getDdaAccountNumber());
            card.setCardStatus(dto.getCardStatus());
            card.setImprintedName(dto.getImprintedName());
            card.setBalance(dto.getBalance());
            if (dto.getCardholderId() != null) {
                Cardholder cardholder = cardholderRepository.findById(dto.getCardholderId()).orElse(null);
                card.setCardholder(cardholder);
            }

            return card;
        }
    }
}
