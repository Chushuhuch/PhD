import Number.Constants.ZERO
import Number.Constants.mContext
import Segment.Companion.EMPTY
import java.math.BigDecimal
import java.math.MathContext
import kotlin.coroutines.experimental.buildSequence
//import kotlin.system.exitProcess
//import kotlin.test.currentStackTrace

enum class DEBUG_LEVEL( val level: Int ) {
    NO_DEBUG( 0 ),
    PRINT_EVERY_CELL( 10 ),
    REPORT_CELL_OPTIMUM( 20 ),
    REPORT_ALL_CELL_CORNER_VALUES( 30 ),
    REPORT_EVALUATIONS( 40 ),
}

val debug_level: DEBUG_LEVEL = DEBUG_LEVEL.NO_DEBUG

fun log(level: DEBUG_LEVEL, msg: () -> String ) {
    if ( level <= debug_level ) {
        println( msg() )
        System.out.flush()
    }
}

val MIN_MARGIN = Number( 1E-5 )

//class NumberDouble( val v : Double ) : Comparable<Number> {
//
//    init {
//        if ( v.isNaN() ) throw RuntimeException( "NaN in the computations" )
//    }
//
//    constructor( n: Int ) : this( n.toDouble() )
//    constructor( n: Long ) : this( n.toDouble() )
//
//    operator fun unaryMinus() = Number( -v )
//    operator fun plus( n: Number ) = Number( v + n.v )
//    operator fun plus( d: Double ) = Number( v + d )
//    operator fun minus( n: Number ) = Number( v - n.v )
//    operator fun minus( d: Double ) = Number( v - d )
//    operator fun times( n: Number ) = Number( v * n.v )
//    operator fun times( d: Double ) = Number( v * d )
//    operator fun div( n: Number ) = Number( v / n.v )
//    operator fun div( d: Double ) = Number( v / d )
//    operator fun compareTo( d: Double ) = v.compareTo( d )
//
//    override fun compareTo( other: Number ) = v.compareTo( other.v )
//
//    override fun toString() = v.toString()
//
//    override fun equals( other: Any? ): Boolean {
//        if ( other !is Number ) return false
//        return v == other.v
//    }
//
//    override fun hashCode() = v.hashCode()
//
//    companion object Constants {
//        val ZERO = Number( 0.0 )
//        val log1pValues = HashMap<Double, Number>()
//        val vOverLog1pValues = HashMap<Double, Number>()
//    }
//
//    fun abs() = Number( Math.abs( v ) )
//    fun log1p() = log1pValues.getOrPut( v, { Number( Math.log1p( v ) ) } )
//    fun log1pi() = Number( Math.log1p( v ) - Math.log( v ) )
//    fun vOverLog1p() = vOverLog1pValues.getOrPut( v, { Number( if ( v == 0.0 ) 1.0 else v / Math.log1p( v ) ) } )
//    fun pow( p: Double ) = Number( Math.pow( v, p ) )
//}
//
//operator fun Double.plus( n: NumberDouble ) = NumberDouble( this + n.v )
//operator fun Double.minus( n: NumberDouble ) = NumberDouble( this - n.v )
//operator fun Double.times( n: NumberDouble ) = NumberDouble( this * n.v )
//operator fun Double.div( n: NumberDouble ) = NumberDouble( this / n.v )

class Number( val v : BigDecimal ) : Comparable<Number> {

    constructor( n: Double ) : this( BigDecimal( n.toString(), mContext ) )
    constructor( n: Int ) : this( BigDecimal( n, mContext ) )
    constructor( n: Long ) : this( BigDecimal( n, mContext ) )

    companion object Constants {
        val mContext = MathContext( 17 )
//        val mContext = MathContext( 18 )
//        val mContext = MathContext( 20 )
//        val mContext = MathContext.DECIMAL128
        val ZERO = Number( 0.0 )
        val log1pValues = HashMap<Number, Number>()
        val vOverLog1pValues = HashMap<Number, Number>()
        val LN10 = BigDecimal( "2.30258509299404568401799145468436420760110148862877", mContext )
        val LN4 = BigDecimal( "1.3862943611198906188344642429163531361510002687205105", mContext )
    }

