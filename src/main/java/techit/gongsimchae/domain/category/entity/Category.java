package techit.gongsimchae.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private Long categoryNumber;



    public Category (String name, Long categoryNumber){
        this.name = name;
        this.categoryNumber = categoryNumber;
    }
}
