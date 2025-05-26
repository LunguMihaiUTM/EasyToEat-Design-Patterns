package agin.designpatternproject.entity;

import agin.designpatternproject.enums.Status;
import agin.designpatternproject.prototype.Prototype;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity(name = "bookings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Prototype<Booking> {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
    @SequenceGenerator(name = "booking_id_seq", sequenceName = "booking_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "table_id")
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TABLE_BOOKING"))
    private Long tableId;

    @Column(name = "location_id",nullable = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_LOCATION_BOOKING"))
    private Long locationId;

    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    private Status bookingStatus;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "no_people")
    private Integer noPeople;

    @Column(name = "final_price")
    private Double finalPrice;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "items")
    private String items;

    @Column(name = "mail")
    private String mail;

    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
    }



    @Override
    public Booking clone() {
        Booking clone = new Booking();
        clone.tableId = this.tableId;
        clone.locationId = this.locationId;
        clone.bookingStatus = Status.IN_PROGRESS;
        clone.noPeople = this.noPeople;
        clone.finalPrice = this.finalPrice;
        clone.name = this.name;
        clone.phoneNumber = this.phoneNumber;
        clone.items = this.items;
        clone.mail = this.mail;
        return clone;
    }
}