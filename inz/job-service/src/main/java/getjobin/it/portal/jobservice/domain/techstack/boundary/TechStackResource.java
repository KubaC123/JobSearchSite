package getjobin.it.portal.jobservice.domain.techstack.boundary;

import getjobin.it.portal.jobservice.api.ResourceDTO;
import getjobin.it.portal.jobservice.api.TechStackDTO;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackMapper;
import getjobin.it.portal.jobservice.domain.techstack.control.TechStackRepository;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
import getjobin.it.portal.jobservice.infrastructure.exceptions.JobServicePreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = TechStackResource.MAIN_PATH)
public class TechStackResource {

    static final String MAIN_PATH = "techStack";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    private TechStackMapper techStackMapper;
    private TechStackRepository techStackRepository;

    @Autowired
    public TechStackResource(TechStackMapper techStackMapper, TechStackRepository techStackRepository) {
        this.techStackMapper = techStackMapper;
        this.techStackRepository = techStackRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<TechStackDTO> browseTechStacks(@PathVariable(IDS) IdsParam ids) {
        return techStackRepository.findByIds(ids.asList()).stream()
                .map(techStackMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDTO> createTechStacks(List<TechStackDTO> techStackDTOs) {
        List<Long> createdTechStacksIds = techStackRepository.createTechStacks(techStackMapper.toEntities(techStackDTOs));
        return createdTechStacksIds.stream()
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
                .map(techStackRepository::updateTechStack)
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
            Optional.ofNullable(techStackRepository.findById(techStackDTO.getId()))
                    .map(foundTechStack -> techStackMapper.updateExistingTechStack(foundTechStack, techStackDTO))
                    .ifPresent(updatedTechStacks::add);
        });
        return updatedTechStacks;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCompanies(@PathVariable(IDS) IdsParam ids) {
        ids.asSet().forEach(techStackRepository::removeTechStackById);
    }

}
