package com.springbootskeleton;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.service.CurvePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurvePointTest {

    @InjectMocks
    CurvePointService curvePointServiceTest;

    @Mock
    CurvePointRepository curvePointRepositoryMock;

    private CurvePointDto curvePointDto;

    private CurvePoint curvePoint;


    private final Timestamp creationDate = Timestamp.from(Instant.now());

    @Test
    public void saveCurvePointDoesNotExistShouldCallTheCurvePointRepositorySaveMethodTest() {

        curvePointDto = new CurvePointDto(null, 1, 1.1, 2.2);

        curvePointServiceTest.saveCurvePoint(curvePointDto);

        verify(curvePointRepositoryMock, Mockito.times(1)).save(any(CurvePoint.class));
    }

    @Test
    public void updateCurvePointShouldCallTheCurvePointRepositorySaveMethodTest() {

        curvePoint = new CurvePoint(1, 1, null, 1.1, 2.2, creationDate);

        curvePointDto = new CurvePointDto(1, 2, 2.2, 3.3);


        curvePointServiceTest.updateCurvePoint(curvePoint, curvePointDto);

        verify(curvePointRepositoryMock, Mockito.times(1)).save(curvePoint);
    }

    @Test
    public void deleteCurvePointShouldCallTheCurvePointRepositoryDeleteMethodTest() {

        curvePoint = new CurvePoint();

        curvePointServiceTest.deleteCurvePoint(curvePoint);

        verify(curvePointRepositoryMock, Mockito.times(1)).delete(curvePoint);
    }

    @Test
    public void loadCurvePointDtoListShouldReturnAllTheCurvePointsDtoTest() {

        List<CurvePoint> curvePointList = new ArrayList<>(List.of(new CurvePoint(1, 1, null, 1.1, 2.2, creationDate)));

        when(curvePointRepositoryMock.findAll()).thenReturn(curvePointList);

        List<CurvePointDto> curvePointDtoList = curvePointServiceTest.loadCurvePointDtoList();

        verify(curvePointRepositoryMock, Mockito.times(1)).findAll();
        assertEquals(curvePointList.get(0).getId(), curvePointDtoList.get(0).getId());
        assertEquals(curvePointList.get(0).getCurveId(), curvePointDtoList.get(0).getCurveId());
        assertEquals(curvePointList.get(0).getTerm(), curvePointDtoList.get(0).getTerm());
        assertEquals(curvePointList.get(0).getValue(), curvePointDtoList.get(0).getValue());
    }

    @Test
    public void loadCurvePointDtoByIdShouldReturnACurvePointDtoTest() {

        CurvePoint curvePoint = new CurvePoint(1, 1, null, 1.1, 2.2, creationDate);

        when(curvePointRepositoryMock.findById(curvePoint.getId())).thenReturn(Optional.of(curvePoint));

        CurvePointDto curvePointDto = curvePointServiceTest.loadCurvePointDtoById(curvePoint.getId());

        verify(curvePointRepositoryMock, Mockito.times(1)).findById(curvePoint.getId());
        assertEquals(curvePoint.getId(), curvePointDto.getId());
        assertEquals(curvePoint.getCurveId(), curvePointDto.getCurveId());
        assertEquals(curvePoint.getTerm(), curvePointDto.getTerm());
        assertEquals(curvePoint.getValue(), curvePointDto.getValue());
    }

    @Test
    public void loadCurvePointDtoByIdWithUnknownIdShouldThrowAnExceptionTest() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> curvePointServiceTest.loadCurvePointDtoById(2));
        assertEquals("Invalid Curve Point Id:" + 2, exception.getMessage());
    }

    @Test
    public void mapToCurvePointDTOShouldReturnACurvePointDtoFromACurvePointEntityTest() {

        curvePoint = new CurvePoint(1, 1, null, 1.1, 2.2, creationDate);

        CurvePointDto curvePointDto = curvePointServiceTest.mapToCurvePointDTO(curvePoint);

        assertEquals(curvePoint.getId(), curvePointDto.getId());
        assertEquals(curvePoint.getCurveId(), curvePointDto.getCurveId());
        assertEquals(curvePoint.getTerm(), curvePointDto.getTerm());
        assertEquals(curvePoint.getValue(), curvePointDto.getValue());
    }
}
