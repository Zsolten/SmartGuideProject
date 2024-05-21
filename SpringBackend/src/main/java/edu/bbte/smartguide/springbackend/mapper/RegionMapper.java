package edu.bbte.smartguide.springbackend.mapper;

import edu.bbte.smartguide.springbackend.dto.incoming.RegionInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.RegionOutDto;
import edu.bbte.smartguide.springbackend.model.Region;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class RegionMapper {
    public abstract Region regionFromDto(RegionInDto locationInDto);

    public abstract RegionOutDto dtoFromRegion(Region region);

    @IterableMapping(elementTargetType = RegionOutDto.class)
    public abstract Collection<RegionOutDto> dtosFromRegions(Collection<Region> regions);
}
