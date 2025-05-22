package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String suggestMeal(UserProfile userProfile, LocalDate date) {
        String prompt = "Bạn là một chuyên gia dinh dưỡng.\n" +
                "Hãy gợi ý thực đơn gồm 3 bữa ăn trong ngày (sáng, trưa, tối) cho người tập thể hình với thông tin sau:\n" +
                "- Mục tiêu: " + userProfile.getFitnessGoal() +
                "\n- Giới tính: " + userProfile.getGender() +
                "\n- Tuổi: " + userProfile.getAge() +
                "\n- Chiều cao: " + userProfile.getHeight() + "cm" +
                "\n- Cân nặng: " + userProfile.getWeight() + "kg" +
                "\n- Cấp độ: " + userProfile.getLevel() +
                "\n- Ngày tập luyện: " + date +
                "\n\nTrả lời chính xác theo định dạng sau, không thêm bất kỳ câu nào khác:\n" +
                "Bữa Sáng:\n" +
                "- Nên ăn: (liệt kê món cách nhau bởi dấu phẩy)\n" +
                "- Không nên ăn: (liệt kê món cách nhau bởi dấu phẩy)\n\n" +
                "Bữa Trưa:\n" +
                "- Nên ăn: ...\n" +
                "- Không nên ăn: ...\n\n" +
                "Bữa Tối:\n" +
                "- Nên ăn: ...\n" +
                "- Không nên ăn: ...";

        return chatWithBot(prompt);
    }





    public String chatWithBot(String userInput) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        Map<String, Object> message = Map.of("parts", List.of(Map.of("text", userInput)));
        Map<String, Object> payload = Map.of("contents", List.of(message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        int attempts = 3; // Số lần thử
        for (int i = 0; i < attempts; i++) {
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
                List<Map> candidates = (List<Map>) response.getBody().get("candidates");
                Map firstCandidate = candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List<Map> parts = (List<Map>) content.get("parts");
                return parts.get(0).get("text").toString();
            } catch (HttpServerErrorException.ServiceUnavailable e) {
                if (i == attempts - 1) {
                    return "Dịch vụ hiện tại không khả dụng. Vui lòng thử lại sau.";
                }
                try {
                    Thread.sleep(2000); // Chờ 2 giây trước khi thử lại
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Bot hiện không thể trả lời. Vui lòng thử lại sau.";
            }
        }
        return "Không thể kết nối với dịch vụ.";
    }

}


