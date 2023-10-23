package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.service.RatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingTest {

    @InjectMocks
    RatingService ratingServiceTest;

    @Mock
    RatingRepository ratingRepositoryMock;

    private final Rating rating = new Rating();

    @Test
    public void saveRatingDoesNotExistShouldCallTheRatingRepositorySaveMethodTest() {

        ratingServiceTest.createRating(rating);

        verify(ratingRepositoryMock, Mockito.times(1)).save(rating);
    }

    @Test
    public void updateRatingShouldCallTheRatingRepositorySaveMethodTest() {
        
        ratingServiceTest.updateRating(rating);

        verify(ratingRepositoryMock, Mockito.times(1)).save(rating);
    }

    @Test
    public void deleteRatingShouldCallTheRatingRepositoryDeleteMethodTest() {
        
        ratingServiceTest.deleteRating(rating);

        verify(ratingRepositoryMock, Mockito.times(1)).delete(rating);
    }

    @Test
    public void loadRatingListShouldCallTheRatingRepositoryFindAllMethodTest() {

        ratingServiceTest.loadRatingList();

        verify(ratingRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadRatingByIdShouldCallTheRatingRepositoryFindByIdMethodTest() {

        when(ratingRepositoryMock.findById(anyInt())).thenReturn(Optional.of(rating));

        ratingServiceTest.loadRatingById(anyInt());

        verify(ratingRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadRatingByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ratingServiceTest.loadRatingById(2));
        assertEquals("Invalid Rating Id:" + 2, exception.getMessage());
    }
}
