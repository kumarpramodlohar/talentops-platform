package com.talentops.user;

public enum Role {
    USER,SUPER_ADMIN,ADMIN, RECRUITER,EMPLOYER, CANDIDATE;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}
