import Number.Constants.ZERO

fun main(args: Array<String> ) {
    // R1
    // Done: A, w = 0..10, q = 0..1, max < 0.62, parts = 10^2
//    proveInequality( A, WQ( 0.0, 0.0 ), WQ( 10.0, 1.0 ), Direction.Max, Number( 0.62 ) )
    // R2
    // Done: Ai, w = 0..1, r = 0..1, max < 0.62, parts = 10^2
//    proveInequality( Ai, WR0, WR( 1.0, 1.0 ), Direction.Max, Number( 0.62 ), minParts = 1 )
    // R3
    // Done: Ai_r, w = 1..6, r = 0..1, max < 0, parts = 10^3
//    proveInequality( Air, WR( 1.0, 0.0 ), WR( 4.0, 1.0 ), Direction.Max, ZERO )
    // R4
//    proveInequality( Aj, VQ( 0.0, 0.0 ), VQ( 0.2, 1.0 ), Direction.Max, Number( 0.62 ) )
    // R5
//    proveInequality( Aij, VR( 0.0, 0.0 ), VR( 0.25, 1.0 ), Direction.Max, Number( 0.62 ) )

    // max on infinity
//    proveInequality( Ainf, W( 1.0 ), W( 6.0 ), Direction.Max, Number( 0.63 ) )

//    proveInequality( D2AinfNominator, W( 1.0 ), W( 4.0 ), Direction.Max, ZERO  )

//    val w_max = ternarySearchOptimum( Ainf, Number( 1.0 ), Number( 4.0 ), Direction.Max, Number( 1E-12 ) )
//    println( "max is for w = ${w_max} and equals to ${Ainf.eval( W( w_max ) )}")

    // 1000 parts got optimum [0.0, 0.497881469097823] near (2.997, 1.35864) which is better than desired 0.5
//    proveInequality( A, WQ( 0.0, 0.0 ), WQ( 3.0, 1.36 ), Direction.Max, Number( 0.5 ) )

    // 100 parts got optimum [-Infinity, 0.49915167986200665] near (0.198, 1.3464) which is better than desired 0.5
//    proveInequality( Aj, VQ( 0.0, 0.0 ), VQ( 0.2, 1.36 ), Direction.Max, Number( 0.5 ) )

    // 1000 parts got optimum [0.0, 0.49798943321683664] near (4.014, 1.2987) which is better than desired 0.5
//    proveInequality( A, WQ( 3.0, 0.0 ), WQ( 5.0, 1.3 ), Direction.Max, Number( 0.5 ) )

    // 10000 parts got optimum [0.49125063601847496, 0.49995960404443324] near (3.9228, 1.3599940000000001) which is better than desired 0.5
    Number( 4 ).log1p()
    proveInequality( A, WQ( 3.0, 1.3 ), WQ( 5.0, 1.36 ), Direction.Max, Number( 0.5 ) )

}

//val Ainf = (
//        ExpressionNode( "4 w" ) { w: W -> 4.0 * w.w }
//        - ExpressionNode( "(w + 3) ln" ) { w: W -> ( w.w + 3.0 ) * w.w.log1p() }
//    ) / ExpressionNode( "2 w" ) { w: W -> 2.0 * w.w }
//
//val D2AinfNominator =
//        ExpressionNode( "w (w^2 + 9w + 6)" ) { w: W -> w.w * ( w.w * w.w + 9.0 * w.w + 6.0 ) } /
//        ExpressionNode( "(w + 1)^2" ) { w: W -> ( w.w + 1.0 ).pow( 2.0 ) } -
//        ExpressionNode( "6 ln" ) { w: W -> 6.0 * w.w.log1p() }
//
//val D2Ainf = D2AinfNominator / ExpressionNode( "2 w^3" ) { w: W -> 2.0 * w.w.pow( 3.0 ) }
//
//val Aij = (
//        ExpressionNode( "4 r / ln" ) { vr: VR -> 4.0 * vr.r / vr.v.log1pi() } +
//        ExpressionNode( "4 (1 - rv)" ) { vr: VR -> 4.0 * ( 1.0 - vr.r * vr.v ) } -
//        ExpressionNode( "(r (v - v^2) + 3 v + 1) ln") { vr: VR -> ( vr.r * vr.v * ( 1.0 - vr.v ) + 3.0 * vr.v + 1.0 ) * vr.v.log1pi() }
//    ) / ExpressionNode( "2 (1 + r) (1 + rv)" ) { vr: VR -> 2.0 * ( 1.0 + vr.r ) * ( 1.0 + vr.r * vr.v ) }
//
//val VQ0 = VQ(0.0, 0.0)
//val vln = ExpressionNode( "v ln" ) { vq: VQ -> vq.v * vq.v.log1pi() }.withValues( { vq: VQ -> vq.v == ZERO }, ZERO )
//val Aj_nominator = (
//        ExpressionNode( "4 q" ) { vq: VQ -> 4.0 * vq.q } -
//                // even if it is not zero in origin, it's ok, since we seek upper boundary
//                ExpressionNode( "q (1 + 3 v) ln" ) { vq: VQ -> vq.q * ( 1.0 + 3.0 * vq.v ) * vq.v.log1pi() }.withValue( VQ0, ZERO ) -
//                vln +
//                ExpressionNode( "v" ) { vq: VQ -> vq.v } * vln +
//                ExpressionNode( "4 / ln" ) { vq: VQ -> 4.0 / vq.v.log1pi() } -
//                ExpressionNode( "4 v" ) { vq: VQ -> 4.0 * vq.v }
//        )
//val Aj = ( Aj_nominator * ExpressionNode( "q" ) { vq: VQ -> vq.q } / ExpressionNode( "v + q" ) { vq: VQ -> vq.v + vq.q } ).withValue( VQ0, ZERO ) /
//        ExpressionNode( "2 (q + 1)" ) { vq: VQ -> 2.0 * ( vq.q + 1.0 ) }
//
//val Air = (
//        ExpressionNode( "4 w / ln" ) { wr: WR -> 4.0 * wr.w.vOverLog1p() }
//            * ExpressionNode( "w - r^2" ) { wr: WR -> wr.w - wr.r * wr.r } +
//        ExpressionNode( "4" ) { wr: WR -> Number( 4.0 ) }
//            * (
//                ExpressionNode( "r^2") { wr: WR -> wr.r * wr.r }
//                - ExpressionNode( "2 r w + w^2 + 2 w" ) { wr: WR -> 2.0 * wr.r * wr.w + wr.w * wr.w + 2.0 * wr.w } )
//                + (
//                    ExpressionNode( "r^2 + 2 r (3 + w) + 4 + 3 w + w^2" ) { wr: WR -> wr.r * wr.r + 2.0 * wr.r * ( 3.0 + wr.w ) + 4.0 + 3.0 * wr.w + wr.w * wr.w }
//                    - ExpressionNode( "r^2 / w" ) { wr: WR -> wr.r * wr.r / wr.w }
//                ) *
//                    ExpressionNode( "ln" ) { wr: WR -> wr.w.log1p() }
//            ) / ExpressionNode( "2 (1 + r)^2 (w + r)^2" ) { wr: WR -> 2.0 * ( ( 1.0 + wr.r ) * ( wr.r + wr.w ) ).pow( 2.0 ) }

