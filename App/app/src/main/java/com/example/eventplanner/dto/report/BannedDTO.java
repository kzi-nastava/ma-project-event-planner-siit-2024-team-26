package com.example.eventplanner.dto.report;

import java.time.LocalDateTime;

public class BannedDTO {
    private String bannedUntil;

    public BannedDTO(String bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public String getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(String bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
