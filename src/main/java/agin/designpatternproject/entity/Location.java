package agin.designpatternproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "locations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_seq")
    @SequenceGenerator(name = "location_id_seq", sequenceName = "location_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "restaurant_id", nullable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_RESTAURANT_LOCATION"))
    private Long restaurantId;
}
