package edu.bbte.smartguide.springbackend.dto.outgoing;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LocationOutDto {
    private Long id;
    private String name;
    private String city;
    private String pictureUrl;
    private String category;
    private String description;
    private String openHours;
    private String prices;
    private String googleMapsLink;
//    private double latitude;
//    private double longitude;
}
