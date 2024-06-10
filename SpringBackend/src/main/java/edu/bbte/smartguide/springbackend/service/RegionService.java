package edu.bbte.smartguide.springbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import javax.persistence.EntityNotFoundException;

import edu.bbte.smartguide.springbackend.controller.exception.NotFoundException;
import edu.bbte.smartguide.springbackend.dao.RegionDao;
import edu.bbte.smartguide.springbackend.dto.incoming.RegionInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.RegionOutDto;
import edu.bbte.smartguide.springbackend.mapper.RegionMapper;
import edu.bbte.smartguide.springbackend.model.Region;

@Service
public class RegionService {
    @Autowired
    private RegionDao regionDao;

    @Autowired
    private RegionMapper regionMapper;

    public Collection<RegionOutDto> getAllRegions(String name) {
        if (name != null) {
            return regionMapper.dtosFromRegions(regionDao.findByName(name));
        }
        return regionMapper.dtosFromRegions(regionDao.findAll());
    }

    public RegionOutDto createRegion(RegionInDto region) {
        return regionMapper.dtoFromRegion(regionDao.saveAndFlush(regionMapper.regionFromDto(region)));
    }

    public void updateRegion(RegionInDto regionDto, Long id) {
        try {
            Region region = regionMapper.regionFromDto(regionDto);

            if (regionDao.getById(id) == null)
                throw new EntityNotFoundException();

            region.setId(id);
            regionDao.saveAndFlush(region);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Updateing ERROR: " + e.getMessage());
        }
    }

    public RegionOutDto getRegionById(Long id) {
        try {
            Region region = regionDao.getById(id);
            if (region == null) {
                throw new EntityNotFoundException();
            }
            return regionMapper.dtoFromRegion(region);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Find By ID ERROR: " + e.getMessage());
        }
    }

    public void deleteRegionById(Long id) {
        try {
            Region region = regionDao.getById(id);
            if (region == null) {
                throw new EntityNotFoundException();
            }
            regionDao.delete(region);

        } catch (EntityNotFoundException e) {
            throw new NotFoundException(">> Find By ID ERROR: " + e.getMessage());
        }
    }
}
