package edu.bbte.smartguide.springbackend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Location")
public class Location extends BaseEntity {
    @NotNull
    private String name;

    @NotNull
    private String city;

    @NotNull
    private String category;

    @NotNull
    @Lob
    private String description;

    @NotNull
    private String openHours;

    @NotNull
    private String prices;

    @NotNull
    private String googleMapsLink;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}
