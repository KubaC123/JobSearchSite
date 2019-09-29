package getjobin.it.portal.jobservice.domain.company;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@RunWith(SpringRunner.class)
//@WebMvcTest(CompanyResource.class)
public class CompanyResourceUnitTest {
//
//    private static final Long TEST_COMPANY_ID = 1L;
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CompanyMapper companyMapperMock;
//
//    @MockBean
//    private CompanyService companyServiceMock;
//
//    @Before
//    public void initMocks() {
////        CompanyDTO companyDTO = buildValidCompanyDTO();
////        Company company = buildValidCompanyEntity();
////        when(companyMapperMock.toEntity(companyDTO)).thenReturn(company);
////        when(companyRepositoryMock.createCompany(company)).thenReturn(TEST_COMPANY_ID);
//    }
//
//    private CompanyDTO buildValidCompanyDTO() {
//        return CompanyDTO.builder()
//                .name(CompanyRepositoryUnitTest.TEST_COMPANY_NAME)
//                .webSite(CompanyRepositoryUnitTest.TEST_COMPANY_WEBSITE)
//                .size(CompanyRepositoryUnitTest.TEST_COMPANY_SIZE)
//                .build();
//    }
//
//    private Company buildValidCompanyEntity() {
//        return Company.builder()
//                .withId(TEST_COMPANY_ID)
//                .withName(CompanyRepositoryUnitTest.TEST_COMPANY_NAME)
//                .withWebSite(CompanyRepositoryUnitTest.TEST_COMPANY_WEBSITE)
//                .withSize(CompanyRepositoryUnitTest.TEST_COMPANY_SIZE)
//                .build();
//    }
//
//    @Test
//    public void givenDependenciesThenTheyAreInjected() {
//        assertNotNull(mockMvc);
//        assertNotNull(companyServiceMock);
//    }
//
//    @Test
//    @Transactional
//    public void givenValidCompanyThenCreatesItAndReturnsResource() throws Exception {
//        CompanyDTO companyDTO = buildValidCompanyDTO();
//        Company company = buildValidCompanyEntity();
//        when(companyMapperMock.toEntity(companyDTO)).thenReturn(company);
//        when(companyServiceMock.createCompany(company)).thenReturn(TEST_COMPANY_ID);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CompanyResource.MAIN_PATH)
//                .content(objectMapper.writeValueAsString(companyDTO))
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1L)))
//                .andExpect(jsonPath("$.objectType", is(Company.COMPANY_TYPE)));
//
//        verify(companyMapperMock, times(1)).toEntity(any(CompanyDTO.class));
//        verify(companyServiceMock, times(1)).createCompany(any(Company.class));
//    }
}
