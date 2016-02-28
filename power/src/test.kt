import java.util.*

/**
 * Created by serg on 09.01.16.
 */

fun testLn1pwOverW() {
    val random = Random()
    for ( i in 1 .. 10 ) {
        val w = random.nextDouble()
        ln1pwOverW( w )
    }
    for ( i in 1 .. 100 ) {
        val w = Math.pow( 10.0, -i.toDouble() ) * random.nextDouble()
        ln1pwOverW( w )
    }
}

fun main(args: Array<String>) {
    testLn1pwOverW()
}