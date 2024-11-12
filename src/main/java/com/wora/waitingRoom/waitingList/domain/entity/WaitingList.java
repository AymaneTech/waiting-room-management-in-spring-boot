package com.wora.waitingRoom.waitingList.domain.entity;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDate date;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Enumerated(EnumType.STRING)
    private Algorithm algorithm;

    @OneToMany(mappedBy = "waitingList")
    private List<Visit> visits = new ArrayList<>();

    public WaitingList(Builder builder) {
        this.date = builder.date;
        this.capacity = builder.capacity;
        this.mode = builder.mode;
        this.algorithm = builder.algorithm;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDate date;
        private Integer capacity;
        private Mode mode;
        private Algorithm algorithm;
        private List<Visit> visits;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder capacity(Integer value, Integer defaultValue) {
            capacity = value != null ? value : defaultValue;
            return this;
        }

        public Builder mode(Mode value, Mode defaultValue) {
            mode = value != null ? value : defaultValue;
            return this;
        }

        public Builder algorithm(Algorithm value, Algorithm defaultValue) {
            algorithm = value != null ? value : defaultValue;
            return this;
        }

        public WaitingList build() {
            return new WaitingList(this);
        }
    }
}
