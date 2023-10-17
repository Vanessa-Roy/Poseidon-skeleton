package com.springbootskeleton;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.CurvePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurvePointTest {

    @InjectMocks
    CurvePointService curvePointServiceTest;

    @Mock
    CurvePointRepository curvePointRepositoryMock;

    private final CurvePoint curvePoint = new CurvePoint();


    @Test
    public void createCurvePointDoesNotExistShouldCallTheCurvePointRepositorySaveMethodTest() {

        curvePointServiceTest.createCurvePoint(curvePoint);

        verify(curvePointRepositoryMock, Mockito.times(1)).save(curvePoint);
    }

    @Test
    public void updateCurvePointShouldCallTheCurvePointRepositorySaveMethodTest() {

        curvePointServiceTest.updateCurvePoint(curvePoint);

        verify(curvePointRepositoryMock, Mockito.times(1)).save(curvePoint);
    }

    @Test
    public void deleteCurvePointShouldCallTheCurvePointRepositoryDeleteMethodTest() {

        curvePointServiceTest.deleteCurvePoint(curvePoint);

        verify(curvePointRepositoryMock, Mockito.times(1)).delete(curvePoint);
    }

    @Test
    public void loadCurvePointListShouldCallTheCurvePointRepositoryFindAllMethodTest() {

        curvePointServiceTest.loadCurvePointList();

        verify(curvePointRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void loadCurvePointByIdShouldCallTheCurvePointRepositoryFindByIdMethodTest() {

        when(curvePointRepositoryMock.findById(anyInt())).thenReturn(Optional.of(curvePoint));

        curvePointServiceTest.loadCurvePointById(anyInt());

        verify(curvePointRepositoryMock, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void loadCurvePointByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> curvePointServiceTest.loadCurvePointById(2));
        assertEquals("Invalid Curve Point Id:" + 2, exception.getMessage());
    }
}
