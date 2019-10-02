package getjobin.it.portal.jobservice.domain.technology.boundary;

import getjobin.it.portal.jobservice.api.domain.rest.ResourceDTO;
import getjobin.it.portal.jobservice.api.domain.rest.TechnologyDTO;
import getjobin.it.portal.jobservice.domain.technology.control.TechnologyService;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.infrastructure.util.IdsParam;
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

    static final String TECHNOLOGY_PATH = "technology";
    private static final String ID_PATH = "{id}";
    private static final String ID = "id";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    private TechnologyMapper technologyMapper;
    private TechnologyService technologyService;

    @Autowired
    public TechnologyResource(TechnologyMapper technologyMapper, TechnologyService technologyService) {
        this.technologyMapper = technologyMapper;
        this.technologyService = technologyService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<TechnologyDTO> browseTechnologies(@PathVariable(IDS)IdsParam ids) {
        return technologyService.findByIds(ids.asList()).stream()
                .map(technologyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDTO> createTechnologies(List<TechnologyDTO> technologyDTOs) {
        return technologyDTOs.stream()
                .map(technologyMapper::toEntity)
                .map(technologyService::create)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO buildResourceDTO(Long technologyId) {
        return ResourceDTO.builder()
                .objectType(Technology.TECHNOLOGY_TYPE)
                .objectId(technologyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + technologyId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = ID_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public ResourceDTO updateTechnology(@PathVariable(ID) Long technologyId, @RequestBody TechnologyDTO technologyDTO) {
        Technology existingTechnology = technologyService.getById(technologyId);
        Technology updatedTechnology = technologyMapper.updateExistingTechnology(existingTechnology, technologyDTO);
        technologyService.update(updatedTechnology);
        return buildResourceDTO(updatedTechnology.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTechnologies(@PathVariable(IDS) IdsParam ids) {
        technologyService.findByIds(ids.asList())
                .forEach(technologyService::remove);
    }

}
