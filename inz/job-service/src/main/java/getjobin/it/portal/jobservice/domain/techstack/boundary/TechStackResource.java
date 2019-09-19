package getjobin.it.portal.jobservice.domain.techstack.boundary;

import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.api.TechStackDTO;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServicePreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = TechStackResource.MAIN_PATH)
public class TechStackResource {

    static final String MAIN_PATH = "techStack";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    private TechStackMapper techStackMapper;
    private TechStackService techStackService;

    @Autowired
    public TechStackResource(TechStackMapper techStackMapper, TechStackService techStackService) {
        this.techStackMapper = techStackMapper;
        this.techStackService = techStackService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<TechStackDTO> browseTechStacks(@PathVariable(IDS) IdsParam ids) {
        return techStackService.findByIds(ids.asList()).stream()
                .map(techStackMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDTO> createTechStacks(@RequestBody List<TechStackDTO> techStackDTOs) {
        return techStackMapper.toEntities(techStackDTOs).stream()
                .map(techStackService::createTechStack)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO buildResourceDTO(Long techStackId) {
        return ResourceDTO.builder()
                .objectType(TechStack.TECH_STACK_TYPE)
                .objectId(techStackId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + techStackId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public List<ResourceDTO> updateTechStacks(List<TechStackDTO> techStackDTOs) {
        JobServicePreconditions.checkArgument(allTechStackDTOsContainUniqueIds(techStackDTOs),
                "Specify unique id in each DTO in order to update tech stacks");
        return getUpdatedTechStacks(techStackDTOs).stream()
                .map(techStackService::updateTechStack)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private boolean allTechStackDTOsContainUniqueIds(List<TechStackDTO> techStackDTOs) {
        return techStackDTOs.stream()
                .map(TechStackDTO::getId)
                .distinct()
                .count() == techStackDTOs.size();
    }

    private List<TechStack> getUpdatedTechStacks(List<TechStackDTO> techStackDTOs) {
        List<TechStack> updatedTechStacks = new ArrayList<>();
        techStackDTOs.forEach(techStackDTO -> {
            techStackService.findById(techStackDTO.getId())
                    .map(foundTechStack -> techStackMapper.updateExistingTechStack(foundTechStack, techStackDTO))
                    .ifPresent(updatedTechStacks::add);
        });
        return updatedTechStacks;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTechStacks(@PathVariable(IDS) IdsParam ids) {
        techStackService.findByIds(ids.asList())
                .forEach(techStackService::removeTechStack);
    }

}
