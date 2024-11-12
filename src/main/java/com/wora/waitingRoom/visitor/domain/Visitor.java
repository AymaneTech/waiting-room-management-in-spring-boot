package com.wora.waitingRoom.visitor.domain;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visitors", uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name"}))

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

    @OneToMany(mappedBy = "visitor")
    private List<Visit> visits = new ArrayList<>();

    public Visitor(String firstName, String lastName) {
        this.name = new Name(firstName, lastName);
    }

    public Visitor(Long id, String firstName, String lastName) {
        this(firstName, lastName);
        this.id = new VisitorId(id);
    }
}
