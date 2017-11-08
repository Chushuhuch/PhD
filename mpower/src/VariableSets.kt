class W( var w: Number ) : VariableSet() {
    constructor( x: Double ) : this( Number( x ) )
    override fun name() = "w"
    override fun getComponents() = listOf( w )
    override fun get( i: Int ) = if ( i == 0 ) w else throw IndexOutOfBoundsException()
    override fun size() = 1
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
    override fun name() = "w, r"
    override fun getComponents() = listOf( w, r )
    override fun get( i: Int ) = if ( i == 0 ) w else if ( i == 1 ) r else throw IndexOutOfBoundsException()
    override fun size() = 2
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
    override fun name() = "w, q"
    override fun getComponents() = listOf( w, q )
    override fun get( i: Int ) = if ( i == 0 ) w else if ( i == 1 ) q else throw IndexOutOfBoundsException()
    override fun size() = 2
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
    override fun name() = "v, q"
    override fun getComponents() = listOf( v, q )
    override fun get( i: Int ) = if ( i == 0 ) v else if ( i == 1 ) q else throw IndexOutOfBoundsException()
    override fun size() = 2
    override fun setComponent(i: Int, value: Number ) {
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
    override fun name() = "v, r"
    override fun getComponents() = listOf( v, r )
    override fun get( i: Int ) = if ( i == 0 ) v else if ( i == 1 ) r else throw IndexOutOfBoundsException()
    override fun size() = 2
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
