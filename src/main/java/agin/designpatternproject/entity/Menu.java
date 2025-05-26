package agin.designpatternproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "menus")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_id_seq")
    @SequenceGenerator(name = "menu_id_seq", sequenceName = "menu_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "restaurant_id", nullable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_RESTAURANT_MENU"))
    private Long restaurantId;
}
