package org.beer30.leaf.web.rest.mapper;

import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.web.rest.dto.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionMapper {
    /*
        private Long id;
        private Integer envId;
        private TransactionType type;
        private ZonedDateTime date;
        private BigDecimal amount;
        private String note;
        private Long cardId;
     */

    /*  @Autowired
      CardholderRepository cardholderRepository;
  */
    public List<TransactionDTO> transactionsToTransactionDTOs(List<Transaction> transactions) {
        return transactions.stream().filter(Objects::nonNull).map(this::transactionToTransactionDTO).collect(Collectors.toList());
    }

    public List<Transaction> transactionDTOsToTransactions(List<TransactionDTO> TransactionDTOs) {
        return TransactionDTOs.stream().filter(Objects::nonNull).map(this::transactionDTOToTransaction).collect(Collectors.toList());
    }

    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        } else {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId());
            dto.setEnvId(transaction.getEnvId());
            dto.setType(transaction.getType());
            dto.setDate(transaction.getDate());
            dto.setAmount(transaction.getAmount());
            dto.setNote(transaction.getNote());
            dto.setCardId(transaction.getCardId());

            return dto;
        }
    }

    public Transaction transactionDTOToTransaction(TransactionDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Transaction transaction = new Transaction();
            transaction.setId(dto.getId());
            transaction.setEnvId(dto.getEnvId());
            transaction.setType(dto.getType());
            transaction.setDate(dto.getDate());
            transaction.setAmount(dto.getAmount());
            transaction.setNote(dto.getNote());
            transaction.setCardId(dto.getCardId());
            return transaction;
        }
    }
}
