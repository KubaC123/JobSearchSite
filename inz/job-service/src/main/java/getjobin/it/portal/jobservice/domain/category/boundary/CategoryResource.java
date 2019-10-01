package getjobin.it.portal.jobservice.domain.category.boundary;

import getjobin.it.portal.jobservice.api.domain.CategoryDTO;
import getjobin.it.portal.jobservice.api.domain.ResourceDTO;
import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.infrastructure.IdsParam;
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
@RequestMapping(value = CategoryResource.MAIN_PATH)
public class CategoryResource {

    static final String MAIN_PATH = "category";
    private static final String ID_PATH = "{id}";
    private static final String ID = "id";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    private CategoryMapper categoryMapper;
    private CategoryService categoryService;

    @Autowired
    public CategoryResource(CategoryMapper categoryMapper, CategoryService categoryService) {
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<CategoryDTO> browseCategories(@PathVariable(IDS) IdsParam ids) {
        return categoryService.findByIds(ids.asList()).stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDTO> createCategories(List<CategoryDTO> categoryDTOs) {
        return categoryDTOs.stream()
                .map(categoryMapper::toEntity)
                .map(categoryService::createCategory)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDTO buildResourceDTO(Long technologyId) {
        return ResourceDTO.builder()
                .objectType(Category.CATEGORY_TYPE)
                .objectId(technologyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + technologyId)
                        .build()
                        .toUri())
                .build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = ID_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public ResourceDTO updateCategory(@PathVariable(ID) Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        Category existingCategory = categoryService.getById(categoryId);
        Category updatedCategory = categoryMapper.updateExistingCategory(existingCategory, categoryDTO);
        categoryService.updateCategory(updatedCategory);
        return buildResourceDTO(updatedCategory.getId());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCategories(@PathVariable(IDS) IdsParam ids) {
        categoryService.findByIds(ids.asList())
                .forEach(categoryService::removeCategory);
    }
}