//val A = (
//        ExpressionNode( "4 q w" ) { wq: WQ -> 4.0 * wq.q * wq.w }
//        - ExpressionNode( "q (w + 3) ln" ) { wq: WQ -> wq.q * ( wq.w + 3.0 ) * wq.w.log1p() }
//        - ExpressionNode( "ln" ) { wq: WQ -> wq.w.log1p() }
//        + ExpressionNode( "ln / w" ) { wq: WQ -> 1.0 / wq.w.vOverLog1p() }
//        + ExpressionNode( "4 w / ln" ) { wq: WQ -> 4.0 * wq.w.vOverLog1p() }
//        - ExpressionNode( "4" ) { wq: WQ -> Number( 4.0 ) }
//    ) /
//        ExpressionNode( "2 (q w + 1)" ) { wq: WQ -> 2.0 * ( wq.q * wq.w + 1.0 ) } *
//        ExpressionNode( "q / (q + 1)" ) { wq: WQ -> wq.q / ( wq.q + 1.0 ) }

val A = (
        ExpressionNode( "4 q w" ) { wq: WQ -> 4 * wq.q * wq.w }
        - ExpressionNode( "q (w + 3) ln" ) { wq: WQ -> wq.q * ( wq.w + 3 ) * wq.w.log1p() }
        - ExpressionNode( "ln" ) { wq: WQ -> wq.w.log1p() }
        + ExpressionNode( "ln / w" ) { wq: WQ -> 1 / wq.w.vOverLog1p() }
        + ExpressionNode( "4 w / ln" ) { wq: WQ -> 4 * wq.w.vOverLog1p() }
        - ExpressionNode( "4" ) { wq: WQ -> Number( 4 ) }
    ) /
        ExpressionNode( "2 (q w + 1)" ) { wq: WQ -> 2 * ( wq.q * wq.w + 1 ) } *
        ExpressionNode( "q / (q + 1)" ) { wq: WQ -> wq.q / ( wq.q + 1 ) }

//val WR0 = WR(0.0, 0.0)
//val Ai_nominator = (
//        ExpressionNode( "4 (w / ln - 1) r" ) { wr: WR -> 4.0 * ( wr.w.vOverLog1p() - 1.0 ) * wr.r }
//                + ExpressionNode( "3 (w - ln)" ) { wr: WR -> 3.0 * ( wr.w - wr.w.log1p() ) }
//                - ExpressionNode( "w ln" ) { wr: WR -> wr.w * wr.w.log1p() }
//                - ExpressionNode( "r ln" ) { wr: WR -> wr.r * wr.w.log1p() }
//                + ExpressionNode( "w" ) { wr: WR -> wr.w }
//                + ExpressionNode( "r ln / w" ) { wr: WR -> wr.r / wr.w.vOverLog1p() }
//        )
//val Ai = ( Ai_nominator / ExpressionNode( "w + r" ) { wr: WR -> wr.w + wr.r } ).withValue( WR0, Number( 1.0 ) ) /
//        ExpressionNode( "2 (1 + r)" ) { wr: WR -> 2.0 * ( 1.0 + wr.r ) }
//
//fun ternarySearchOptimum( f: Expression<W>, from: Number, to: Number, dir: Direction, eps: Number ): Number {
//    var left = from
//    var right = to
//    while ( right - left > eps ) {
//        val l = ( left * 2.0 + right ) / 3.0;
//        val r = ( left + right * 2.0 ) / 3.0;
//        val lv = f.eval( W( l ) )
//        val rv = f.eval( W( r ) )
//        if ( dir.get( lv, rv ) == lv ) {
//            right = r
//        } else {
//            left = l
//        }
//    }
//    return ( left + right ) * 0.5
//}