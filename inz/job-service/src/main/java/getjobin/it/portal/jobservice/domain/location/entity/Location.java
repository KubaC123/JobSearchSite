package getjobin.it.portal.jobservice.domain.location.entity;

import getjobin.it.portal.jobservice.domain.ManagedEntity;
import getjobin.it.portal.jobservice.domain.location.entity.validation.LocationUsageValidation;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "LOCATION")
@Getter
@LocationUsageValidation(groups = Location.DeleteValidations.class)
public class Location extends ManagedEntity {

    public static final String LOCATION_TYPE = "Location";
    private static final String COORDINATES_MUST_BE_PROVIDED_MSG = "Coordinates must be provided";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CITY")
    @NotEmpty(message = "City must be provided")
    private String city;

    @Column(name = "STREET")
    @NotEmpty(message = "Street must be provided")
    private String street;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "COUNTRY_NAME")
    private String countryName;

    @Column(name = "POST_CODE")
    private String postCode;

    @Column(name = "LONGITUDE")
    @NotNull(message = COORDINATES_MUST_BE_PROVIDED_MSG)
    private Float longitude;

    @Column(name = "LATITUDE")
    @NotNull(message = COORDINATES_MUST_BE_PROVIDED_MSG)
    private Float latitude;

    public interface DeleteValidations { }

    public Location() { }

    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    private Location(LocationBuilder builder) {
        this.id = builder.id;
        this.city = builder.city;
        this.street = builder.street;
        this.countryCode = builder.countryCode;
        this.countryName = builder.countryName;
        this.postCode = builder.postCode;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
    }

    public static LocationBuilder toBuilder(Location location) {
        return new LocationBuilder()
                .id(location.getId())
                .city(location.getCity())
                .street(location.getStreet())
                .countryCode(location.getCountryCode())
                .countryName(location.getCountryName())
                .postCode(location.getPostCode())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude());
    }

    public static class LocationBuilder {

        private Long id;
        private String city;
        private String street;
        private String countryCode;
        private String countryName;
        private String postCode;
        private Float longitude;
        private Float latitude;

        public LocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LocationBuilder city(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder street(String street) {
            this.street = street;
            return this;
        }

        public LocationBuilder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public LocationBuilder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }

        public LocationBuilder postCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public LocationBuilder longitude(Float longitude) {
            this.longitude = longitude;
            return this;
        }

        public LocationBuilder latitude(Float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
