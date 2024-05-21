package edu.bbte.smartguide.springbackend.dto.outgoing;

import lombok.Data;

@Data
public class LocationOutSmallDto {
    private Long id;
    private String name;
    private String city;
    private String category;
    private double distance;
}
