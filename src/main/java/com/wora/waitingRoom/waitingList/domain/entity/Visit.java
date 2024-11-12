package com.wora.waitingRoom.waitingList.domain.entity;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalTime;

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

    private LocalTime arrivalTime;

    private LocalTime startTime;

    private LocalTime endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Byte priority;

    private Duration estimatedProcessingTime;

    @ManyToOne
    @JoinColumn(name = "visitor_id", insertable = false, updatable = false)
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "waiting_list_id", insertable = false, updatable = false)
    private WaitingList waitingList;

    public Visit(Visitor visitor, WaitingList waitingList, Byte priority, Duration estimatedProcessingTime) {
        this.id = new VisitId(visitor.getId(), waitingList.getId());
        this.visitor = visitor;
        this.waitingList = waitingList;
        this.priority = priority;
        this.estimatedProcessingTime = estimatedProcessingTime;
    }

    public Visit cancelVisit() {
        this.status = Status.CANCELED;
        this.endDate = LocalTime.now();
        return this;
    }

    public Visit beginVisit() {
        this.status = Status.IN_PROGRESS;
        this.startTime = LocalTime.now();
        return this;
    }

    public Visit completeVisit() {
        this.status = Status.FINISHED;
        this.endDate = LocalTime.now();
        return this;
    }

    public boolean isInProgress() {
        return this.status == Status.IN_PROGRESS;
    }

    public boolean isPending() {
        return this.status == Status.IN_PROGRESS;
    }
}
