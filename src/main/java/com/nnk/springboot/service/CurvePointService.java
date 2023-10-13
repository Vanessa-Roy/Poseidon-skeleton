package com.nnk.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurvePointService {

    @Autowired
    CurvePointRepository curvePointRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CurvePointDto mapToCurvePointDTO(CurvePoint curvePoint) {
        return new CurvePointDto(
                curvePoint.getId(),curvePoint.getCurveId(),curvePoint.getTerm(),curvePoint.getValue());
    }

    public CurvePointDto loadCurvePointDtoById(Integer id) {
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
        return mapToCurvePointDTO(curvePoint);
    }

    public List<CurvePointDto> loadCurvePointDtoList() {
        List<CurvePointDto> curvePointDtoList = new ArrayList<>();
        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        curvePoints.forEach(curvePoint -> curvePointDtoList.add(mapToCurvePointDTO(curvePoint)));
        return curvePointDtoList;
    }

    public void saveCurvePoint(CurvePointDto curvePointDto) {
        CurvePoint curvePoint = objectMapper.convertValue(curvePointDto, CurvePoint.class);
        curvePoint.setCreationDate(Timestamp.from(Instant.now()));
        curvePointRepository.save(curvePoint);
    }

    public void updateCurvePoint(CurvePoint curvePointToUpdate, CurvePointDto curvePointDto) {
        curvePointToUpdate.setCurveId(curvePointDto.getCurveId());
        curvePointToUpdate.setTerm(curvePointDto.getTerm());
        curvePointToUpdate.setValue(curvePointDto.getValue());
        curvePointRepository.save(curvePointToUpdate);
    }

    public void deleteCurvePoint(CurvePoint curvePointToDelete) {
        curvePointRepository.delete(curvePointToDelete);
    }
}
