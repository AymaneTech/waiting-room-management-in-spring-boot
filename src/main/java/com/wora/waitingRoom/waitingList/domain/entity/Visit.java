package com.wora.waitingRoom.waitingList.domain.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "visits")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Visit {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private VisitId id;

    private LocalDateTime arrivalTime;

    private LocalTime startTime;

    private LocalTime endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Byte priority;

    private Duration estimatedProcessingTime;

    @ManyToOne
    private Visitor visitor;

    @ManyToOne
    private WaitingList waitingList;
}
