package com.TrainingSouls.Controller;

import com.TrainingSouls.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String userInput = request.get("message");
        String response = chatService.chatWithBot(userInput);
        return ResponseEntity.ok(response);
    }
}

