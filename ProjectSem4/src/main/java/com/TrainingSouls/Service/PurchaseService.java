package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserItem;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final StoreItemService storeItemService;
    private final PointsTransactionRepository pointsTransactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public String purchaseItem(HttpServletRequest request, Integer itemId){
        //lay userId tu header
        Long userId = JWTUtils.getSubjectFromRequest(request);

        // lay user tu DB
        User user = userRepository.findById(userId).orElse(null);
        StoreItem item = storeItemService.getById(itemId);


        if (user == null || item == null) {
            return "User hoặc Item không tồn tại!";
        }

        if (user.getPoints() < item.getPointsRequired()) {
            return "Không đủ points để mua!";
        }


        //tru point
        user.setPoints(user.getPoints() - item.getPointsRequired());

        // Đảm bảo danh sách purchasedItems không null
        if (user.getPurchasedItems() == null) {
            user.setPurchasedItems(new ArrayList<>());
        }

        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItemId(itemId);
        user.getPurchasedItems().add(userItem);

        userRepository.save(user);


        // luu lich su giao dich
        PointsTransaction pointsTransaction = new PointsTransaction();
        pointsTransaction.setUser(user);
        pointsTransaction.setPoints(item.getPointsRequired());
        pointsTransaction.setType(PointsTransaction.TransactionType.SPEND);
        pointsTransaction.setDescription("mua item" + " " + item.getName());
        pointsTransaction.setDate(Instant.now());
        pointsTransaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);

        pointsTransactionRepository.save(pointsTransaction);

        return "Purchase Success";
    }

}
