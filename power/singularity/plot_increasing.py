def K_s( s, p ):
    import math
    return math.pow( 1 + 1 / ( s * s ), p / 2 - 1 ) * ( 1 - ( p - 1 ) / ( s * s ) )


def main():
    s_step = 1e-3
    s_start = s_step
    s_end = 100
    p_step = 1e-3
    p_start = 1
    p_end = 100
    x = []
    y = []
    import numpy as np
    for s in np.arange( s_start, s_end, s_step ):
        for p in np.arange( p_start, p_end, p_step ):
            value = K_s( s, p )
            if value > 1:
                x.append( s )
                y.append( p )
                print s, p
                return
    print len( x )


if __name__ == "__main__":
    main()