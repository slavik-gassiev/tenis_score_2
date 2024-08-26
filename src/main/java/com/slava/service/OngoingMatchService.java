package com.slava.service;

import com.slava.dto.MatchStateDto;

import java.util.HashMap;
import java.util.Map;

public class OngoingMatchService {
    private static OngoingMatchService INSTANCE;
    private OngoingMatchService() {};

    private volatile Map<String, MatchStateDto> matches = new HashMap<>();

    public synchronized static OngoingMatchService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OngoingMatchService();
        }
        return INSTANCE;
    }
}
