fun main( args: Array<String> ) {
    evalTopLeft()
}

class W( var w: Number ) : VariableSet() {
    constructor( x: Double ) : this( Number( x ) )
    override fun getComponents() = listOf( w )
    override fun setComponent( i: Int, value: Number ) { w = value }
    override fun <VS : VariableSet> copy() = W( w ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is W ) return false
        return w == other.w
    }
}

class WR( var w: Number, var r: Number ) : VariableSet() {
    constructor( wd: Double, rd: Double ) : this( Number( wd ), Number( rd ) )
    override fun getComponents() = listOf( w, r )
    override fun setComponent( i: Int, value: Number ) {
        if ( i == 0 ) w = value
        else r = value
    }
    override fun <VS : VariableSet> copy() = WR( w, r ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is WR ) return false
        return w == other.w && r == other.r
    }
}

val ZERO_FUNC = { _: WR -> Number(0.0) }

class ROverRPWExpression: ExpressionNode<WR>( "r / (w + r)", ZERO_FUNC ) {
    override fun eval( v: WR, dir: Direction ): Number {
        if ( v.w < 0.0 || v.r < 0.0 ) throw RuntimeException( "Expression defined only on nonnegative arguments" )
        if ( v.w.v == 0.0 && v.r.v == 0.0 ) return if ( dir == Direction.Max ) Number( 1.0 ) else Number( 0.0 )
        return v.r / ( v.w + v.r )
    }
}

class WOverRPWExpression: ExpressionNode<WR>( "w / (w + r)", ZERO_FUNC ) {
    override fun eval( v: WR, dir: Direction ): Number {
        if ( v.w < 0.0 || v.r < 0.0 ) throw RuntimeException( "Expression defined only on nonnegative arguments" )
        if ( v.w.v == 0.0 && v.r.v == 0.0 ) return if ( dir == Direction.Max ) Number( 1.0 ) else Number( 0.0 )
        return v.w / ( v.w + v.r )
    }
}

fun evalTopLeft() {
//    val Ai0 = ExpressionNode { w: W -> Number( 2.0 ) } -
//            ExpressionNode { w: W -> w.w.log1p() / 2.0 } -
//            ExpressionNode { w: W -> 1.5 / w.w.vOverLog1p() }
//    proveInequality( Ai0, W( 0.0 ), W( 1.0 ), Direction.Max, Number( 0.51 ) )

//    val Ai1r = (
//            ExpressionNode { wr: WR -> wr.w.log1p() } +
//                    ExpressionNode { wr: WR -> wr.w * wr.w.vOverLog1p() } -
//                    ExpressionNode { wr: WR -> wr.w }
//            ) / ExpressionNode { wr: WR -> ( wr.w + wr.r ) * ( wr.w + wr.r ) }
//    proveInequality( Ai1r, WR( 0.0, 0.0 ), WR( 6.0, 0.1 ), Direction.Max, Number( 0.0 ) )

    val Ai = (
            ExpressionNode( "4 (w / ln - 1) r / (w + r)" ) { wr: WR -> 4.0 * ( wr.w.vOverLog1p() - 1.0 ) * wr.r / ( wr.w + wr.r ) }.withValue( WR( 0.0, 0.0 ), Number( 0.0 ) )
                    + ExpressionNode( "3 (w - ln) / (w + r)" ) { wr: WR -> 3.0 * ( wr.w - wr.w.log1p() ) / ( wr.w + wr.r ) }.withValue( WR( 0.0, 0.0 ), Number( 0.0 ) )
                    - ExpressionNode( "w ln / (w + r)" ) { wr: WR -> wr.w * wr.w.log1p() / ( wr.w + wr.r ) }.withValue( WR( 0.0, 0.0 ), Number( 0.0 ) )
                    - ExpressionNode( "r ln / (w + r)" ) { wr: WR -> wr.r * wr.w.log1p() / ( wr.w + wr.r ) }.withValue( WR( 0.0, 0.0 ), Number( 0.0 ) )
                    + ExpressionNode( "(w + r ln / w) / (w + r)" ) { wr: WR -> ( wr.w + wr.r / wr.w.vOverLog1p() ) / ( wr.w + wr.r ) }.withValue( WR( 0.0, 0.0 ), Number( 1.0 ) )
            ) /
            ExpressionNode( "2" ) { wr: WR -> Number( 2.0 ) }
    proveInequality( Ai, WR( 0.0, 0.0 ), WR( 1.0, 1.0 ), Direction.Max, Number( 0.62 ), minParts = 1 )

//    Done: Ai_r, w = 1..6, r = 0..1, max < 0, parts = 10^3
//    val Air = (
//            ExpressionNode( "4 w / ln" ) { wr: WR -> 4.0 * wr.w.vOverLog1p() } * ExpressionNode( "w - r^2" ) { wr: WR -> wr.w - wr.r * wr.r } +
//                    ExpressionNode( "4" ) { wr: WR -> Number( 4.0 ) } * ( ExpressionNode( "r^2") { wr: WR -> wr.r * wr.r } - ExpressionNode( "2 r w + w^2 + 2 w" ) { wr: WR -> 2.0 * wr.r * wr.w + wr.w * wr.w + 2.0 * wr.w } ) +
//                    ( ExpressionNode( "r^2 + 2 r (3 + w) + 4 + 3 w + w^2" ) { wr: WR -> wr.r * wr.r + 2.0 * wr.r * ( 3.0 + wr.w ) + 4.0 + 3.0 * wr.w + wr.w * wr.w } - ExpressionNode( "r^2 / w" ) { wr: WR -> wr.r * wr.r / wr.w } ) *
//                            ExpressionNode( "ln" ) { wr: WR -> wr.w.log1p() }
//            ) / ExpressionNode( "2 (1 + r)^2 (w + r)^2" ) { wr: WR -> 2.0 * ( ( 1.0 + wr.r ) * ( wr.r + wr.w ) ).pow( 2.0 ) }
//    proveInequality( Air, WR( 1.0, 0.0 ), WR( 6.0, 1.0 ), Direction.Max, Number( 0.0 ) )

//    val Air0 =
//            ExpressionNode { w: W -> -4.0 * w.w * w.w } +
//                    ExpressionNode { w: W -> 4.0 * w.w * ( w.w.vOverLog1p() - 1.0 ) } +
//                    ExpressionNode { w: W -> w.w * w.w * w.w.log1p() } +
//                    ExpressionNode { w: W -> 3.0 * w.w * w.w.log1p() } +
//                    ExpressionNode { w: W -> 4.0 * ( w.w.log1p() - w.w ) }
//    proveInequality( Air0, W( 0.0 ), W( 6.0 ), Direction.Max, Number( 0.0 ) )
}