    operator fun unaryMinus() = Number( v.negate( mContext ) )
    operator fun plus( n: Number ) = Number( v.add( n.v, mContext ) )
    operator fun plus( d: Int ) = Number( v.add( BigDecimal( d ), mContext ) )
    operator fun minus( n: Number ) = Number( v.subtract( n.v, mContext ) )
//    operator fun minus( d: Double ) = Number( v - d )
    operator fun times( n: Number ) = Number( v.multiply( n.v, mContext ) )
//    operator fun times( d: Double ) = Number( v * d )
    operator fun div( n: Number ) = Number( v.divide( n.v, mContext ) )
//    operator fun div( d: Double ) = Number( v / d )
//    operator fun compareTo( d: Double ) = v.compareTo( d )

    override fun compareTo( other: Number ) = v.compareTo( other.v )

    override fun toString() = v.toString()

    override fun equals( other: Any? ): Boolean {
        if ( other !is Number ) return false
        return v == other.v
    }

    override fun hashCode() = v.hashCode()

    fun abs() = Number( v.abs( mContext ) )
    fun log1p() = log1pValues.getOrPut( this, {
        var sum = BigDecimal( 0, mContext )
        val x = v.subtract( BigDecimal( 3 ), mContext ).divide( BigDecimal( 4 ), mContext )
        val y = x.divide( x.add( BigDecimal( 2 ), mContext ), mContext )
        val y2 = y.multiply( y, mContext )
        var term = y
        for ( p in 1 .. Int.MAX_VALUE step 2 ) {
            val prevSum = sum
            sum = sum.add( term.divide( BigDecimal( p ), mContext), mContext)
            if ( sum == prevSum ) break
            term = term.multiply( y2, mContext )
        }
        return Number( sum.multiply( BigDecimal( 2 ), mContext )
                .add( LN4, mContext ) )
//        var sum = BigDecimal( 0, mContext )
//        var pow = 0
//        val y = if ( v <= BigDecimal.ONE ) {
//            v.divide( v.add( BigDecimal( 2 ), mContext ), mContext )
//        } else {
//            val x = v.add( BigDecimal.ONE, mContext )
//            while ( x >= BigDecimal.TEN ) {
//                x.scaleByPowerOfTen( -1 )
//                pow ++
//            }
//            x.subtract( BigDecimal.ONE, mContext ).divide( x.add( BigDecimal.ONE, mContext ), mContext )
//        }
//        val y2 = y.multiply( y, mContext )
//        var term = y
//        for ( p in 1 .. Int.MAX_VALUE step 2 ) {
//            val prevSum = sum
//            sum = sum.add( term.divide( BigDecimal( p ), mContext), mContext)
//            if ( sum == prevSum ) break
//            term = term.multiply( y2, mContext )
//        }
//        return Number( sum.multiply( BigDecimal( 2 ), mContext )
//                .add( LN10.multiply( BigDecimal( pow ), mContext ), mContext ) )
    } )
//    fun log1pi() = Number( Math.log1p( v ) - Math.log( v ) )
    fun vOverLog1p() = vOverLog1pValues.getOrPut( this, {
        if ( v == BigDecimal.ZERO ) Number( BigDecimal.ONE ) else this / log1p()
        } )
//    fun pow( p: Double ) = Number( Math.pow( v, p ) )
}

operator fun Int.plus( n: Number ) = Number( n.v.add( BigDecimal( this ), mContext ) )
operator fun Int.times( n: Number ) = Number( n.v.multiply( BigDecimal( this ), mContext ) )
operator fun Int.div( n: Number ) = Number( BigDecimal( this, mContext ).divide( n.v, mContext ) )

fun min( a: Number?, b: Number? ) = if ( a == null ) b else if ( b == null ) a else minOf( a, b )
fun max( a: Number?, b: Number? ) = if ( a == null ) b else if ( b == null ) a else maxOf( a, b )

abstract class VariableSet {
    abstract fun getComponents(): List<Number>
    abstract fun setComponent( i: Int, value: Number )
    abstract fun size(): Int
    abstract operator protected fun get( i: Int ): Number
    private operator fun set( i: Int, value: Number ) = if ( i >= 0 && i < size() ) setComponent( i, value ) else throw IndexOutOfBoundsException()
    abstract fun <VS : VariableSet> copy(): VS
    abstract fun name(): String

    fun <VS : VariableSet> combine( with: VS ) = buildSequence {
        for ( m in 0 until ( 1 shl size() ) ) {
            val vs = copy<VS>()
            for ( i in 0 until size() ) {
                if ( m and ( 1 shl i ) != 0 ) vs[i] = with[i]
            }
            yield( vs )
        }
    }

