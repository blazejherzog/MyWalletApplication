package pl.blazejherzog.mywallet.dto;

import lombok.Data;
import pl.blazejherzog.mywallet.model.Category;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Data
public class BudgetedAmountDTO {

    private int id;
    private LocalDate budgetedDate;
    private int budgetedAmount;
    private String categoryName;
}
