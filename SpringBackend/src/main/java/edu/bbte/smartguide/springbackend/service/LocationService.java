package edu.bbte.smartguide.springbackend.service;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityNotFoundException;

import edu.bbte.smartguide.springbackend.controller.exception.InternalServerException;
import edu.bbte.smartguide.springbackend.controller.exception.NotFoundException;
import edu.bbte.smartguide.springbackend.dao.LocationDao;
import edu.bbte.smartguide.springbackend.dto.incoming.LocationInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.LocationOutSmallDto;
import edu.bbte.smartguide.springbackend.mapper.LocationMapper;
import edu.bbte.smartguide.springbackend.model.Location;

@Service
public class LocationService {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private GeoApiContext geoApiContext;

    public Collection<LocationOutDto> getAllLocations(String name) {
        if (name != null) {
            return locationMapper.dtosFromLocations(locationDao.findByName(name));
        }
        return locationMapper.dtosFromLocations(locationDao.findAll());
    }

    public Collection<LocationOutSmallDto> getAllLocationsWithoutDistance() {
        return locationMapper.smallDtosFromLocations(locationDao.findAll());
    }

    public Collection<LocationOutSmallDto> getAllLocationsWithDistance(double latPathVariable, double lngPathVariable) {
        Collection<Location> locations = locationDao.findAll();
        Collection<LocationOutSmallDto> locationsDto = locationMapper.smallDtosFromLocations(locations);

        List<LatLng> destinationLatLngs = new ArrayList<>();
        for (Location location : locations) {
            destinationLatLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(geoApiContext);
        request.origins(new LatLng(latPathVariable, lngPathVariable));
        request.destinations(destinationLatLngs.toArray(new LatLng[0]));
        request.mode(TravelMode.DRIVING);

        DistanceMatrix distanceMatrix;
        try {
            distanceMatrix = request.await();
        } catch (IOException | InterruptedException | ApiException e) {
            for (LocationOutSmallDto dto : locationsDto) {
                dto.setDistance(0.0);
            }
            throw new InternalServerException(">> Getting the locations small list ERROR ");
        }

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

    public LocationOutDto createLocation(LocationInDto locationDto) {

        Location location = locationMapper.locationFromDto(locationDto);

        String regex = "@([-\\d.]+),([-\\d.]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(location.getGoogleMapsLink());

        if (matcher.find()) {
            location.setLatitude(Double.parseDouble(matcher.group(1)));
            location.setLongitude(Double.parseDouble(matcher.group(2)));
        } else {
            location.setLatitude(0.0);
            location.setLongitude(0.0);
        }

        location = locationDao.saveAndFlush(location);

        return locationMapper.dtoFromLocation(location);
    }

    public void updateLocation(LocationInDto locationInDto, Long id) {
        try {
            Location location = locationMapper.locationFromDto(locationInDto);

            locationDao.getById(id);
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
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Updateing ERROR: " + e.getMessage());
        }
    }

    public LocationOutDto findById(Long id) {
        try {
            Location result = locationDao.getById(id);
            return locationMapper.dtoFromLocation(result);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Find By ID ERROR: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        try {
            Location result = locationDao.getById(id);
            locationDao.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Delete ERROR: " + e.getMessage());
        }
    }
}
