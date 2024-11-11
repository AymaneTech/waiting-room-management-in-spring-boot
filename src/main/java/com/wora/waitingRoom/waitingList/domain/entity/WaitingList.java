package com.wora.waitingRoom.waitingList.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "waiting_lists")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class WaitingList {
    
   @EmbeddedId
   @AttributeOverride(name = "value", column = @Column(name = "id"))
   private WaitingListId id;

   private LocalDateTime date;

   private Integer capacity;

   @Enumerated(EnumType.STRING)
   private Mode mode;

   
    
}
