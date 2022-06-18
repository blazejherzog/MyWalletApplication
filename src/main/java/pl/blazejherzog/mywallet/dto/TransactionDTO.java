package pl.blazejherzog.mywallet.dto;

import lombok.Data;
import pl.blazejherzog.mywallet.model.Budget;
import pl.blazejherzog.mywallet.model.Subcategory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
public class TransactionDTO {

    private int id;
    private String name;
    private LocalDate date;
    private int amount;
    private String subcategoryName;
    private int budgetId;
}
