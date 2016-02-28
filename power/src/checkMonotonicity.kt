import java.util.*

/**
 * Created by serg on 09.01.16.
 */

val RANGE_MIN_10_POW = -4
val RANGE_MAX_10_POW = 4

enum class Monotonicity {
    Decreasing, None, Increasing
}

fun main(args: Array<String>) {
    val range = genRange()
    for ( w in range ) {
        var state = Monotonicity.None
        val changes = ArrayList<Pair<Double, Monotonicity>>()
        val mon = EnumSet.noneOf( Monotonicity::class.java )
        var value = qA( 0.0, w )
        var min = Extremum( 0.0, value )
        var max = Extremum( 0.0, value )
        for ( q in range ) {
            val nextValue = qA( q, w )
            var nextState = if ( nextValue > value ) Monotonicity.Increasing else if ( nextValue < value ) Monotonicity.Decreasing else state
            mon.add( nextState )
            if ( nextState != state ) {
                changes.add( Pair( q, nextState ) )
            }
            state = nextState
            value = nextValue
            max = max( max, Extremum( q, value ) )
            min = min( min, Extremum( q, value ) )
        }
        System.out.format( "For w = %6.5f the A function can be %25s, ranging from %20s to %20s. Changes at: %s\n", w, mon, min, max, changes )
//        println( "For w = $w the A function can be $mon, ranging from $min to $max. Changes at: $changes" )
    }
}

fun genRange(): ArrayList<Double> {
    val r = ArrayList<Double>()
    for ( i in RANGE_MIN_10_POW .. RANGE_MAX_10_POW ) {
        for ( j in 10 .. 99 ) {
            r.add( j * Math.pow( 10.0, i.toDouble() ) )
        }
    }
    return r
}

