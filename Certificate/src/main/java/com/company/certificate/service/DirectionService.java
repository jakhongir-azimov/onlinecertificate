package com.company.certificate.service;

import com.company.certificate.exception.BadRequest;
import com.company.certificate.model.Direction;
import com.company.certificate.repository.DirectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectionService {
    private DirectionRepository directionRepository;

    public boolean create(Direction direction) {
        directionRepository.save(direction);
        return true;
    }

    public List<Direction> getAll() {
        return directionRepository.findAll();
    }

    public Direction getById(Integer id) {
        Optional<Direction> optional = directionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequest("Direction not found");
        }
        return optional.get();
    }
}
