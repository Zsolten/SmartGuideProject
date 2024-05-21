package edu.bbte.smartguide.springbackend.dto.outgoing;

import lombok.Data;

@Data
public class RegionOutDto {
    private String name;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private String description;
}
