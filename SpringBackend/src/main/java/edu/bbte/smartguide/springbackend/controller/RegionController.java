package edu.bbte.smartguide.springbackend.controller;

import edu.bbte.smartguide.springbackend.controller.exception.NotFoundException;
import edu.bbte.smartguide.springbackend.dao.RegionDao;
import edu.bbte.smartguide.springbackend.dto.incoming.RegionInDto;
import edu.bbte.smartguide.springbackend.dto.outgoing.RegionOutDto;
import edu.bbte.smartguide.springbackend.mapper.RegionMapper;
import edu.bbte.smartguide.springbackend.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("region")
public class RegionController {
    @Autowired
    private RegionDao regionDao;

    @Autowired
    private RegionMapper regionMapper;

    @GetMapping
    public Collection<RegionOutDto> getAllRegions(@RequestParam(required = false) String name) {
        if (name != null) {
            return regionMapper.dtosFromRegions(regionDao.findByName(name));
        }
        return regionMapper.dtosFromRegions(regionDao.findAll());
    }

    @PostMapping
    public RegionOutDto createRegion(@RequestBody @Valid RegionInDto region) {
        return regionMapper.dtoFromRegion(regionDao.saveAndFlush(regionMapper.regionFromDto(region)));
    }

    @PutMapping("/{id}")
    public void updateRegion(@RequestBody @Valid RegionInDto regionDto, @PathVariable("id") Long id) {
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

    @GetMapping("/{id}")
    public RegionOutDto getRegionById(@PathVariable("id") Long id) {
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

    @DeleteMapping("/{id}")
    public void deleteRegionById(@PathVariable("id") Long id) {
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
