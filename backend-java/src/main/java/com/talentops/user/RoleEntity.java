//package com.talentops.user;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import java.util.UUID;
//
//@Entity
//@Table (name = "roles")
//@Access(AccessType.FIELD)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class RoleEntity {
//
//    @Id
//    @Column(columnDefinition = "uuid")
//    private UUID id;
//
//    @Column(unique = true, nullable = false)
//    private String name;
//
//    private String description;
//
//    public static RoleEntity of(UUID id, String name, String description) {
//        RoleEntity roleEntity = new RoleEntity();
//        roleEntity.id = id;
//        roleEntity.name = name;
//        roleEntity.description = description;
//        return roleEntity;
//
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//}