    fun <VS : VariableSet> split( with: VS, parts: Long ) = buildSequence {
        val cur = IntArray( size(), { 0 } )
        while ( true ) {
            val from = copy<VS>()
            val to = copy<VS>()
            for ( i in 0 until size() ) {
                from[i] = ( this@VariableSet[i] + ( with[i] - this@VariableSet[i] ) * Number( cur[i] ) / Number( parts ) )
                to[i] = ( this@VariableSet[i] + ( with[i] - this@VariableSet[i] ) * Number( cur[i] + 1 ) / Number( parts ) )
            }
            yield( Pair( from, to ) )
            for ( i in 0 until size() ) {
                cur[i] ++
                if ( cur[i] >= parts ) {
                    cur[i] = 0
                } else {
                    break
                }
            }
            if ( cur.all { it == 0 } ) break
        }
    }

    override fun toString() = "(${getComponents().joinToString( ", " )})"
}


enum class Direction( val get: ( Number?, Number? ) -> Number? ) { Min( ::min ), Max( ::max ) }

fun Direction.opposite() = if ( this == Direction.Min) Direction.Max else Direction.Min

class Segment( from: Number, to: Number ) {
    val from = if ( from == ZERO ) ZERO else from
    val to = if ( to == ZERO ) -ZERO else to

    companion object {
        val EMPTY = Segment( Number( 1.0 ), Number( 0.0 ) )
    }

//    operator fun get( dir: Direction ) = if ( dir == Direction.Min ) from else to

    operator fun plus( s: Segment ) : Segment {
        checkNonEmpty()
        s.checkNonEmpty()
        return Segment( from + s.from, to + s.to )
    }
    operator fun minus( s: Segment ) : Segment{
        checkNonEmpty()
        s.checkNonEmpty()
        return Segment( from - s.to, to - s.from )
    }
    operator fun times( s: Segment ) : Segment {
        checkNonEmpty()
        s.checkNonEmpty()
        val candidates = listOf( from * s.from, from * s.to, to * s.from, to * s.to )
        return Segment( candidates.min()!!, candidates.max()!! )
    }
    operator fun div( s: Segment ) : Segment {
        checkNonEmpty()
        s.checkNonEmpty()
        if ( s.from < ZERO && s.to > ZERO ) throw RuntimeException()
        val candidates = ArrayList<Number>()
        for ( x in listOf(from, to ) ) {
            for ( y in listOf( s.from, s.to ) ) {
                if ( x == ZERO && y == ZERO ) throw RuntimeException( "0/0 not supported" )
                candidates += x / y
            }
        }
        // candidates are empty iff both intervals are [0, 0]
        return Segment( candidates.min()!!, candidates.max()!! )
    }
    operator fun get( dir: Direction ) = if ( dir == Direction.Min ) from else to

    fun unite( s: Segment ) = if ( this == EMPTY ) s else Segment( minOf( from, s.from ), maxOf( to, s.to ) )

    private fun checkNonEmpty() {
        if ( this == EMPTY ) throw RuntimeException( "Empty range unexpected" )
    }

    override fun toString() = "[$from, $to]"

    override fun equals( other: Any? ): Boolean {
        if ( other !is Segment ) return false
        return from == other.from && to == other.to
    }

    override fun hashCode() = from.hashCode() xor to.hashCode()
}

operator fun Direction.get( s: Segment ) = if ( this == Direction.Min ) s.from else s.to

abstract class Expression<VS : VariableSet> {
    abstract fun eval( v: VS, dir: Direction = Direction.Max ): Number
    abstract fun optimize( from: VS, to: VS ): Segment
    override abstract fun toString(): String

    fun reportOptimum( optimum: Segment) = log( DEBUG_LEVEL.REPORT_CELL_OPTIMUM, { "$this: ${optimum.from} .. ${optimum.to}" } )

    operator fun plus( e: Expression<VS> ) = SumExpression( this, e )
    operator fun minus( e: Expression<VS> ) = SubtExpression( this, e )
    operator fun times( e: Expression<VS> ) = TimesExpression( this, e )
    operator fun div( e: Expression<VS> ) = DivExpression( this, e )
}

