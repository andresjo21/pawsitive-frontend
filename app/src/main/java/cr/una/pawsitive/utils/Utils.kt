package cr.una.pawsitive.utils

import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

fun calculateAge(birthDate: Date): String {
    val birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val meses = Period.between(birthDateLocal, LocalDate.now()).toTotalMonths()

    //si meses es igual a 0 entonces que calcule los dias y si es mayor a 12 que calcule los años
    return if (meses == 0L) {
        "${Period.between(birthDateLocal, LocalDate.now()).days} días"
    } else if (meses > 12) {
        "${Period.between(birthDateLocal, LocalDate.now()).years} años"
    } else {
        "$meses meses"
    }
}