package edu.bbte.smartguide.springbackend.controller;

import edu.bbte.smartguide.springbackend.dto.incoming.RegionInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.RegionOutDto;
import edu.bbte.smartguide.springbackend.service.RegionService;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("region")
public class RegionController {
    private RegionService service;

    public RegionController(RegionService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<RegionOutDto> getAllRegions(@RequestParam(required = false) String name) {
        return service.getAllRegions(name);
    }

    @PostMapping
    public RegionOutDto createRegion(@RequestBody @Valid RegionInDto region) {
        return service.createRegion(region);
    }

    @PutMapping("/{id}")
    public void updateRegion(@RequestBody @Valid RegionInDto regionDto, @PathVariable("id") Long id) {
        service.updateRegion(regionDto, id);
    }

    @GetMapping("/{id}")
    public RegionOutDto getRegionById(@PathVariable("id") Long id) {
        return service.getRegionById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRegionById(@PathVariable("id") Long id) {
        service.deleteRegionById(id);
    }
}