open class ExpressionNode<VS : VariableSet>( private val s: String, private val f: ( VS ) -> Number ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = f( v )
    override fun toString() = s

    override fun optimize( from: VS, to: VS ): Segment {
        var min: Number? = null
        var max: Number? = null
//        log( "Combining $from and $to" )
        for ( vs in from.combine( to ) ) {
            val resultMin = eval( vs, Direction.Min )
            val resultMax = eval( vs, Direction.Max )
//            log( "Evaluated $vs -> $result" )
            min = Direction.Min.get( min, resultMin )
            max = Direction.Max.get( max, resultMax )
        }
        val result = Segment( min!!, max!! )
        reportOptimum( result )
        return result
    }
}

fun <VS : VariableSet> Expression<VS>.withValue( key: VS, value: Number )
        = object : ExpressionNode<VS>( toString() + " {$key -> $value}", { v: VS ->
    log( DEBUG_LEVEL.REPORT_EVALUATIONS, { "expr: $this, v: $v, key: $key, value: $value, v == key: ${v == key}" } )
    if ( v == key ) {
        value
    } else eval( v )
} ) {}

fun <VS : VariableSet> Expression<VS>.withValues( keyCondition: ( VS ) -> Boolean, value: Number )
        = object : ExpressionNode<VS>( toString() + " {conditioned -> $value}", { v: VS ->
    log( DEBUG_LEVEL.REPORT_EVALUATIONS, { "expr: $this, v: $v, value: $value, condition holds: ${keyCondition( v )}" } )
    if ( keyCondition( v ) ) {
        value
    } else eval( v )
} ) {}

//class WithValueExpression<VS : VariableSet> private constructor( private val expr: Expression<VS>, private val key: VS, private val value: Number )
//    : ExpressionNode<VS>( expr.toString(), { v: VS -> if ( v == key ) value else expr.eval( v ) } ) {
//
//}

class SumExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) + right.eval( v, dir )
    override fun toString() = "$left + $right"
    override fun optimize( from: VS, to: VS ): Segment {
        val optimum = left.optimize( from, to ) + right.optimize( from, to )
        reportOptimum( optimum )
        return optimum
    }
}

class SubtExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) - right.eval( v, dir.opposite() )
    override fun toString() = "$left - ($right)"
    override fun optimize( from: VS, to: VS ): Segment {
        val optimum = left.optimize( from, to ) - right.optimize( from, to )
        reportOptimum( optimum )
        return optimum
    }
}

class TimesExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) * right.eval( v, dir )
    override fun toString() = "[$left] * [$right]"
    override fun optimize( from: VS, to: VS ): Segment {
        val optimum = left.optimize( from, to ) * right.optimize( from, to )
        reportOptimum( optimum )
        return optimum
    }
}

class DivExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) / right.eval( v, dir.opposite() )
    override fun toString() = "[$left] / [$right]"
    override fun optimize( from: VS, to: VS ): Segment {
        val optimum = left.optimize( from, to ) / right.optimize( from, to )
        reportOptimum( optimum )
        return optimum
    }
}

// proves dir /min/max/ f is bound or more strict
fun <VS : VariableSet> proveInequality( f: Expression<VS>, from: VS, to: VS, dir: Direction, bound: Number, minParts: Long = 1, maxParts: Long = Long.MAX_VALUE ) {
    println( "Proving for ${from.name()} from $from to $to")
    var parts = minParts
    while ( true ) {
        log( DEBUG_LEVEL.NO_DEBUG, { "Trying with $parts parts" } )
        var range: Segment = EMPTY
        var where: VS? = null
        var cnt = 0
        val start = System.currentTimeMillis()
        for ( ( localFrom, localTo ) in from.split( to, parts ) ) {
            log( DEBUG_LEVEL.PRINT_EVERY_CELL, { "Optimizing on $localFrom - $localTo" } )
            val localRange = f.optimize( localFrom, localTo )
            val newRange = range.unite( localRange )
            if ( dir[newRange] != dir[range] ) {
                range = newRange
                where = localFrom
            }
            cnt ++
            if ( cnt and ( ( 1 shl 20 ) - 1 ) == 0 ) {
                println( "$cnt rectangles processed. ${System.currentTimeMillis() - start} passed.")
            }
        }
        if ( dir.get( range[dir], bound ) == bound && ( range[dir] - bound ).abs() > MIN_MARGIN ) {
            log( DEBUG_LEVEL.NO_DEBUG, { "Using partition into $parts parts got optimum $range near $where which is better than desired $bound" } )
            return
        }
        log( DEBUG_LEVEL.NO_DEBUG, { "Best: $range near $where" } )
        parts *= 10
        if ( parts > maxParts ) return
    }
}