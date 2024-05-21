package edu.bbte.smartguide.springbackend.mapper;

import edu.bbte.smartguide.springbackend.dto.incoming.LocationInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutSmallDto;
import edu.bbte.smartguide.springbackend.model.Location;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class LocationMapper {
    public abstract Location locationFromDto(LocationInDto locationInDto);

    public abstract LocationOutDto dtoFromLocation(Location location);

    @IterableMapping(elementTargetType = LocationOutDto.class)
    public abstract Collection<LocationOutDto> dtosFromLocations(Collection<Location> location);

    public abstract LocationOutSmallDto smallDtoFromLocation(Location location);

    @IterableMapping(elementTargetType = LocationOutDto.class)
    public abstract Collection<LocationOutSmallDto> smallDtosFromLocations(Collection<Location> location);
}
