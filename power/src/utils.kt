/**
 * Created by serg on 09.01.16.
 */
fun <T: Comparable<T>> max( x: T, y: T ): T = if ( x < y ) y else x
fun <T: Comparable<T>> min( x: T, y: T ): T = if ( x < y ) x else y

public class Complex( val re: Double, val im: Double ) {
    operator fun plus( other: Complex ) = Complex( re + other.re, im + other.im )
}

fun fromPolar( r: Double, phi: Double ) = Complex( r * Math.cos( phi ), r * Math.sin( phi ) )


data class Extremum<A, V: Comparable<V>>(val arg: A, val value: V ): Comparable<Extremum<A, V>> {
    override fun compareTo(other: Extremum<A, V>): Int = value.compareTo( other.value )
    override fun toString(): String = java.lang.String.format( "%10.5f at %10.3f", value, arg )//"$value at $arg"
}