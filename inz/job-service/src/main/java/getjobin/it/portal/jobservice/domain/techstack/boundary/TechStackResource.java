package getjobin.it.portal.jobservice.domain.techstack.boundary;

import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.api.TechStackDto;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackService;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsAdmin;
import getjobin.it.portal.jobservice.infrastructure.config.security.IsRecruiter;
import getjobin.it.portal.jobservice.infrastructure.exception.JobServicePreconditions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = TechStackResource.TECH_STACK_PATH)
public class TechStackResource {

    public static final String TECH_STACK_PATH = "api/techStack";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    @Autowired
    private TechStackMapper techStackMapper;

    @Autowired
    private TechStackService techStackService;

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    public List<TechStackDto> browseTechStacks(@PathVariable(IDS) IdsParam ids) {
        return techStackService.findByIds(ids.asList()).stream()
                .map(techStackMapper::toDto)
                .collect(Collectors.toList());
    }

    @IsAdmin @IsRecruiter
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDto> createTechStacks(@RequestBody List<TechStackDto> techStackDtos) {
        return techStackMapper.toEntities(techStackDtos).stream()
                .map(techStackService::create)
                .map(this::buildResourceDto)
                .collect(Collectors.toList());
    }

    private ResourceDto buildResourceDto(Long techStackId) {
        return ResourceDto.builder()
                .objectType(TechStack.TECH_STACK_TYPE)
                .objectId(techStackId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + techStackId)
                        .build()
                        .toUri())
                .build();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.PUT)
    public List<ResourceDto> updateTechStacks(@RequestBody List<TechStackDto> techStackDtos) {
        JobServicePreconditions.checkArgument(allTechStackDtosContainUniqueIds(techStackDtos),
                "Specify unique id in each Dto in order to update tech stacks");
        return getUpdatedTechStacks(techStackDtos).stream()
                .map(techStackService::update)
                .map(this::buildResourceDto)
                .collect(Collectors.toList());
    }

    private boolean allTechStackDtosContainUniqueIds(List<TechStackDto> techStackDtos) {
        return techStackDtos.stream()
                .map(TechStackDto::getId)
                .distinct()
                .count() == techStackDtos.size();
    }

    private List<TechStack> getUpdatedTechStacks(List<TechStackDto> techStackDtos) {
        List<TechStack> updatedTechStacks = new ArrayList<>();
        techStackDtos.forEach(techStackDto -> {
            techStackService.findById(techStackDto.getId())
                    .map(foundTechStack -> techStackMapper.updateExistingTechStack(foundTechStack, techStackDto))
                    .ifPresent(updatedTechStacks::add);
        });
        return updatedTechStacks;
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    public void deleteTechStacks(@PathVariable(IDS) IdsParam ids) {
        techStackService.findByIds(ids.asList())
                .forEach(techStackService::remove);
    }

}
