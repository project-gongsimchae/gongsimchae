package techit.gongsimchae.domain.groupbuying.category.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, name = "category_uid")
    private String categoryUID;


    public Category (String name){
        this.name = name;
        this.categoryUID = UUID.randomUUID().toString().substring(0,8);
    }
}
