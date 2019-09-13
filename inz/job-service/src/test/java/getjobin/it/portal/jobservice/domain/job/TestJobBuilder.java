package getjobin.it.portal.jobservice.domain.job;

import getjobin.it.portal.jobservice.domain.joboffer.control.enums.EmploymentType;
import getjobin.it.portal.jobservice.domain.joboffer.control.enums.ExperienceLevel;
import getjobin.it.portal.jobservice.domain.joboffer.entity.Job;
import getjobin.it.portal.jobservice.domain.joboffer.control.enums.JobType;
import getjobin.it.portal.jobservice.domain.joboffer.entity.JobTechStackRelation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestJobBuilder {

    static String TYPE = JobType.REGULAR.getLiteral();
    static String TYPE_UPDATE = JobType.FREELANCE.getLiteral();
    static String TITLE = "Super praca w korpo";
    static String EXP_LEVEL = ExperienceLevel.SENIOR.getLiteral();
    static String EXP_LEVEL_UPDATE = ExperienceLevel.MID.getLiteral();
    static String EMP_TYPE = EmploymentType.EMP_CONTRACT.getLiteral();
    static String EMP_TYPE_UPDATE = EmploymentType.B2B.getLiteral();
    static Integer SALARY_MIN = 4000;
    static Integer SALARY_MIN_UPDATE = 3000;
    static Integer SALARY_MAX = 6000;
    static Integer SALARY_MAX_UPDATE = 4000;
    static String CURRENCY = "PLN";
    static String CURRENCY_UPDATE = "EU";
    static String DESCRIPTION = "Super atmosfera, darmowe przekąski.";
    static String AGREEMENTS = "Przetwarzamy wszystko co się da, sprzedajemy twoje dane.";
    static Boolean REMOTE = Boolean.FALSE;
    static String UPDATE = "update";

    static Job buildValidJob() {
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

    static Job buildValidUpdatedJob(Job existingJob) {
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

    static Job buildJobWithInvalidType() {
        return Job.toBuilder(buildValidJob())
                .type("random")
                .build();
    }

    static Job buildJobWithInvalidExperienceLevel() {
        return Job.toBuilder(buildValidJob())
                .experienceLevel("random")
                .build();
    }

    static Job buildJobWithInvalidEmploymentType() {
        return Job.toBuilder(buildValidJob())
                .employmentType("random")
                .build();
    }

    static Job buildJobWithRelationsToNotExistingTechStacks() {
        return Job.toBuilder(buildValidJob())
                .techStackRelations(Stream.of(-1L, -2L, -3L, -4L)
                        .map(techStackId -> JobTechStackRelation.builder()
                                .withTechStackId(techStackId)
                                .withExperienceLevel(1)
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
