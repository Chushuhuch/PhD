import java.util.*

/**
 * Created by serg on 10.01.16.
 */

private fun minimizeqASomehow() {
    val k = 1.1
    var rStep = 1.0
    var phiN = 20
    val random = Random()
    var cur = toExtremum( Complex( random.nextDouble() * 1e6, random.nextDouble() * 1e6 ) )
//    var cur = toExtremum( Complex( 10.0, 2.0 ) )
    while ( true ) {
        rStep *= k * k
        var min: Extremum<Complex, Double>
        do {
            rStep /= k
            min = cur
            for ( i in 0 .. phiN )
                min = min( min, toExtremum( cur.arg + fromPolar( rStep, Math.PI * 2 / phiN * i ) ) )
        } while ( min >= cur )
        cur = min
        System.out.format( "q = %20.5f, w = %10.6f, min = %10.6f, rStep = %20.5f\n", cur.arg.q(), cur.arg.w(), cur.value, rStep )
    }
}

private fun toExtremum( c: Complex ): Extremum<Complex, Double> {
    val d = Complex( Math.max( 0.0, c.re ), Math.max( 0.0, c.im ) )
    return Extremum( d, qA( d.q(), d.w() ) )
}

fun main(args: Array<String>) {
    minimizeqASomehow()
}
