package com.marketplace.user.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "UserConnection")
public class UserConnectionBO implements Serializable {

     private static final long serialVersionUID = -5889526109417397633L;

     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Id
     @Column(name = "id", nullable = false)
     private String id;

     @Column(name = "userId", nullable = false)
     private String userId;

     @Column(name = "providerId")
     private String providerId;

     @Column(name = "providerUserId")
     private String providerUserId;

     @Column(name = "rank")
     private int rank;

     @Column(name = "displayName")
     private String displayName;

     @Column(name = "profileUrl")
     private String profileUrl;

     @Column(name = "profileImageUrl")
     private String imageUrl;

     @Column(name = "expireTime")
     private long expireTime;
}
