package com.wora.waitingroom.waitinglist.domain.entity;

import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.persistence.*;
import lombok.*;
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
        this.id = builder.id;
        this.date = builder.date;
        this.capacity = builder.capacity;
        this.mode = builder.mode;
        this.algorithm = builder.algorithm;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private WaitingListId id;
        private LocalDate date;
        private Integer capacity;
        private Mode mode;
        private Algorithm algorithm;
        private List<Visit> visits;

        public Builder id(WaitingListId id) {
            this.id = id;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder capacity(Integer value, Integer defaultValue) {
            capacity = value != null ? value : defaultValue;
            return this;
        }

        public Builder capacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder mode(Mode value, Mode defaultValue) {
            mode = value != null ? value : defaultValue;
            return this;
        }

        public Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder algorithm(Algorithm value, Algorithm defaultValue) {
            algorithm = value != null ? value : defaultValue;
            return this;
        }

        public Builder algorithm(Algorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public WaitingList build() {
            return new WaitingList(this);
        }
    }
}
