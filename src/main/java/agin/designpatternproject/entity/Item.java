package agin.designpatternproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    @SequenceGenerator(name = "item_id_seq", sequenceName = "item_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "dish_name", nullable = false, unique = true)
    private String dishName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "FK_MENU_ITEM"))
    private Long menuId;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "image")
    private String image;
}
