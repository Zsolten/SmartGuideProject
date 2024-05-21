package edu.bbte.smartguide.springbackend.dto.outgoing;

import lombok.Data;

@Data
public class LocationOutDto {
    private Long id;
    private String name;
    private String city;
    private String category;
    private String description;
    private String openHours;
    private String prices;
    private String googleMapsLink;
//    private double latitude;
//    private double longitude;
}
