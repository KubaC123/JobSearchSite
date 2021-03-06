package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.category.entity.Category;
import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.control.enums.EmploymentType;
import getjobin.it.portal.jobservice.domain.job.control.enums.ExperienceLevel;
import getjobin.it.portal.jobservice.domain.job.control.enums.JobType;
import getjobin.it.portal.jobservice.domain.location.entity.Location;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJobBuilder {

    public static String TYPE = JobType.FULL_TIME.getLiteral();
    public static String TYPE_UPDATE = JobType.PART_TIME.getLiteral();
    public static String TITLE = "Super praca w korpo";
    public static String EXP_LEVEL = ExperienceLevel.SENIOR.getLiteral();
    public static String EXP_LEVEL_UPDATE = ExperienceLevel.MID.getLiteral();
    public static String EMP_TYPE = EmploymentType.EMPLOYMENT.getLiteral();
    public static String EMP_TYPE_UPDATE = EmploymentType.B2B.getLiteral();
    public static Integer SALARY_MIN = 4000;
    public static Integer SALARY_MIN_UPDATE = 3000;
    public static Integer SALARY_MAX = 6000;
    public static Integer SALARY_MAX_UPDATE = 4000;
    public static String START_DATE = "ASAP";
    public static String CONTRACT_DURATION = "3 months";
    public static Boolean FLEXIBLE_WORK_HOURS = Boolean.TRUE;
    public static String CURRENCY = "PLN";
    public static String CURRENCY_UPDATE = "EU";
    public static String DESCRIPTION = "Super atmosfera, darmowe przekąski.";
    public static String PROJECT_INDUSTRY = "Banking";
    public static Integer PROJECT_TEAM_SIZE = 5;
    public static Integer PROJECT_TEAM_SIZE_UPDATE = 10;
    public static String PROJECT_DESCRIPTION = "Syfiaty soft";
    public static Integer DEVELOPMENT = 60;
    public static Integer MAINTENANCE = 20;
    public static Integer TESTING = 5;
    public static Integer CLIENT_SUPPORT = 5;
    public static Integer DOCUMENTATION = 5;
    public static Integer OTHER_ACTIVITIES = 100 - DEVELOPMENT - MAINTENANCE - TESTING - CLIENT_SUPPORT - DOCUMENTATION;
    public static String AGREEMENTS = "Przetwarzamy wszystko co się da, sprzedajemy twoje dane.";
    public static String UPDATE = "update";
    public static Integer TECH_STACK_RELATION_EXP_LEVEL = 2;
    public static Boolean LOCATION_RELATION_REMOTE = true;

    public static Job buildValidJob() {
        return Job.builder()
                .type(TYPE)
                .title(TITLE)
                .experienceLevel(EXP_LEVEL)
                .employmentType(EMP_TYPE)
                .salaryMin(SALARY_MIN)
                .salaryMax(SALARY_MAX)
                .startDate(START_DATE)
                .contractDuration(CONTRACT_DURATION)
                .flexibleWorkHours(FLEXIBLE_WORK_HOURS)
                .currency(CURRENCY)
                .description(DESCRIPTION)
                .projectIndustry(PROJECT_INDUSTRY)
                .projectTeamSize(PROJECT_TEAM_SIZE)
                .projectDescription(PROJECT_DESCRIPTION)
                .development(DEVELOPMENT)
                .maintenance(MAINTENANCE)
                .testing(TESTING)
                .clientSupport(CLIENT_SUPPORT)
                .documentation(DOCUMENTATION)
                .agreements(AGREEMENTS)
                .build();
    }

    public static Job buildValidUpdatedJob(Job existingJob) {
        return Job.toBuilder(existingJob)
                .type(TYPE_UPDATE)
                .title(TITLE + UPDATE)
                .experienceLevel(EXP_LEVEL_UPDATE)
                .employmentType(EMP_TYPE_UPDATE)
                .salaryMin(SALARY_MIN_UPDATE)
                .salaryMax(SALARY_MAX_UPDATE)
                .startDate(START_DATE + UPDATE)
                .contractDuration(CONTRACT_DURATION + UPDATE)
                .flexibleWorkHours(!FLEXIBLE_WORK_HOURS)
                .currency(CURRENCY_UPDATE)
                .description(DESCRIPTION + UPDATE)
                .projectIndustry(PROJECT_INDUSTRY + UPDATE)
                .projectTeamSize(PROJECT_TEAM_SIZE_UPDATE)
                .projectDescription(PROJECT_DESCRIPTION + UPDATE)
                .agreements(AGREEMENTS + UPDATE)
                .build();
    }

    public static Job buildJobWithInvalidType() {
        return Job.toBuilder(buildValidJob())
                .type("random")
                .build();
    }

    public static Job buildJobWithInvalidExperienceLevel() {
        return Job.toBuilder(buildValidJob())
                .experienceLevel("random")
                .build();
    }

    public static Job buildJobWithInvalidEmploymentType() {
        return Job.toBuilder(buildValidJob())
                .employmentType("random")
                .build();
    }

    public static Job buildJobWithRelationsToNotExistingTechStacks() {
        return Job.toBuilder(buildValidJob())
                .techStackRelations(getNotExistingTechStacks())
                .build();
    }

    public static List<JobTechStackRelation> getNotExistingTechStacks() {
        return Stream.of(-1L, -2L, -3L, -4L)
                .map(techStackId -> buildJobTechStackRelation(techStackId))
                .collect(Collectors.toList());
    }

    public static List<JobLocationRelation> getNotExistingLocations() {
        return Stream.of(-1L, -2L, -3L, -4L)
                .map(locationId -> buildJobLocationRelation(locationId))
                .collect(Collectors.toList());
    }

    public static Job buildValidJobWithTitle(String title) {
        return Job.toBuilder(buildValidJob())
                .title(title)
                .build();
    }

    public static Job buildValidJobInCompany(Company company) {
        return Job.toBuilder(buildValidJob())
                .company(company)
                .build();
    }

    public static Job buildValidJobWithTechnology(Technology technology) {
        return Job.toBuilder(buildValidJob())
                .technology(technology)
                .build();
    }

    public static Job buildValidJobWithCategory(Category category) {
        return Job.toBuilder(buildValidJob())
                .category(category)
                .build();
    }

    public static Job buildValidJobWithTechStack(TechStack techStack) {
        return Job.toBuilder(buildValidJob())
                .techStackRelations(Collections.singletonList(
                        buildJobTechStackRelation(techStack.getId())))
                .build();
    }

    public static JobTechStackRelation buildJobTechStackRelation(Long techStackId) {
        return JobTechStackRelation.builder()
                .withTechStackId(techStackId)
                .withExperienceLevel(TECH_STACK_RELATION_EXP_LEVEL)
                .build();
    }

    public static Job buildValidJobWith(Company company, Category category, Technology technology,
                                        List<TechStack> techStacks, List<Location> locations) {
        return Job.toBuilder(buildValidJob())
                .company(company)
                .category(category)
                .technology(technology)
                .techStackRelations(getTechStackRelations(techStacks))
                .locationRelations(getLocationRelations(locations))
                .build();
    }

    public static List<JobLocationRelation> getLocationRelations(List<Location> locations) {
        return locations.stream()
                .map(Location::getId)
                .map(locationId -> buildJobLocationRelation(locationId))
                .collect(Collectors.toList());
    }

    public static List<JobTechStackRelation> getTechStackRelations(List<TechStack> techStacks) {
        return techStacks.stream()
                .map(TechStack::getId)
                .map(techStackId -> buildJobTechStackRelation(techStackId))
                .collect(Collectors.toList());
    }

    public static JobLocationRelation buildJobLocationRelation(Long locationId) {
        return JobLocationRelation.builder()
                .locationId(locationId)
                .remote(LOCATION_RELATION_REMOTE)
                .build();
    }

    public static Job buildJobWithOutCompany(Category category, Technology technology,
                                             List<TechStack> techStacks, List<Location> locations) {
        return Job.toBuilder(buildValidJob())
                .category(category)
                .technology(technology)
                .techStackRelations(getTechStackRelations(techStacks))
                .locationRelations(getLocationRelations(locations))
                .build();
    }

    public static Job buildJobWithoutCategory(Company company, Technology technology,
                                              List<TechStack> techStacks, List<Location> locations) {
        return Job.toBuilder(buildValidJob())
                .company(company)
                .technology(technology)
                .techStackRelations(getTechStackRelations(techStacks))
                .locationRelations(getLocationRelations(locations))
                .build();
    }

    public static Job buildJobWithoutTechnology(Company company, Category category,
                                              List<TechStack> techStacks, List<Location> locations) {
        return Job.toBuilder(buildValidJob())
                .company(company)
                .category(category)
                .techStackRelations(getTechStackRelations(techStacks))
                .locationRelations(getLocationRelations(locations))
                .build();
    }

    public static Job buildJobWithoutLocation(Company company, Category category,
                                                Technology technology, List<TechStack> techStacks) {
        return Job.toBuilder(buildValidJob())
                .company(company)
                .category(category)
                .technology(technology)
                .techStackRelations(getTechStackRelations(techStacks))
                .build();
    }
}
