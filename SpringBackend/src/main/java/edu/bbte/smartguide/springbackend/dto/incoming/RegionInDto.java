package edu.bbte.smartguide.springbackend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegionInDto {
    @NotNull
    private String name;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Double radius;
    @NotNull
    private String description;
}
