package com.kaj.myapp.schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleModifyRequest {

    private String petname;
    private String content;
    private long reservationTime;
}
