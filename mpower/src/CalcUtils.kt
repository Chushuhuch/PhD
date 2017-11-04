import kotlin.coroutines.experimental.buildSequence

enum class DEBUG_LEVEL( val level: Int ) {
    NO_DEBUG( 0 ),
    PRINT_EVERY_CELL( 10 ),
    REPORT_CELL_OPTIMUM( 20 ),
    REPORT_ALL_CELL_CORNER_VALUES( 30 ),
}

val debug_level: DEBUG_LEVEL = DEBUG_LEVEL.NO_DEBUG

val MIN_MARGIN = Number( 1E-5 )

class Number( val v : Double ) : Comparable<Number> {
    constructor( n: Int ) : this( n.toDouble() )
    constructor( n: Long ) : this( n.toDouble() )

    operator fun plus( n: Number ) = Number( v + n.v )
    operator fun plus( d: Double ) = Number( v + d )
    operator fun minus( n: Number ) = Number( v - n.v )
    operator fun minus( d: Double ) = Number( v - d )
    operator fun times( n: Number ) = Number( v * n.v )
    operator fun div( n: Number ) = Number( v / n.v )
    operator fun div( d: Double ) = Number( v / d )
    operator fun compareTo( d: Double ) = v.compareTo( d )

    override fun compareTo( other: Number ) = v.compareTo( other.v )

    override fun toString() = v.toString()

    fun abs() = Number( Math.abs( v ) )
    fun log1p() = Number( Math.log1p( v ) )
    fun vOverLog1p() = Number( if ( v == 0.0 ) 1.0 else v / Math.log1p( v ) )
    fun pow( p: Double ) = Number( Math.pow( v, p ) )
    fun sanityCheck() {
        if ( v.isNaN() ) throw RuntimeException( "NaN in computations" )
    }
}

operator fun Double.plus( n: Number ) = Number( this + n.v )
operator fun Double.minus( n: Number ) = Number( this - n.v )
operator fun Double.times( n: Number ) = Number( this * n.v )
operator fun Double.div( n: Number ) = Number( this / n.v )


fun min( a: Number?, b: Number? ) = if ( a == null ) b else if ( b == null ) a else minOf( a, b )
fun max( a: Number?, b: Number? ) = if ( a == null ) b else if ( b == null ) a else maxOf( a, b )

abstract class VariableSet {
    abstract fun getComponents(): List<Number>
    abstract fun setComponent( i: Int, value: Number )
    private fun size() = getComponents().size
    private operator fun get(i: Int ) = if ( i >= 0 && i < size() ) getComponents()[i] else throw IndexOutOfBoundsException()
    private operator fun set(i: Int, value: Number ) = if ( i >= 0 && i < size() ) setComponent( i, value ) else throw IndexOutOfBoundsException()
    abstract fun <VS : VariableSet> copy(): VS

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

abstract class Expression<VS : VariableSet> {
    abstract fun eval( v: VS, dir: Direction = Direction.Max ): Number
    abstract fun optimize( from: VS, to: VS, dir: Direction ): Number
    override abstract fun toString(): String

    fun reportOptimum( optimum: Number, dir: Direction ) = if ( debug_level >= DEBUG_LEVEL.REPORT_CELL_OPTIMUM ) println("$dir to $this: $optimum") else Unit

    operator fun plus( e: Expression<VS> ) = SumExpression( this, e )
    operator fun minus( e: Expression<VS> ) = SubtExpression( this, e )
    operator fun times( e: Expression<VS> ) = TimesExpression( this, e )
    operator fun div( e: Expression<VS> ) = DivExpression( this, e )
}

open class ExpressionNode<VS : VariableSet>( private val s: String, private val f: ( VS ) -> Number ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = f( v )
    override fun toString() = s

    override fun optimize( from: VS, to: VS, dir: Direction ): Number {
        var optimum: Number? = null
//        println( "Combining $from and $to" )
        for ( vs in from.combine( to ) ) {
            val result = eval( vs, dir )
            result.sanityCheck()
//            println( "Evaluated $vs -> $result" )
            optimum = dir.get( optimum, result )
        }
        optimum!!.sanityCheck()
        reportOptimum( optimum, dir )
        return optimum
    }

    fun withValue( key: VS, value: Number ) : ExpressionNode<VS> = object : ExpressionNode<VS>( s, f ) {
        override fun eval( v: VS, dir: Direction ) = if ( v == key ) value else super.eval( v, dir )
    }
}

class SumExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) + right.eval( v, dir )
    override fun toString() = "$left + $right"
    override fun optimize( from: VS, to: VS, dir: Direction ): Number {
        val optimum = left.optimize( from, to, dir ) + right.optimize( from, to, dir )
        reportOptimum( optimum, dir )
        return optimum
    }
}

class SubtExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) - right.eval( v, dir.opposite() )
    override fun toString() = "$left - ($right)"
    override fun optimize( from: VS, to: VS, dir: Direction ): Number {
        val optimum = left.optimize( from, to, dir ) - right.optimize( from, to, dir.opposite() )
        reportOptimum( optimum, dir )
        return optimum
    }
}

class TimesExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) * right.eval( v, dir )
    override fun toString() = "[$left] * [$right]"
    override fun optimize( from: VS, to: VS, dir: Direction ): Number {
        val optimum = left.optimize( from, to, dir ) * right.optimize( from, to, dir )
        reportOptimum( optimum, dir )
        return optimum
    }
}

class DivExpression<VS : VariableSet>( private val left: Expression<VS>, private val right: Expression<VS> ) : Expression<VS>() {
    override fun eval( v: VS, dir: Direction ) = left.eval( v, dir ) / right.eval( v, dir.opposite() )
    override fun toString() = "[$left] / [$right]"
    override fun optimize( from: VS, to: VS, dir: Direction ): Number {
        val optimum = left.optimize( from, to, dir ) / right.optimize( from, to, dir.opposite() )
        reportOptimum( optimum, dir )
        return optimum
    }
}

// proves dir /min/max/ f is bound or more strict
fun <VS : VariableSet> proveInequality( f: Expression<VS>, from: VS, to: VS, dir: Direction, bound: Number, minParts: Long = 1, maxParts: Long = Long.MAX_VALUE ) {
    var parts = minParts
    while ( true ) {
        println( "Trying with $parts parts" )
        var optimum: Number? = null
        var where: VS? = null
        for ( ( localFrom, localTo ) in from.split( to, parts ) ) {
            if ( debug_level >= DEBUG_LEVEL.PRINT_EVERY_CELL ) println( "Optimizing on $localFrom - $localTo" )
            val localOptimum = f.optimize( localFrom, localTo, dir )
            val newOptimum = dir.get( optimum, localOptimum )
            if ( newOptimum != optimum ) {
                optimum = newOptimum
                where = localFrom
            }
        }
        if ( dir.get( optimum, bound ) == bound && ( optimum!! - bound ).abs() > MIN_MARGIN ) {
            println( "Using partition into $parts parts got optimum $optimum near $where which is better than desired $bound" )
            return
        }
        println( "Best: $optimum near $where" )
        parts *= 10
        if ( parts > maxParts ) return
    }
}