package com.TrainingSouls.DTO.Request;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationContent {
    private String title;
    private String body;
    private String image;
    private Map<String, String> data;
}

