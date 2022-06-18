package pl.blazejherzog.mywallet.dto;

import lombok.Data;
import pl.blazejherzog.mywallet.model.Category;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class SubcategoryDTO {

    private int id;
    private String name;
    private String categoryName;
}
