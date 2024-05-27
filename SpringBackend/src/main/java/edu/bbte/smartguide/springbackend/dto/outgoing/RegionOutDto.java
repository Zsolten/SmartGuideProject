package edu.bbte.smartguide.springbackend.dto.outgoing;

import lombok.Data;

@Data
public class RegionOutDto {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private String description;
}
