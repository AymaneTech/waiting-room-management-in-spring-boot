package com.wora.waitingroom.waitinglist.domain.vo;

import com.wora.waitingroom.visitor.domain.VisitorId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record VisitId(
        @NotNull
        @AttributeOverride(name = "value", column = @Column(name = "visitor_id"))
        VisitorId visitorId,

        @NotNull
        @AttributeOverride(name = "value", column = @Column(name = "waiting_list_id"))
        WaitingListId waitingListId) {
}
