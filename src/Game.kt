import kotlin.math.min
import kotlin.math.pow

fun main(args: Array<String>) {

    val name = "Star"
    var healthPoints = 89
    var isBlessed = true
    val isImmortal = false
    val auraColor = formatAuraColor(isBlessed, healthPoints, isImmortal)
    val healthStatus = formatHealthStatus(healthPoints, isBlessed)
    val statusFormatString = "(HP)(A) -> H"

    printPlayerStatus(auraColor = auraColor, isBlessed = isBlessed, name = name, healthStatus = healthStatus)
    printFormattedPlayerStatus(statusFormatString, isBlessed, auraColor, healthPoints, name, healthStatus)

    val inebriationStatus = castFireball(5)

    println("Inebriation Status: $inebriationStatus")
}

private fun printFormattedPlayerStatus(
    statusFormatString: String,
    isBlessed: Boolean,
    auraColor: String,
    healthPoints: Int,
    name: String,
    healthStatus: String
) {
    val result: StringBuilder = StringBuilder("")

    for (i in statusFormatString.indices) {

        val replacement = when (statusFormatString[i]) {
            'B' -> "Blessed: ${if (isBlessed) "YES" else "NO"}"
            'A' -> "Aura: $auraColor"
            'H' -> if (((i + 1) < statusFormatString.length) && (statusFormatString[i + 1] == 'P')) {
                "HP: $healthPoints"
            } else {
                "$name $healthStatus"
            }
            'P' -> ""
            else -> {
                statusFormatString[i].toString()
            }
        }

        result.append(replacement)
    }

    println(result)
}

private fun printPlayerStatus(
    auraColor: String,
    isBlessed: Boolean,
    name: String,
    healthStatus: String
) {
    println(
        "(Aura: $auraColor) " +
                "(Blessed: ${if (isBlessed) "YES" else "NO"})"
    )
    println("$name $healthStatus")
}

private fun formatAuraColor(
    isBlessed: Boolean,
    healthPoints: Int,
    isImmortal: Boolean
) = if ((isBlessed && healthPoints > 50) || isImmortal) {
        when ((Math.random().pow((110 - healthPoints) / 100.0) * 20).toInt()) {
            in 0..5 -> "RED"
            in 6..10 -> "ORANGE"
            in 11..15 -> "PURPLE"
            in 16..20 -> "GREEN"
            else -> "NONE"
        }
    } else "NONE"

private fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean) =
    when (healthPoints) {
        100 -> "is in excellent condition!"
        in 90..99 -> "has a few scratches."
        in 75..89 -> if (isBlessed) {
            "has some minor wounds but is healing quite quickly!"
        } else {
            "has some minor wounds."
        }
        in 15..74 -> "looks pretty hurt."
        else -> "is in awful condition!"
    }

private fun castFireball(numFireballs: Int = 2): String {
    println("A glass of Fireball springs into existence. (x$numFireballs)")

    return when ((min(numFireballs, 100) + 1) / 2) {
        in 1..10 -> "tipsy"
        in 11..20 -> "sloshed"
        in 21..30 -> "soused"
        in 31..40 -> "stewed"
        in 41..50 -> "..t0aSt3d"
        else -> ""
    }
}
