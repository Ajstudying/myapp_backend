package com.kaj.myapp.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationModifyRequest {

    private String petname;
    private long reservationTime;
}