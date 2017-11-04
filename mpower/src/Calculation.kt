import Number.Constants.ZERO

fun main(args: Array<String> ) {
//    evalTopLeft()
//    evalTopMid()
//    evalBotLeft()
//    evalBotRight()
    evalTopRight()
}

fun evalTopRight() {
    val Aij = (
            ExpressionNode( "4 r / ln" ) { vr: VR -> 4.0 * vr.r / vr.v.log1pi() } +
                    ExpressionNode( "4 (1 - rv)" ) { vr: VR -> 4.0 * ( 1.0 - vr.r * vr.v ) } -
                    ExpressionNode( "(r (v - v^2) + 3 v + 1) ln") { vr: VR -> ( vr.r * vr.v * ( 1.0 - vr.v ) + 3.0 * vr.v + 1.0 ) * vr.v.log1pi() }
            ) / ExpressionNode( "2 (1 + r) (1 + rv)" ) { vr: VR -> 2.0 * ( 1.0 + vr.r ) * ( 1.0 + vr.r * vr.v ) }
    proveInequality( Aij, VR( 0.0, 0.0 ), VR( 0.2, 1.0 ), Direction.Max, Number( 0.62 ) )
}

fun evalBotRight() {
    val VQ0 = VQ(0.0, 0.0)
    val vln = ExpressionNode( "v ln" ) { vq: VQ -> vq.v * vq.v.log1pi() }.withValues( { vq: VQ -> vq.v == ZERO }, ZERO )
    val nominator = (
            ExpressionNode( "4 q" ) { vq: VQ -> 4.0 * vq.q } -
            // even if it is not zero in origin, it's ok, since we seek upper boundary
            ExpressionNode( "q (1 + 3 v) ln" ) { vq: VQ -> vq.q * ( 1.0 + 3.0 * vq.v ) * vq.v.log1pi() }.withValue( VQ0, ZERO ) -
            vln +
            ExpressionNode( "v" ) { vq: VQ -> vq.v } * vln +
            ExpressionNode( "4 / ln" ) { vq: VQ -> 4.0 / vq.v.log1pi() } -
            ExpressionNode( "4 v" ) { vq: VQ -> 4.0 * vq.v }
            )
    val Aj = ( nominator * ExpressionNode( "q" ) { vq: VQ -> vq.q } / ExpressionNode( "v + q" ) { vq: VQ -> vq.v + vq.q } ).withValue( VQ0, ZERO ) /
            ExpressionNode( "2 (q + 1)" ) { vq: VQ -> 2.0 * ( vq.q + 1.0 ) }
    proveInequality( Aj, VQ( 0.0, 0.0 ), VQ( 0.2, 10.0 ), Direction.Max, Number( 0.62 ) )
}

fun evalTopMid() {
    // Done: Ai_r, w = 1..6, r = 0..1, max < 0, parts = 10^3
    val Air = (
            ExpressionNode( "4 w / ln" ) { wr: WR -> 4.0 * wr.w.vOverLog1p() } * ExpressionNode( "w - r^2" ) { wr: WR -> wr.w - wr.r * wr.r } +
                    ExpressionNode( "4" ) { wr: WR -> Number( 4.0 ) } * ( ExpressionNode( "r^2") { wr: WR -> wr.r * wr.r } - ExpressionNode( "2 r w + w^2 + 2 w" ) { wr: WR -> 2.0 * wr.r * wr.w + wr.w * wr.w + 2.0 * wr.w } ) +
                    ( ExpressionNode( "r^2 + 2 r (3 + w) + 4 + 3 w + w^2" ) { wr: WR -> wr.r * wr.r + 2.0 * wr.r * ( 3.0 + wr.w ) + 4.0 + 3.0 * wr.w + wr.w * wr.w } - ExpressionNode( "r^2 / w" ) { wr: WR -> wr.r * wr.r / wr.w } ) *
                            ExpressionNode( "ln" ) { wr: WR -> wr.w.log1p() }
            ) / ExpressionNode( "2 (1 + r)^2 (w + r)^2" ) { wr: WR -> 2.0 * ( ( 1.0 + wr.r ) * ( wr.r + wr.w ) ).pow( 2.0 ) }
    proveInequality( Air, WR( 1.0, 0.0 ), WR( 6.0, 1.0 ), Direction.Max, ZERO )
}

