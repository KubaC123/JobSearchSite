package getjobin.it.portal.jobservice.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import getjobin.it.portal.jobservice.api.CompanyDTO;
import getjobin.it.portal.jobservice.domain.company.boundary.CompanyResource;
import getjobin.it.portal.jobservice.domain.company.control.CompanyMapper;
import getjobin.it.portal.jobservice.domain.company.control.CompanyRepository;
import getjobin.it.portal.jobservice.domain.company.control.CompanyService;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyResource.class)
public class CompanyResourceUnitTest {

    private static final Long TEST_COMPANY_ID = 1L;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyMapper companyMapperMock;

    @MockBean
    private CompanyService companyServiceMock;

    @Before
    public void initMocks() {
//        CompanyDTO companyDTO = buildValidCompanyDTO();
//        Company company = buildValidCompanyEntity();
//        when(companyMapperMock.toEntity(companyDTO)).thenReturn(company);
//        when(companyRepositoryMock.createCompany(company)).thenReturn(TEST_COMPANY_ID);
    }

    private CompanyDTO buildValidCompanyDTO() {
        return CompanyDTO.builder()
                .name(CompanyRepositoryUnitTest.TEST_COMPANY_NAME)
                .webSite(CompanyRepositoryUnitTest.TEST_COMPANY_WEBSITE)
                .size(CompanyRepositoryUnitTest.TEST_COMPANY_SIZE)
                .build();
    }

    private Company buildValidCompanyEntity() {
        return Company.builder()
                .withId(TEST_COMPANY_ID)
                .withName(CompanyRepositoryUnitTest.TEST_COMPANY_NAME)
                .withWebSite(CompanyRepositoryUnitTest.TEST_COMPANY_WEBSITE)
                .withSize(CompanyRepositoryUnitTest.TEST_COMPANY_SIZE)
                .build();
    }

    @Test
    public void givenDependenciesThenTheyAreInjected() {
        assertNotNull(mockMvc);
        assertNotNull(companyServiceMock);
    }

    @Test
    @Transactional
    public void givenValidCompanyThenCreatesItAndReturnsResource() throws Exception {
        CompanyDTO companyDTO = buildValidCompanyDTO();
        Company company = buildValidCompanyEntity();
        when(companyMapperMock.toEntity(companyDTO)).thenReturn(company);
        when(companyServiceMock.createCompany(company)).thenReturn(TEST_COMPANY_ID);

        mockMvc.perform(MockMvcRequestBuilders.post(CompanyResource.MAIN_PATH)
                .content(objectMapper.writeValueAsString(companyDTO))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1L)))
                .andExpect(jsonPath("$.objectType", is(Company.COMPANY_TYPE)));

        verify(companyMapperMock, times(1)).toEntity(any(CompanyDTO.class));
        verify(companyServiceMock, times(1)).createCompany(any(Company.class));
    }
}
