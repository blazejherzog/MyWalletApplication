package pl.blazejherzog.mywallet.dto;

import lombok.Data;

@Data
public class BudgetDTO {

    private int budgetId;
    private int amount;
    private String userNickName;

}
