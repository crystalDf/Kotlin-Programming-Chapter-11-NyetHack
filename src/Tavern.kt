import java.io.File
import kotlin.math.roundToInt

const val TAVERN_NAME = "Taernyl's Folly"
const val CASK = 5
const val PINTS_ONE_GALLON = 1 / .125
const val GOLDS_ONE_DRAGON_COIN = 1.43

var playerGold = 10
var playerSilver = 10
var playerDragonCoin = 5.0

var remainingQuantityOfDragonBreathInPints = (PINTS_ONE_GALLON * CASK).toInt()

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split("\n")

fun main(args: Array<String>) {

    println("Number of pints left in the cask: $remainingQuantityOfDragonBreathInPints")

    println(patronList)
    patronList.remove("Eli")
    patronList.add("Alex")
    patronList.add(0, "Alex")
    patronList[0] = "Alexis"
    println(patronList)

    if (patronList.contains("Eli")) {
        println("The tavern master says: Eli's in the back playing cards.")
    } else {
        println("The tavern master says: Eli isn't here.")
    }

    if (patronList.containsAll(listOf("Sophie", "Mordoc"))) {
        println("The tavern master says: Yea, they're seated by the stew kettle.")
    } else {
        println("The tavern master says: Nay, they departed hours ago.")
    }

    for (patron in patronList) {
        println("Good evening, $patron")
    }

    patronList.forEach { patron ->
        println("Good evening, $patron")
    }

    patronList.forEachIndexed { index, patron ->
        println("Good evening, $patron - you're #${index + 1} in line.")
        placeOrder(patron, menuList.shuffled().first())
    }

    menuList.forEachIndexed { index, data ->
        println("$index : $data")
    }

    repeat(10) {
        val first = patronList.shuffled().first()
        val last = lastName.shuffled().first()
        val name = "$first $last"
        uniquePatrons.add(name)
    }

    println(uniquePatrons)

    var orderCount = 0

    while (orderCount <= 9) {
        placeOrder(uniquePatrons.shuffled().first(), menuList.shuffled().first())
        orderCount++
    }

    displayFormattedTavernMenu(menuList)
    displayAdvancedFormattedTavernMenu(menuList)
}

private fun placeOrder(patronName: String, menuData: String) {

    val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)

    println("$patronName speaks with $tavernMaster about their order.")

    val (type, name, price) = menuData.split(',')
    val message = "$patronName buys a $name ($type) for $price"

    println(message)

    val phrase = if (name == "Dragon's Breath") {
        remainingQuantityOfDragonBreathInPints--
        "$patronName exclaims: ${toDragonSpeak("Ah, delicious $name!")}" + "\n" +
                "$patronName exclaims: ${toDragonSpeak("DRAGON'S BREATH: IT'S GOT WHAT ADVENTURERS CRAVE!")}"
    } else {
        "$patronName says: Thanks for the $name"
    }

    println(phrase)
}

private fun toDragonSpeak(phrase: String) = phrase.replace(Regex("[aeiouAEIOU]")) {

    when (it.value.toLowerCase()) {

        "a" -> "4"
        "e" -> "3"
        "i" -> "1"
        "o" -> "0"
        "u" -> "|_|"
        else -> it.value
    }
}

fun performPurchase(price: Double) {

    displayBalance()

//    val totalPurse = playerGold + (playerSilver / 100.0)
    val totalPurse = GOLDS_ONE_DRAGON_COIN * playerDragonCoin

    println("Total purse: ${"%.2f".format(totalPurse)}")
    println("Purchasing item for $price")

    if (totalPurse < price) {
        println("The customer is short on gold.")
        return
    }

    val remainingBalance = totalPurse - price

    println("Remaining balance: ${"%.2f".format(remainingBalance)}")

    val remainingGold = remainingBalance.toInt()
    val remainingSilver = (remainingBalance % 1 * 100).roundToInt()
    val remainingDragonCoin = remainingBalance / GOLDS_ONE_DRAGON_COIN

    playerGold = remainingGold
    playerSilver = remainingSilver
    playerDragonCoin = remainingDragonCoin

    displayBalance()
}

fun displayBalance() {

//    println("Player's purse balance: Gold: $playerGold , Silver: $playerSilver")
    println("Player's purse balance: DragonCoin: ${"%.4f".format(playerDragonCoin)}")
}

fun displayFormattedTavernMenu(menuList: List<String>) {

    val welcome = "*** Welcome to Taernyl's Folly ***"

    println(welcome)
    println()

    menuList.forEach { menuData ->

        val (_, name, price) = menuData.split(',')
        val menuItem = capitalizeEachWord(name) + ".".repeat(welcome.length - name.length - price.length) + price

        println(menuItem)
    }
}

fun displayAdvancedFormattedTavernMenu(menuList: List<String>) {

    val welcome = "*** Welcome to Taernyl's Folly ***"

    println(welcome)
    println()

    val typeSet = mutableSetOf<String>()
    val menuMap = mutableMapOf<String, MutableList<String>>()

    menuList.forEach { menuData ->

        val (type, name, price) = menuData.split(',')
        val menuItem = capitalizeEachWord(name) + ".".repeat(welcome.length - name.length - price.length) + price
        val typeItem = " ".repeat((welcome.length - "~[$type]~".length) / 2) + "~[$type]~"

        if (type !in typeSet) {
            typeSet.add(type)
            menuMap[typeItem] = mutableListOf(menuItem)
        } else {
            menuMap[typeItem]?.add(menuItem)
        }
    }

    menuMap.forEach { item ->
        println(item.key)
        item.value.forEach(::println)
    }
}

private fun capitalizeEachWord(words: String): String {

    val answerList = mutableListOf<String>()

    words.split(" ").forEach { word ->
        answerList.add(word.capitalize())
    }

    return answerList.joinToString(" ")
}