package org.beer30.leaf.web.rest.mapper;

import org.beer30.leaf.domain.Cardholder;
import org.beer30.leaf.web.rest.dto.CardholderDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardholderMapper {

    public List<CardholderDTO> usersToUserDTOs(List<Cardholder> cardholders) {
        return cardholders.stream().filter(Objects::nonNull).map(this::cardholderToCardholderDTO).collect(Collectors.toList());
    }

    public List<Cardholder> userDTOsToUsers(List<CardholderDTO> cardholderDTOs) {
        return cardholderDTOs.stream().filter(Objects::nonNull).map(this::cardholderDTOToCardholder).collect(Collectors.toList());
    }

    public CardholderDTO cardholderToCardholderDTO(Cardholder cardholder) {
        if (cardholder == null) {
            return null;
        } else {
            CardholderDTO dto = new CardholderDTO();
            dto.setId(cardholder.getId());
            dto.setEnvId(cardholder.getEnvId());
            dto.setExternalId(cardholder.getExternalId());
            dto.setFirstName(cardholder.getFirstName());
            dto.setMiddleName(cardholder.getMiddleName());
            dto.setLastName(cardholder.getLastName());
            dto.setDob(cardholder.getDob());
            dto.setSsn(cardholder.getSsn());
            dto.setHomeStreet1(cardholder.getHomeStreet1());
            dto.setHomeStreet2(cardholder.getHomeStreet2());
            dto.setHomeCity(cardholder.getHomeCity());
            dto.setHomeState(cardholder.getHomeState());
            dto.setHomePostalCode(cardholder.getHomePostalCode());
            dto.setShipStreet1(cardholder.getShipStreet1());
            dto.setShipStreet2(cardholder.getShipStreet2());
            dto.setShipCity(cardholder.getShipCity());
            dto.setShipState(cardholder.getShipState());
            dto.setShipPostalCode(cardholder.getShipPostalCode());
            dto.setPhoneNumber(cardholder.getPhoneNumber());
            dto.setEmail(cardholder.getEmail());

            return dto;
        }

    }

    public Cardholder cardholderDTOToCardholder(CardholderDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Cardholder cardholder = new Cardholder();
            cardholder.setId(dto.getId());
            cardholder.setEnvId(dto.getEnvId());
            cardholder.setExternalId(dto.getExternalId());
            cardholder.setFirstName(dto.getFirstName());
            cardholder.setMiddleName(dto.getMiddleName());
            cardholder.setLastName(dto.getLastName());
            cardholder.setDob(dto.getDob());
            cardholder.setSsn(dto.getSsn());
            cardholder.setHomeStreet1(dto.getHomeStreet1());
            cardholder.setHomeStreet2(dto.getHomeStreet2());
            cardholder.setHomeCity(dto.getHomeCity());
            cardholder.setHomeState(dto.getHomeState());
            cardholder.setHomePostalCode(dto.getHomePostalCode());
            cardholder.setShipStreet1(dto.getShipStreet1());
            cardholder.setShipStreet2(dto.getShipStreet2());
            cardholder.setShipCity(dto.getShipCity());
            cardholder.setShipState(dto.getShipState());
            cardholder.setShipPostalCode(dto.getShipPostalCode());
            cardholder.setPhoneNumber(dto.getPhoneNumber());
            cardholder.setEmail(dto.getEmail());

            return cardholder;
        }
    }
}
