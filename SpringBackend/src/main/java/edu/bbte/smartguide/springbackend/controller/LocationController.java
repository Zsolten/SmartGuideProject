package edu.bbte.smartguide.springbackend.controller;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import edu.bbte.smartguide.springbackend.controller.exception.InternalServerException;
import edu.bbte.smartguide.springbackend.controller.exception.NotFoundException;
import edu.bbte.smartguide.springbackend.dao.LocationDao;
import edu.bbte.smartguide.springbackend.dto.incoming.LocationInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutSmallDto;
import edu.bbte.smartguide.springbackend.mapper.LocationMapper;
import edu.bbte.smartguide.springbackend.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private GeoApiContext geoApiContext;

    @GetMapping
    public Collection<LocationOutDto> getAllLocations(@RequestParam(required = false) String name) {
        if (name != null) {
            return locationMapper.dtosFromLocations(locationDao.findByName(name));
        }
        return locationMapper.dtosFromLocations(locationDao.findAll());
    }

    @GetMapping("/list") //In case Location Permission isnt granted or cannot get
    public Collection<LocationOutSmallDto> getAllLocationsWithoutDistance() {
        return locationMapper.smallDtosFromLocations(locationDao.findAll());
    }

    @GetMapping("/list/{lat}/{lng}")
    public Collection<LocationOutSmallDto> getAllLocationsWithDistance(@PathVariable("lat") double latPathVariable, @PathVariable("lng") double lngPathVariable) throws IOException, InterruptedException, ApiException {
        Collection<Location> locations = locationDao.findAll();
        Collection<LocationOutSmallDto> locationsDto = locationMapper.smallDtosFromLocations(locations);

        List<LatLng> destinationLatLngs = new ArrayList<>();
        for (Location location : locations) {
            destinationLatLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        // Creating the DistanceMatrixApiRequest
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(geoApiContext);
        request.origins(new LatLng(latPathVariable, lngPathVariable));
        request.destinations(destinationLatLngs.toArray(new LatLng[0]));
        request.mode(TravelMode.DRIVING);

        // Executing the request and getting the distance matrix
        DistanceMatrix distanceMatrix;
        try {
            distanceMatrix = request.await();
        } catch (IOException | InterruptedException | ApiException e) {
            // Handle exceptions for the entire batch request
            for (LocationOutSmallDto dto : locationsDto) {
                dto.setDistance(0.0);
            }
            throw new InternalServerException(">> Getting the locations small list ERROR ");
        }

        // Iterating over the results and update the DTOs
        DistanceMatrixElement element;
        LocationOutSmallDto locationDto;
        Iterator<LocationOutSmallDto> locationOutSmallDtoIterator = locationsDto.iterator();
        for (int i = 0; i < distanceMatrix.rows[0].elements.length; i++) {
            element = distanceMatrix.rows[0].elements[i];
            locationDto = locationOutSmallDtoIterator.next();
            if (element.status == DistanceMatrixElementStatus.OK) {
                locationDto.setDistance(element.distance.inMeters / 1000.0);
            } else {
                locationDto.setDistance(0.0);
            }
        }

        return locationsDto;
    }

    @PostMapping
    public LocationOutDto createLocation(@RequestBody @Valid LocationInDto locationDto) {

        Location location = locationMapper.locationFromDto(locationDto);

        String regex = "@([-\\d.]+),([-\\d.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(location.getGoogleMapsLink());

        if (matcher.find()) {
            // Parsing the latitude and longitude from the matched groups
            location.setLatitude(Double.parseDouble(matcher.group(1)));
            location.setLongitude(Double.parseDouble(matcher.group(2)));
        } else {
            location.setLatitude(0.0);
            location.setLongitude(0.0);
        }

        location = locationDao.saveAndFlush(location);

        if (location == null) {
            throw new InternalServerException(">> Inserting ERROR ");
        } else {
            return locationMapper.dtoFromLocation(location);
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid LocationInDto locationInDto, @PathVariable("id") Long id) {
        try {
            Location location = locationMapper.locationFromDto(locationInDto);

            if (locationDao.getById(id) == null) {
                throw new EntityNotFoundException();
            } else {
                location.setId(id);

                String regex = "@([-\\d.]+),([-\\d.]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(location.getGoogleMapsLink());

                if (matcher.find()) {
                    // Parsing the latitude and longitude from the matched groups
                    location.setLatitude(Double.parseDouble(matcher.group(1)));
                    location.setLongitude(Double.parseDouble(matcher.group(2)));
                } else {
                    location.setLatitude(0.0);
                    location.setLongitude(0.0);
                }

                locationDao.saveAndFlush(location);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Updateing ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public LocationOutDto findById(@PathVariable("id") Long id) {
        try {
            Location result = locationDao.getById(id);
            if (result == null) {
                throw new EntityNotFoundException();
            } else {
                return locationMapper.dtoFromLocation(result);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Find By ID ERROR: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        try {
            Location result = locationDao.getById(id);
            if (id == null || result == null) {
                throw new EntityNotFoundException();
            } else {
                locationDao.deleteById(id);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Delete ERROR: " + e.getMessage());
        }
    }
}
