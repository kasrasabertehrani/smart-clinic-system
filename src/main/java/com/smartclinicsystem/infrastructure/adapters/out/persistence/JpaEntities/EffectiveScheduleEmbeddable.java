package com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities;



import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Embeddable
public class EffectiveScheduleEmbeddable {

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "weekly_schedule_json", columnDefinition = "jsonb", nullable = false)
    private Map<DayOfWeek, List<ShiftJson>> weeklySchedule;

    public record ShiftJson(LocalTime startTime, LocalTime endTime) {}

    protected EffectiveScheduleEmbeddable() {

    }

    public EffectiveScheduleEmbeddable(LocalDate validFrom, Map<DayOfWeek, List<ShiftJson>> weeklySchedule) {
        this.validFrom = validFrom;
        this.weeklySchedule = weeklySchedule;
    }


}
