package com.wora.waitingRoom.visitor.domain;

import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "visitors", uniqueConstraints = @UniqueConstraint(columnNames = { "first_name", "last_name" }))

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Visitor {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private VisitorId id;

    @Embedded
    private Name name;

    @OneToMany
    private List<Visit> visits;

    public Visitor(String firstName, String lastName) {
        this.name = new Name(firstName, lastName);
    }
}
