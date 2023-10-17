package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class CurvePointService {

    @Autowired
    CurvePointRepository curvePointRepository;

    public CurvePoint loadCurvePointById(Integer id) {
        return curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Curve Point Id:" + id));
    }

    public List<CurvePoint> loadCurvePointList() {
        return curvePointRepository.findAll();
    }

    public void createCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    public void updateCurvePoint(CurvePoint curvePointToUpdate) {
        curvePointRepository.save(curvePointToUpdate);
    }

    public void deleteCurvePoint(CurvePoint curvePointToDelete) {
        curvePointRepository.delete(curvePointToDelete);
    }
}
