package getjobin.it.portal.jobservice.domain.location.boundary;

import getjobin.it.portal.jobservice.api.LocationDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsAdmin;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsRecruiter;
import getjobin.it.portal.jobservice.infrastructure.rest.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = LocationResource.LOCATION_PATH)
public class LocationResource {

    public static final String LOCATION_PATH = "api/location";

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET, value = "all")
    public List<LocationDto> findAll() {
        List<Location> allLocations = locationService.findAll();
        return locationMapper.toDtos(allLocations);
    }

    @RequestMapping(method = RequestMethod.GET, value = IdsParam.IDS_PATH)
    public List<LocationDto> browseLocations(@PathVariable(IdsParam.IDS) IdsParam ids) {
        List<Location> foundLocations = locationService.findByIds(ids.asList());
        return locationMapper.toDtos(foundLocations);
    }

    @IsAdmin @IsRecruiter
    @RequestMapping(method = RequestMethod.POST)
    public List<ResourceDto> createLocations(@RequestBody List<LocationDto> locationDtos) {
        return locationDtos.stream()
                .map(locationMapper::toEntity)
                .map(locationService::create)
                .map(this::buildResourceDto)
                .collect(Collectors.toList());
    }

    private ResourceDto buildResourceDto(Long locationId) {
        return ResourceDto.builder()
                .objectType(Location.LOCATION_TYPE)
                .objectId(locationId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + locationId)
                        .build()
                        .toUri())
                .build();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.DELETE, value = IdsParam.IDS_PATH)
    public void deleteLocations(@PathVariable(IdsParam.IDS) IdsParam ids) {
        locationService.findByIds(ids.asList())
                .forEach(locationService::remove);
    }
}
