def symm(x, y):
    print x
    print y
    y_sorted = sorted(set(y))
    x_new = []
    y_new = []
    for level in y_sorted:
        y_new.append( level )
        x_width = 0.0
        for i in range( len( x ) - 1 ):
            if y[i] >= level and y[i + 1] >= level:
                x_width += x[i + 1] - x[i]
            elif y[i] < level and y[i + 1] < level:
                pass
            else:
                x_width += ( max( y[i], y[i + 1] ) - level ) / abs( y[i + 1] - y[i] ) * ( x[i + 1] - x[i] )
            print i, x_width
        x_new.append( -x_width / 2 )
        print "got: ", -x_width / 2, level

    for i in reversed(range(len(x_new))):
        x_new.append( -x_new[i] )
        y_new.append( y_new[i] )
    print x_new
    print y_new
    return ( x_new, y_new )


def draw_levels(u_x, u_y, plt):
    for y in [0.25 * (i + 1) for i in range(7)]:
        print 'y', y
        x1 = None
        for i in range(len(u_x) - 1):
            take = abs(u_y[i] - y) > EPS and abs(u_y[i + 1] - y) > EPS and (u_y[i] - y) * (u_y[i + 1] - y) < 0
            if not take:
                if abs(u_y[i] - y) <= EPS:
                    take = i > 0 and ( u_y[i - 1] - u_y[i] ) * ( u_y[i + 1] - u_y[i] ) < 0
                # elif abs(u_y[i + 1] - y) <= EPS:
                #     take = i < len(u_x) - 2 and ( u_y[i] - u_y[i + 1] ) * ( u_y[i + 2] - u_y[i + 1] ) < 0
            if take:
                x = u_x[i] + (u_x[i + 1] - u_x[i]) * (y - u_y[i]) / (u_y[i + 1] - u_y[i])
                print "taking", x, i
                if x1 is None:
                    x1 = x
                else:
                    x2 = x
                    print "drawing", x1, x2
                    plt.plot([x1, x2], [y, y], "--", color="red")
                    x1 = None


import matplotlib.pyplot as plt
EPS = 1e-5
u_x = [-1.0, -0.5, 0, 0.5, 1.0]
u_y = [0.0, 2.0, 0.5, 1.0, 0.0]
# u_x = [-1.0, -0.7, -0.2, 0.2, 0.5, 1.0]
# u_y = [0.0, 2.0, 0.5, 1.0, 0.0, 0.0]
plt.plot( u_x, u_y, linewidth = 3.0 )
plt.axvline( x = 0, color='k', linewidth = 2.5 )
plt.axhline( y = 0, color='k', linewidth = 2.5 )
draw_levels(u_x, u_y, plt)
plt.suptitle("u(x)", fontsize = 36, family='serif')
plt.savefig("symmetrization_exmpl_u.pdf")
plt.close()

us_x, us_y = symm( u_x, u_y )
plt.axvline( x = 0, color='k' )
plt.plot( us_x, us_y, linewidth = 3.0 )
plt.axvline( x = 0, color='k', linewidth = 2.5 )
plt.axhline( y = 0, color='k', linewidth = 2.5 )
draw_levels(us_x, us_y, plt)
plt.suptitle("u*(x)", fontsize = 36, family='serif')
# plt.figtext( 0.2, 0.8, "u(x)")
# plt.figtext( 0.6, 0.8, "u*(x)")
# plt.show()
plt.savefig("symmetrization_exmpl_us.pdf")
plt.close()