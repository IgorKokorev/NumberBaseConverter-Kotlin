package converter

import java.math.BigDecimal

// Do not delete this line

val digitsArr = arrayListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
var baseFrom = 10
var baseTo = 10
const val MAX_BASE = 36
const val MAX_FRACTIONAL_DIGITS = 5
const val WR_INP = "Wrong input!"

class ConvertException(s: String): Exception(s)



fun main() {
    while (true) {
        print("\nEnter two numbers in format: {source base} {target base} (To quit type /exit) ")
        val inp = readln().split(Regex("\\s+"))
        if (inp[0].equals("/exit")) break

        try {
            baseFrom = inp[0].toInt()
            baseTo = inp[1].toInt()
        } catch (e: Exception) {
            println(WR_INP)
            continue
        }

        if(baseFrom !in 2..MAX_BASE || baseTo !in 2..MAX_BASE) {
            println(WR_INP)
            continue
        }

        while (true) {
            print("Enter number in base $baseFrom to convert to base $baseTo (To go back type /back) ")
            val inpNum = readln().split('.')
            if (inpNum[0].equals("/back")) break

            try {
                print("Conversion result: ${convertFromLong(convertToLong(inpNum[0], baseFrom), baseTo)}")
            } catch (e: ConvertException) {
                println(e.message)
            }

            if (inpNum.size == 1) {
                println("")
            } else {
                println(".${fractionalFromN(fractionalToN(inpNum[1], baseFrom), baseTo)}")
            }
        }


    }
}

fun fractionalFromN(num: BigDecimal, base: Int): String {
    var rest = num
    var resStr = ""

    for (i in 1..MAX_FRACTIONAL_DIGITS) {
        rest *= BigDecimal(base)
        resStr += digitsArr[rest.toInt()]
        rest = rest.remainder(BigDecimal.ONE)
    }

    return resStr
}

fun fractionalToN(str: String, base: Int): BigDecimal {
    if (str.isEmpty()) return BigDecimal.ZERO
    val firstCh = str.first().uppercaseChar()
    if (!digitsArr.contains(firstCh)) throw ConvertException("Wrong input!")
    if (digitsArr.indexOf(firstCh) >= base) throw ConvertException("Wrong input!")

    val d1 = BigDecimal(digitsArr.indexOf(firstCh)).setScale(100).div(BigDecimal(base))
    val d2 = if (str.length == 1) { BigDecimal.ZERO }
    else { fractionalToN(str.substring(1 .. str.lastIndex), base).setScale(100).div(BigDecimal(base)) }
    return d1 + d2
}

fun convertToLong(str: String, base: Int): Long {
    if (str.isEmpty()) return 0
    val lastCh = str.last().uppercaseChar()
    if (!digitsArr.contains(lastCh)) throw ConvertException("Wrong input!")
    if (digitsArr.indexOf(lastCh) >= base) throw ConvertException("Wrong input!")
    if (str.length == 1) return digitsArr.indexOf(lastCh).toLong()
    return convertToLong(str.substring(0 until str.lastIndex), base) * base + digitsArr.indexOf(lastCh)
}

fun convertFromLong(num: Long, base: Int): String {
    if (base !in 2..36) throw ConvertException("Wrong base!")
    if (num < 0) return "-" + convertFromLong(-num, base)
    if (num < base) return digitsArr[num.toInt()].toString()
    return convertFromLong(num / base, base) + digitsArr[(num % base).toInt()]
}
