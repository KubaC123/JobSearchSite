package getjobin.it.portal.jobservice.domain.job.entity;

import getjobin.it.portal.jobservice.domain.company.entity.Company;
import getjobin.it.portal.jobservice.domain.job.control.enums.EmploymentType;
import getjobin.it.portal.jobservice.domain.job.control.enums.ExperienceLevel;
import getjobin.it.portal.jobservice.domain.job.control.enums.JobType;
import getjobin.it.portal.jobservice.domain.technology.entity.Technology;
import getjobin.it.portal.jobservice.domain.techstack.entity.TechStack;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJobBuilder {

    public static String TYPE = JobType.REGULAR.getLiteral();
    public static String TYPE_UPDATE = JobType.FREELANCE.getLiteral();
    public static String TITLE = "Super praca w korpo";
    public static String EXP_LEVEL = ExperienceLevel.SENIOR.getLiteral();
    public static String EXP_LEVEL_UPDATE = ExperienceLevel.MID.getLiteral();
    public static String EMP_TYPE = EmploymentType.EMP_CONTRACT.getLiteral();
    public static String EMP_TYPE_UPDATE = EmploymentType.B2B.getLiteral();
    public static Integer SALARY_MIN = 4000;
    public static Integer SALARY_MIN_UPDATE = 3000;
    public static Integer SALARY_MAX = 6000;
    public static Integer SALARY_MAX_UPDATE = 4000;
    public static String CURRENCY = "PLN";
    public static String CURRENCY_UPDATE = "EU";
    public static String DESCRIPTION = "Super atmosfera, darmowe przekąski.";
    public static String AGREEMENTS = "Przetwarzamy wszystko co się da, sprzedajemy twoje dane.";
    public static Boolean REMOTE = Boolean.FALSE;
    public static String UPDATE = "update";
    public static Integer TECH_STACK_RELATION_EXP_LEVEL = 2;

    public static Job buildValidJob() {
        return Job.builder()
                .type(TYPE)
                .title(TITLE)
                .experienceLevel(EXP_LEVEL)
                .employmentType(EMP_TYPE)
                .salaryMin(SALARY_MIN)
                .salaryMax(SALARY_MAX)
                .currency(CURRENCY)
                .description(DESCRIPTION)
                .agreements(AGREEMENTS)
                .remote(REMOTE)
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
                .currency(CURRENCY_UPDATE)
                .description(DESCRIPTION + UPDATE)
                .agreements(AGREEMENTS + UPDATE)
                .remote(!REMOTE)
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
                .techStackRelations(Stream.of(-1L, -2L, -3L, -4L)
                        .map(techStackId -> JobTechStackRelation.builder()
                                .withTechStackId(techStackId)
                                .withExperienceLevel(TECH_STACK_RELATION_EXP_LEVEL)
                                .build())
                        .collect(Collectors.toList()))
                .build();
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

    public static Job buildValidJobWithTechStack(TechStack techStack) {
        return Job.toBuilder(buildValidJob())
                .techStackRelations(Collections.singletonList(
                        JobTechStackRelation.builder()
                                .withTechStackId(techStack.getId())
                                .withExperienceLevel(TECH_STACK_RELATION_EXP_LEVEL)
                                .build()))
                .build();
    }
}
