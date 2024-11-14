package com.wora.waitingRoom.waitingList.domain.entity;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.domain.exception.VisitAlreadyCanceledException;
import com.wora.waitingRoom.waitingList.domain.exception.VisitAlreadyCompletedException;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "visits")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
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
        this.priority = priority != null ? priority : (byte) 255;
        this.estimatedProcessingTime = estimatedProcessingTime;
        this.status = Status.WAITING;
        this.arrivalTime = LocalTime.now();
    }

    public Visit cancelVisit() {
        if (status == Status.CANCELED || status != Status.WAITING) {
            throw new VisitAlreadyCompletedException("Visit has already been " + status.name().toLowerCase());
        }
        this.status = Status.CANCELED;
        return this;
    }

    public Visit beginVisit() {
        ensureVisitIsToday();
        if (status == Status.CANCELED)
            throw new VisitAlreadyCanceledException("Visit has already been canceled.");

        if (isInProgress())
            throw new VisitAlreadyCompletedException("Visit has already been started.");

        this.status = Status.IN_PROGRESS;
        this.startTime = LocalTime.now();
        return this;
    }

    public Visit completeVisit() {
        ensureVisitIsToday();
        if (!isInProgress()) {
            throw new VisitAlreadyCompletedException("Visit has already been completed.");
        }
        this.status = Status.FINISHED;
        this.endDate = LocalTime.now();
        return this;
    }

    public boolean isInProgress() {
        return this.status == Status.IN_PROGRESS;
    }

    private void ensureVisitIsToday() {
        if (!waitingList.getDate().isEqual(LocalDate.now()))
            throw new IllegalArgumentException("You can't start or complete or cancel a visit that is not today");
    }
}
