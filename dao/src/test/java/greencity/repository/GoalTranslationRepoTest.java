package greencity.repository;

import greencity.entity.Goal;
import greencity.entity.localization.GoalTranslation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Sql("classpath:sql/goal_translation.sql")
class GoalTranslationRepoTest {

    @Autowired
    GoalTranslationRepo goalTranslationRepo;

    @Test
    void findAllByLanguageCodeTest() {
        List<GoalTranslation> goalTranslations = goalTranslationRepo.findAllByLanguageCode("en");
        assertEquals(3, goalTranslations.size());
        assertEquals("en", goalTranslations.get(0).getLanguage().getCode());
    }

    @Test
    void findAvailableByUserIdTest() {
        List<GoalTranslation> goalTranslations = goalTranslationRepo.findAvailableByUserId(1L, "ua");
        assertEquals(2, goalTranslations.size());
        assertEquals("ua", goalTranslations.get(0).getLanguage().getCode());
    }

    @Test
    void findAvailableByUserIdNotFoundTest() {
        List<GoalTranslation> goalTranslations = goalTranslationRepo.findAvailableByUserId(1L, "xx");
        assertTrue(goalTranslations.isEmpty());
    }

    @Test
    void findByUserIdLangAndUserGoalIdTest() {
        GoalTranslation goalTranslation = goalTranslationRepo.findByLangAndUserGoalId("ru", 2L);
        assertEquals("ru", goalTranslation.getLanguage().getCode());
        assertEquals(2L, goalTranslation.getGoal().getId());
    }

    @Test
    void findByUserIdLangAndUserGoalIdNotFoundTest() {
        GoalTranslation goalTranslation = goalTranslationRepo.findByLangAndUserGoalId("ru", 10L);
        assertNull(goalTranslation);
    }

    @Test
    void findByUserIdLangAndUserGoalIdWithEmptyResultTest() {
        GoalTranslation goalTranslation = goalTranslationRepo.findByLangAndUserGoalId("ru", 3L);
        assertNull(goalTranslation);
    }
}