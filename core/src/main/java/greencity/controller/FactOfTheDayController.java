package greencity.controller;

import greencity.annotations.ApiLocale;
import greencity.annotations.ApiPageable;
import greencity.annotations.ValidLanguage;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.factoftheday.FactOfTheDayDTO;
import greencity.dto.factoftheday.FactOfTheDayTranslationDTO;
import greencity.dto.language.LanguageDTO;
import greencity.entity.FactOfTheDay;
import greencity.service.FactOfTheDayService;
import greencity.service.FactOfTheDayTranslationService;
import greencity.service.LanguageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@AllArgsConstructor
@RestController
@RequestMapping("/factoftheday")
public class FactOfTheDayController {
    private FactOfTheDayService factOfTheDayService;
    private FactOfTheDayTranslationService factOfTheDayTranslationService;
    private LanguageService languageService;

    /**
     * Method which return a random {@link FactOfTheDay}.
     *
     * @param locale string code od language example: en
     * @return {@link FactOfTheDayTranslationDTO}
     */
    @ApiOperation(value = "Get random fact of the day.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = FactOfTheDayTranslationDTO.class),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping("/")
    @ApiLocale
    public ResponseEntity<FactOfTheDayTranslationDTO> getRandomFactOfTheDay(
        @ApiIgnore @AuthenticationPrincipal Principal principal,
        @ApiIgnore @ValidLanguage Locale locale) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(factOfTheDayService.getRandomFactOfTheDayByLanguage(locale.getLanguage()));
    }

    /**
     * Method which return pageable 0f {@link FactOfTheDay}.
     *
     * @return {@link ResponseEntity}
     */
    @ApiPageable
    @ApiOperation(value = "Get all facts of the day.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = PageableDto.class),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping("/all")
    public ResponseEntity<PageableDto<FactOfTheDayDTO>> getAllFactOfTheDay(
        @ApiIgnore @AuthenticationPrincipal Principal principal, @ApiIgnore Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(factOfTheDayService.getAllFactsOfTheDay(pageable));
    }

    /**
     * Method which return {@link FactOfTheDayDTO} by given id.
     *
     * @param id of {@link FactOfTheDay}
     * @return {@link FactOfTheDayDTO}
     */
    @ApiOperation(value = "Find fact of the day by given id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK, response = FactOfTheDayDTO.class),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping("/find")
    public ResponseEntity<FactOfTheDayDTO> findFactOfTheDay(@ApiIgnore @AuthenticationPrincipal Principal principal,
        @RequestParam("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(factOfTheDayService.getFactOfTheDayById(id));
    }

    /**
     * Method which return {@link FactOfTheDayDTO} by given id.
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation(value = "Get all distinguish languages that exists in DB")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDTO>> getLanguages(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(languageService.getAllLanguages());
    }
}