fun evalBotLeft() {
    //    Done: A, w = 0..10, q = 0..1, max < 0.62, parts = 10^2
    val A = (
            ExpressionNode( "4 q w" ) { wq: WQ -> 4.0 * wq.q * wq.w }
                    - ExpressionNode( "q (w + 3) ln" ) { wq: WQ -> wq.q * ( wq.w + 3.0 ) * wq.w.log1p() }
                    - ExpressionNode( "ln" ) { wq: WQ -> wq.w.log1p() }
                    + ExpressionNode( "ln / w" ) { wq: WQ -> 1.0 / wq.w.vOverLog1p() }
                    + ExpressionNode( "4 w / ln" ) { wq: WQ -> 4.0 * wq.w.vOverLog1p() }
                    - ExpressionNode( "4" ) { wq: WQ -> Number( 4.0 ) }
            ) / ExpressionNode( "2 (q w + 1)" ) { wq: WQ -> 2.0 * ( wq.q * wq.w + 1.0 ) } * ExpressionNode( "q / (q + 1)" ) { wq: WQ -> wq.q / ( wq.q + 1.0 ) }
    proveInequality( A, WQ( 0.0, 0.0 ), WQ( 10.0, 1.0 ), Direction.Max, Number( 0.62 ) )
}

fun evalTopLeft() {
//    Done: Ai, w = 0..1, r = 0..1, max < 0.62, parts = 10^3
    val WR0 = WR(0.0, 0.0)
    val nominator = (
            ExpressionNode( "4 (w / ln - 1) r" ) { wr: WR -> 4.0 * ( wr.w.vOverLog1p() - 1.0 ) * wr.r }
                    + ExpressionNode( "3 (w - ln)" ) { wr: WR -> 3.0 * ( wr.w - wr.w.log1p() ) }
                    - ExpressionNode( "w ln" ) { wr: WR -> wr.w * wr.w.log1p() }
                    - ExpressionNode( "r ln" ) { wr: WR -> wr.r * wr.w.log1p() }
                    + ExpressionNode( "w + r ln / w" ) { wr: WR -> wr.w + wr.r / wr.w.vOverLog1p() }
            )
    val Ai = ( nominator / ExpressionNode( "w + r" ) { wr: WR -> wr.w + wr.r } ).withValue( WR0, Number( 1.0 ) ) /
            ExpressionNode( "2 (1 + r)" ) { wr: WR -> 2.0 * ( 1.0 + wr.r ) }
    proveInequality( Ai, WR0, WR( 1.0, 1.0 ), Direction.Max, Number( 0.62 ), minParts = 1 )
}


//    val Ai0 = ExpressionNode { w: W -> Number( 2.0 ) } -
//            ExpressionNode { w: W -> w.w.log1p() / 2.0 } -
//            ExpressionNode { w: W -> 1.5 / w.w.vOverLog1p() }
//    proveInequality( Ai0, W( 0.0 ), W( 1.0 ), Direction.Max, Number( 0.51 ) )

//    val Ai1r = (
//            ExpressionNode { wr: WR -> wr.w.log1p() } +
//                    ExpressionNode { wr: WR -> wr.w * wr.w.vOverLog1p() } -
//                    ExpressionNode { wr: WR -> wr.w }
//            ) / ExpressionNode { wr: WR -> ( wr.w + wr.r ) * ( wr.w + wr.r ) }
//    proveInequality( Ai1r, WR( 0.0, 0.0 ), WR( 6.0, 0.1 ), Direction.Max, ZERO )

