/**
 * Created by serg on 10.01.16.
 */

fun maximizeqASomehow() {
    val k = 1.1
    var rStep = 1.0
    var phiN = 20
    var cur = toExtremum( Complex( 10.0, 2.0 ) )
    while ( true ) {
        rStep *= k * k
        var max: Extremum<Complex, Double>
        do {
            rStep /= k
            max = cur
            for ( i in 0 .. phiN )
                max = max( max, toExtremum( cur.arg + fromPolar( rStep, Math.PI * 2 / phiN * i ) ) )
        } while ( max <= cur )
        cur = max
        System.out.format( "q = %20.5f, w = %10.6f, max = %10.6f, rStep = %20.5f\n", cur.arg.q(), cur.arg.w(), cur.value, rStep )
    }
}

fun Complex.q() = re
fun Complex.w() = im

fun toExtremum( c: Complex ): Extremum<Complex, Double> {
    val d = Complex( Math.max( 0.0, c.re ), Math.max( 0.0, c.im ) )
    return Extremum( d, qA( d.q(), d.w() ) )
}

fun main(args: Array<String>) {
    maximizeqASomehow()
}
