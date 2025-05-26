package agin.designpatternproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "tables")
@AllArgsConstructor
@NoArgsConstructor
public class Table {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_id_seq")
    @SequenceGenerator(name = "table_id_seq", sequenceName = "table_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "location_id")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_LOCATION_TABLE"))
    private Long locationId;

    @Column(name = "is_free", columnDefinition = "BIT")
    private Boolean isFree;
}