//    val Air0 =
//            ExpressionNode { w: W -> -4.0 * w.w * w.w } +
//                    ExpressionNode { w: W -> 4.0 * w.w * ( w.w.vOverLog1p() - 1.0 ) } +
//                    ExpressionNode { w: W -> w.w * w.w * w.w.log1p() } +
//                    ExpressionNode { w: W -> 3.0 * w.w * w.w.log1p() } +
//                    ExpressionNode { w: W -> 4.0 * ( w.w.log1p() - w.w ) }
//    proveInequality( Air0, W( 0.0 ), W( 6.0 ), Direction.Max, ZERO )


class W( var w: Number ) : VariableSet() {
    constructor( x: Double ) : this( Number( x ) )
    override fun getComponents() = listOf( w )
    override fun setComponent( i: Int, value: Number ) { w = value }
    override fun <VS : VariableSet> copy() = W( w ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is W ) return false
        return w == other.w
    }

    override fun hashCode() = w.hashCode()
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

    override fun hashCode() = w.hashCode() xor r.hashCode()
}

class WQ( var w: Number, var q: Number ) : VariableSet() {
    constructor( wd: Double, qd: Double ) : this( Number( wd ), Number( qd ) )
    override fun getComponents() = listOf( w, q )
    override fun setComponent( i: Int, value: Number ) {
        if ( i == 0 ) w = value
        else q = value
    }
    override fun <VS : VariableSet> copy() = WQ( w, q ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is WQ ) return false
        return w == other.w && q == other.q
    }

    override fun hashCode() = w.hashCode() xor q.hashCode()
}

class VQ( var v: Number, var q: Number ) : VariableSet() {
    constructor( vd: Double, qd: Double ) : this( Number( vd ), Number( qd ) )
    override fun getComponents() = listOf( v, q )
    override fun setComponent( i: Int, value: Number ) {
        if ( i == 0 ) v = value
        else q = value
    }
    override fun <VS : VariableSet> copy() = VQ( v, q ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is VQ ) return false
        return v == other.v && q == other.q
    }

    override fun hashCode() = v.hashCode() xor q.hashCode()
}

class VR( var v: Number, var r: Number ) : VariableSet() {
    constructor( vd: Double, rd: Double ) : this( Number( vd ), Number( rd ) )
    override fun getComponents() = listOf( v, r )
    override fun setComponent( i: Int, value: Number ) {
        if ( i == 0 ) v = value
        else r = value
    }
    override fun <VS : VariableSet> copy() = VR( v, r ) as VS

    override fun equals( other: Any? ): Boolean {
        if ( other !is VR ) return false
        return v == other.v && r == other.r
    }

    override fun hashCode() = v.hashCode() xor r.hashCode()
}

val ZERO_FUNC = { _: WR -> Number(0.0) }

class ROverRPWExpression: ExpressionNode<WR>( "r / (w + r)", ZERO_FUNC ) {
    override fun eval( v: WR, dir: Direction ): Number {
        if ( v.w < 0.0 || v.r < 0.0 ) throw RuntimeException( "Expression defined only on nonnegative arguments" )
        if ( v.w.v == 0.0 && v.r.v == 0.0 ) return if ( dir == Direction.Max ) Number( 1.0 ) else ZERO
        return v.r / ( v.w + v.r )
    }
}

class WOverRPWExpression: ExpressionNode<WR>( "w / (w + r)", ZERO_FUNC ) {
    override fun eval( v: WR, dir: Direction ): Number {
        if ( v.w < 0.0 || v.r < 0.0 ) throw RuntimeException( "Expression defined only on nonnegative arguments" )
        if ( v.w.v == 0.0 && v.r.v == 0.0 ) return if ( dir == Direction.Max ) Number( 1.0 ) else ZERO
        return v.w / ( v.w + v.r )
    }
}
