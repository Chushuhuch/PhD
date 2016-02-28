/**
 * Created by serg on 09.01.16.
 */

fun ln1pwOverW( w: Double ): Double {
    if ( w < 0 ) throw RuntimeException( "The argument of ln1pwOverW is not supposed to be negative (called with w = $w)." )
    // we get around 15-16 decimal digits in double, the value is about 1 - w/2, so if w < 1E-20 we will always get 1.0
    // we need this not to divide by zero
    if ( w < 1E-30 ) return 1.0
    return Math.log1p( w ) / w
}

fun A( q: Double, w: Double ): Double =
        ( q * ( 4 * w - ( w + 3 ) * Math.log1p( w ) ) + 4 / ln1pwOverW( w ) - 4 - ( w - 1 ) * ln1pwOverW( w ) ) /
                ( ( q + 1 ) * ( q * w + 1 ) )

fun qA( q: Double, w: Double ): Double = q * A( q, w )