package com.wora.waitingRoom.visitor.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

   public Visitor(String firstName, String lastName) {
        this.name = new Name(firstName, lastName);
   }
}
