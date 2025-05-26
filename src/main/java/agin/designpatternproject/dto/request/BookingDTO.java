package agin.designpatternproject.dto.request;

import agin.designpatternproject.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @Email
    private String mail;
    @Builder.Default
    private Integer noPeople = 0;
    private String preferences;
    @NotNull
    private Long locationId;
    private Long tableId;
    @NotNull
    private List<Long> itemIds;
    private Status status;
    private String bookingDate;
}
