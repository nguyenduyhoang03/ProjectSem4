package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Response.PointsTransactionDTO;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PointsTransactionService {
    PointsTransactionRepository pointsTransactionRepository;

    public PointsTransactionService(PointsTransactionRepository pointsTransactionRepository) {
        this.pointsTransactionRepository = pointsTransactionRepository;
    }

    public List<PointsTransaction> getAll(){
        return pointsTransactionRepository.findAll();
    }

    public PointsTransaction create(User user, long amount) throws AppException {
        PointsTransaction transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setPoints((int) amount);
        transaction.setType(PointsTransaction.TransactionType.EARN);
        transaction.setDescription("Nạp " + amount + " points qua VNPay");
        transaction.setDate(Instant.now());
        transaction.setStatus(PointsTransaction.TransactionStatus.PENDING);
        return pointsTransactionRepository.save(transaction);
    }


    public PointsTransaction getById(int id){
        return pointsTransactionRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));

    }

}
