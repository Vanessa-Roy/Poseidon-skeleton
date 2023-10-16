package com.springbootskeleton;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingTest {

    @InjectMocks
    RatingService ratingServiceTest;

    @Mock
    RatingRepository ratingRepositoryMock;

    private RatingDto ratingDto;

    private Rating rating;

    @Test
    public void saveRatingDoesNotExistShouldCallTheRatingRepositorySaveMethodTest() {

        ratingDto = new RatingDto(null, "moodysRating", "sandPRating", "fitchRating", 1);

        ratingServiceTest.saveRating(ratingDto);

        verify(ratingRepositoryMock, Mockito.times(1)).save(any(Rating.class));
    }

    @Test
    public void updateRatingShouldCallTheRatingRepositorySaveMethodTest() {

        rating = new Rating(1, "moodysRating", "sandPRating", "fitchRating", 1);

        ratingDto = new RatingDto(1, "moodysRating2", "sandPRating2", "fitchRating2", 1);


        ratingServiceTest.updateRating(rating, ratingDto);

        verify(ratingRepositoryMock, Mockito.times(1)).save(any(Rating.class));
    }

    @Test
    public void deleteRatingShouldCallTheRatingRepositoryDeleteMethodTest() {

        rating = new Rating(11, "moodysRating", "sandPRating", "fitchRating", 1);

        ratingServiceTest.deleteRating(rating);

        verify(ratingRepositoryMock, Mockito.times(1)).delete(rating);
    }

    @Test
    public void loadRatingDtoListShouldReturnAllTheRatingsDtoTest() {

        List<Rating> ratingList = new ArrayList<>(List.of(new Rating(1, "moodysRating", "sandPRating", "fitchRating", 1)));

        when(ratingRepositoryMock.findAll()).thenReturn(ratingList);

        List<RatingDto> ratingDtoList = ratingServiceTest.loadRatingDtoList();

        verify(ratingRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(ratingList.get(0).getId(), ratingDtoList.get(0).getId());
        assertEquals(ratingList.get(0).getMoodysRating(), ratingDtoList.get(0).getMoodysRating());
        assertEquals(ratingList.get(0).getSandPRating(), ratingDtoList.get(0).getSandPRating());
        assertEquals(ratingList.get(0).getFitchRating(), ratingDtoList.get(0).getFitchRating());
        assertEquals(ratingList.get(0).getOrderNumber(), ratingDtoList.get(0).getOrderNumber());
    }

    @Test
    public void loadRatingDtoByIdShouldReturnARatingDtoTest() {

        Rating rating = new Rating(1, "moodysRating", "sandPRating", "fitchRating", 1);

        when(ratingRepositoryMock.findById(rating.getId())).thenReturn(Optional.of(rating));

        RatingDto ratingDto = ratingServiceTest.loadRatingDtoById(rating.getId());

        verify(ratingRepositoryMock, Mockito.times(1)).findById(rating.getId());
        assertEquals(rating.getId(), ratingDto.getId());
        assertEquals(rating.getMoodysRating(), ratingDto.getMoodysRating());
        assertEquals(rating.getSandPRating(), ratingDto.getSandPRating());
        assertEquals(rating.getFitchRating(), ratingDto.getFitchRating());
        assertEquals(rating.getOrderNumber(), ratingDto.getOrderNumber());
    }

    @Test
    public void loadRatingDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ratingServiceTest.loadRatingDtoById(2));
        assertEquals("Invalid Rating Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToRatingDTOShouldReturnARatingDtoFromARatingEntityTest() {

        rating = new Rating(1, "moodysRating", "sandPRating", "fitchRating", 1);

        RatingDto ratingDto = ratingServiceTest.mapToRatingDTO(rating);

        assertEquals(rating.getId(), ratingDto.getId());
        assertEquals(rating.getMoodysRating(), ratingDto.getMoodysRating());
        assertEquals(rating.getSandPRating(), ratingDto.getSandPRating());
        assertEquals(rating.getFitchRating(), ratingDto.getFitchRating());
        assertEquals(rating.getOrderNumber(), ratingDto.getOrderNumber());
    }
}
