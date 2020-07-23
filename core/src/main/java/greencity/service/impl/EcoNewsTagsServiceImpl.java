package greencity.service.impl;

import greencity.constant.ErrorMessage;
import greencity.entity.Tag;
import greencity.exception.exceptions.TagNotFoundException;
import greencity.repository.EcoNewsTagsRepo;
import greencity.service.EcoNewsTagsService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EcoNewsTagsServiceImpl implements EcoNewsTagsService {
    private final EcoNewsTagsRepo tagRepo;

    /**
     * All args constructor.
     *
     * @param tagRepo Repo to get {@link Tag}.
     */
    @Autowired
    public EcoNewsTagsServiceImpl(EcoNewsTagsRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag findByName(String name) {
        return tagRepo.findByNameIgnoreCase(name).orElseThrow(() ->
            new TagNotFoundException(ErrorMessage.TAG_NOT_FOUND + name));
    }

    /**
     * Method that allow you to find all {@link Tag}.
     *
     * @return list of {@link Tag}'s names
     */
    @Override
    public List<String> findAll() {
        return tagRepo.findAll().stream()
            .filter(tag -> !tag.getEcoNews().isEmpty())
            .map(Tag::getName)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAllValid(List<String> tags) {
        for (String tag : tags) {
            try {
                findByName(tag);
            } catch (TagNotFoundException e) {
                return false;
            }
        }
        return true;
    }
}