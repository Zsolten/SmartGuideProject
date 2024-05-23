package edu.bbte.smartguide.springbackend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LocationInDto {
    @NotNull
    private String name;
    @NotNull
    private String city;
    @NotNull
    private String pictureUrl;
    @NotNull
    private String category;
    @NotNull
    private String description;
    @NotNull
    private String openHours;
    @NotNull
    private String prices;
    @NotNull
    private String googleMapsLink;
//    @NotNull
//    private double latitude;
//    @NotNull
//    private double longitude;
}
