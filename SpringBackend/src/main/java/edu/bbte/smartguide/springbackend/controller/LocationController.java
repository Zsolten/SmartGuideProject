package edu.bbte.smartguide.springbackend.controller;

import com.google.maps.errors.ApiException;
import edu.bbte.smartguide.springbackend.dto.incoming.LocationInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutSmallDto;
import edu.bbte.smartguide.springbackend.service.LocationService;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public Collection<LocationOutDto> getAllLocations(@RequestParam(required = false) String name) {
        return locationService.getAllLocations(name);
    }

    @GetMapping("/list") //In case Location Permission isnt granted or cannot get
    public Collection<LocationOutSmallDto> getAllLocationsWithoutDistance() {
        return locationService.getAllLocationsWithoutDistance();
    }

    @GetMapping("/list/{lat}/{lng}")
    public Collection<LocationOutSmallDto> getAllLocationsWithDistance(@PathVariable("lat") double latPathVariable, @PathVariable("lng") double lngPathVariable) throws IOException, InterruptedException, ApiException {
        return locationService.getAllLocationsWithDistance(latPathVariable, lngPathVariable);
    }

    @PostMapping
    public LocationOutDto createLocation(@RequestBody @Valid LocationInDto locationDto) {
        return locationService.createLocation(locationDto);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid LocationInDto locationInDto, @PathVariable("id") Long id) {
        locationService.updateLocation(locationInDto, id);
    }

    @GetMapping("/{id}")
    public LocationOutDto findById(@PathVariable("id") Long id) {
        return locationService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        locationService.deleteById(id);
    }

}
