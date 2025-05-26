public class Booking {
    private Long id;
    private Long tableId;
    private Long locationId;
    private Status bookingStatus;
    private LocalDateTime orderTime;
    private Integer noPeople;
    private Double finalPrice;
    private String name;
    private String phoneNumber;
    private String items;
    private String mail;
    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
    }
}

public class Category {
    private Long id;
    private String categoryName;
}

public class Item {
    private Long id;
    private String dishName;
    private Double price;
    private String description;
    private Long menuId;
    private Boolean isAvailable;
    private String image;
}

public class Location {
    private Long id;
    private String address;
    private Long restaurantId;
}

public class Menu {
    private Long id;
    private Long restaurantId;
}

public class Restaurant {
    private Long id;
    private String restaurantName;
    private String logo;
    private Long categoryId;
}

public class Table {
    private Long id;
    private Long locationId;
    private Boolean isFree;
}


public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}


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

public class CategoryDTO {
private Long id;
private String categoryName;
}

public class ItemDTO {
private Long id;
private String dishName;
private Double price;
private String description;
private Boolean isAvailable;
private byte[] image;
private Long menuId;
}

public class LocationDTO {
private Long id;
private String address;
}

public class MenuDTO {
private Long id;
private List<ItemDTO> items;
}

public class RestaurantDTO {
private Long id;
private String restaurantName;
private byte[] logo;
private List<LocationDTO> locations;
}

public class TableDTO {
private Long id;
private Long locationId;
@JsonProperty("isFree")
private Boolean isFree;
}

public class UserDTO {
private String username;
private String password;
private String email;
private String phone;
private Role role;
}
