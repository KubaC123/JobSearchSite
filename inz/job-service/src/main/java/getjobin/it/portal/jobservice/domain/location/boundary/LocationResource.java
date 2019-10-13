package getjobin.it.portal.jobservice.domain.location.boundary;

import getjobin.it.portal.jobservice.api.LocationDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.location.control.LocationService;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = LocationResource.LOCATION_PATH)
public class LocationResource {

    static final String LOCATION_PATH = "location";

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET)
    public List<LocationDto> browseLocations(@RequestParam(IdsParam.IDS) IdsParam ids) {
        return locationService.findByIds(ids.asList()).stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

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

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteLocations(@RequestParam(IdsParam.IDS) IdsParam ids) {
        locationService.findByIds(ids.asList())
                .forEach(locationService::remove);
    }
}
