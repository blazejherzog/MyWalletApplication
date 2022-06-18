package pl.blazejherzog.mywallet.dto;

import lombok.Data;
import pl.blazejherzog.mywallet.model.Budget;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class CategoryDTO {

    private int id;
    private String categoryName;
    private int budgetId;
}
