package getjobin.it.portal.jobservice.domain.category.boundary;

import getjobin.it.portal.jobservice.api.CategoryDto;
import getjobin.it.portal.jobservice.api.ResourceDto;
import getjobin.it.portal.jobservice.domain.category.control.CategoryService;
import getjobin.it.portal.jobservice.domain.category.entity.Category;
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
@RequestMapping(value = CategoryResource.CATEGORY_PATH)
public class CategoryResource {

    public static final String CATEGORY_PATH = "api/category";
    private static final String ID_PATH = "{id}";
    private static final String ID = "id";
    private static final String IDS_PATH = "{ids}";
    private static final String IDS = "ids";

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, value = "all")
    public List<CategoryDto> findAll() {
        List<Category> allCategories = categoryService.findAll();
        return categoryMapper.toDtos(allCategories);
    }

    @RequestMapping(method = RequestMethod.GET, value = IDS_PATH)
    public List<CategoryDto> browseCategories(@PathVariable(IDS) IdsParam ids) {
        List<Category> foundCategories = categoryService.findByIds(ids.asList());
        return categoryMapper.toDtos(foundCategories);
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ResourceDto> createCategories(@RequestBody List<CategoryDto> categoryDTOs) {
        return categoryDTOs.stream()
                .map(categoryMapper::toEntity)
                .map(categoryService::create)
                .map(this::buildResourceDTO)
                .collect(Collectors.toList());
    }

    private ResourceDto buildResourceDTO(Long technologyId) {
        return ResourceDto.builder()
                .objectType(Category.CATEGORY_TYPE)
                .objectId(technologyId)
                .resourceURI(ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/" + technologyId)
                        .build()
                        .toUri())
                .build();
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.PUT, value = ID_PATH)
    public ResourceDto updateCategory(@PathVariable(ID) Long categoryId, @RequestBody CategoryDto categoryDTO) {
        Category existingCategory = categoryService.getById(categoryId);
        Category updatedCategory = categoryMapper.updateExistingCategory(existingCategory, categoryDTO);
        categoryService.update(updatedCategory);
        return buildResourceDTO(updatedCategory.getId());
    }

    @IsAdmin
    @RequestMapping(method = RequestMethod.DELETE, value = IDS_PATH)
    public void deleteCategories(@PathVariable(IDS) IdsParam ids) {
        categoryService.findByIds(ids.asList())
                .forEach(categoryService::remove);
    }
}
