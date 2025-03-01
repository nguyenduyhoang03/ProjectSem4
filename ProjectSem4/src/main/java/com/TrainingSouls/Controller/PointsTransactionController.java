package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Response.PointsTransactionDTO;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Service.PointsTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/PointsTransaction")
public class PointsTransactionController {

    private final PointsTransactionService pointsTransactionService;

    public PointsTransactionController(PointsTransactionService pointsTransactionService) {
        this.pointsTransactionService = pointsTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<PointsTransactionDTO>> getAll() {
        List<PointsTransaction> transactions = pointsTransactionService.getAll();
        List<PointsTransactionDTO> dtos = transactions.stream()
                .map(PointsTransactionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public PointsTransaction getById(@PathVariable int id) {
        return pointsTransactionService.getById(id);
    }


}
