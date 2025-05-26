package agin.designpatternproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
    @SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;
}
