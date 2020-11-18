package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.goal.*;
import greencity.dto.language.LanguageTranslationDTO;
import greencity.entity.Goal;
import greencity.entity.localization.GoalTranslation;
import greencity.exception.exceptions.GoalNotFoundException;
import greencity.repository.GoalRepo;
import greencity.repository.GoalTranslationRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoalServiceImpl implements GoalService {
    private final GoalTranslationRepo goalTranslationRepo;
    private final GoalRepo goalRepo;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GoalDto> findAll(String language) {
        return goalTranslationRepo
            .findAllByLanguageCode(language)
            .stream()
            .map(g -> modelMapper.map(g, GoalDto.class))
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LanguageTranslationDTO> saveGoal(GoalPostDto goal) {
        Goal savedGoal = modelMapper.map(goal, Goal.class);
        savedGoal.getTranslations().forEach(a -> a.setGoal(savedGoal));
        goalRepo.save(savedGoal);
        return modelMapper.map(savedGoal.getTranslations(),
            new TypeToken<List<LanguageTranslationDTO>>() {
            }.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LanguageTranslationDTO> update(GoalPostDto goalPostDto) {
        Optional<Goal> optionalGoal = goalRepo.findById(goalPostDto.getGoal().getId());
        if (optionalGoal.isPresent()) {
            Goal updatedGoal = optionalGoal.get();
            List<GoalTranslation> translations = modelMapper.map(goalPostDto.getTranslations(),
                new TypeToken<List<GoalTranslation>>() {
                }.getType());
            updatedGoal.getTranslations()
                .forEach(goalTranslation -> {
                    goalTranslation.setContent(goalPostDto.getTranslations().stream()
                        .filter(newTranslation -> newTranslation.getLanguage().getCode()
                            .equals(goalTranslation.getLanguage().getCode()))
                        .findFirst().get()
                        .getContent());
                });
            goalRepo.save(updatedGoal);
            return modelMapper.map(updatedGoal.getTranslations(),
                new TypeToken<List<LanguageTranslationDTO>>() {
                }.getType());
        } else {
            throw new GoalNotFoundException(ErrorMessage.GOAL_NOT_FOUND_BY_ID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GoalResponseDto findGoalById(Long id) {
        Optional<Goal> goal = goalRepo.findById(id);
        if (goal.isPresent()) {
            GoalResponseDto responseDto = GoalResponseDto.builder().id(goal.get().getId()).build();
            responseDto.setTranslations(modelMapper.map(goal.get().getTranslations(),
                new TypeToken<List<GoalTranslationDTO>>() {
                }.getType()));
            return responseDto;
        } else {
            throw new GoalNotFoundException(ErrorMessage.GOAL_NOT_FOUND_BY_ID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long delete(Long goalId) {
        if (goalRepo.findById(goalId).isPresent()) {
            goalRepo.deleteById(goalId);
        } else {
            throw new GoalNotFoundException(ErrorMessage.GOAL_NOT_FOUND_BY_ID);
        }
        return goalId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableAdvancedDto<GoalManagementDto> findGoalForManagementByPage(Pageable pageable) {
        Page<Goal> goals = goalRepo.findAll(pageable);
        List<GoalManagementDto> goalManagementDtos =
            goals.getContent().stream()
                .map(goal -> modelMapper.map(goal, GoalManagementDto.class))
                .collect(Collectors.toList());
        return new PageableAdvancedDto<>(
            goalManagementDtos,
            goals.getTotalElements(),
            goals.getPageable().getPageNumber(),
            goals.getTotalPages(),
            goals.getNumber(),
            goals.hasPrevious(),
            goals.hasNext(),
            goals.isFirst(),
            goals.isLast());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Long> deleteAllGoalByListOfId(List<Long> listId) {
        listId.forEach(id -> {
            delete(id);
        });
        return listId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableAdvancedDto<GoalManagementDto> searchBy(Pageable paging, String query) {
        Page<Goal> page = goalRepo.searchBy(paging, query);
        List<GoalManagementDto> goals = page.stream()
            .map(goal -> modelMapper.map(goal, GoalManagementDto.class))
            .collect(Collectors.toList());
        return new PageableAdvancedDto<>(
            goals,
            page.getTotalElements(),
            page.getPageable().getPageNumber(),
            page.getTotalPages(),
            page.getNumber(),
            page.hasPrevious(),
            page.hasNext(),
            page.isFirst(),
            page.isLast());
    }
}
