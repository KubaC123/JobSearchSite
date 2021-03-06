package getjobin.it.portal.jobservice.domain.technology.boundary;

import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.api.TechnologyDto;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsAdmin;
import getjobin.it.portal.jobservice.infrastructure.rest.IdsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = TechnologyResource.TECHNOLOGY_PATH)
public class TechnologyResource {

    public static final String TECHNOLOGY_PATH = "api/technology";
    private static final String ID_PATH = "{id}";
    private static final String ID = "id";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    @Autowired
    private TechnologyMapper technologyMapper;

    @Autowired
    private TechnologyService technologyService;

    @RequestMapping(method = RequestMethod.GET, value = "all")
    public List<TechnologyDto> findAll() {
        List<Technology> allTechnologies = technologyService.findAll();
        return technologyMapper.toDtos(allTechnologies);
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    public List<TechnologyDto> browseTechnologies(@PathVariable(IDS)IdsParam ids) {
        List<Technology> foundTechnologies = technologyService.findByIds(ids.asList());
        return technologyMapper.toDtos(foundTechnologies);
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDto> createTechnologies(@RequestBody List<TechnologyDto> technologyDtos) {
        return technologyDtos.stream()
                .map(technologyMapper::toEntity)
                .map(technologyService::create)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDto buildResourceDTO(Long technologyId) {
        return ResourceDto.builder()
                .objectType(Technology.TECHNOLOGY_TYPE)
                .objectId(technologyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + technologyId)
                        .build()
                        .toUri())
                .build();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.PUT, value = ID_PATH)
    public ResourceDto updateTechnology(@PathVariable(ID) Long technologyId, @RequestBody TechnologyDto technologyDTO) {
        Technology existingTechnology = technologyService.getById(technologyId);
        Technology updatedTechnology = technologyMapper.updateExistingTechnology(existingTechnology, technologyDTO);
        technologyService.update(updatedTechnology);
        return buildResourceDTO(updatedTechnology.getId());
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    public void deleteTechnologies(@PathVariable(IDS) IdsParam ids) {
        technologyService.findByIds(ids.asList())
                .forEach(technologyService::remove);
    }

}
