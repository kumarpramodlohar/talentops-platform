package com.talentops.domain.jobapplication.dto;

public enum ApplicationStage {

    APPLIED,
    SCREENING,
    INTERVIEW,
    OFFER,
    HIRED,
    REJECTED;

    public boolean canTransitionTo(ApplicationStage next) {
        return switch (this) {
            case APPLIED -> next == SCREENING || next == REJECTED;
            case SCREENING -> next == INTERVIEW || next == REJECTED;
            case INTERVIEW -> next == OFFER || next == REJECTED;
            case OFFER -> next == HIRED || next == REJECTED;
            default -> false;
        };
    }
}

