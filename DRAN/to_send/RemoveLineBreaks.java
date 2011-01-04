import java.io.*;
import java.util.*;

public class RemoveLineBreaks {

  static private String[] readFile( File inFile ) throws IOException {
    Scanner in = new Scanner( inFile );
    ArrayList < String > strings = new ArrayList < String > ();
    while ( in.hasNextLine() ) {
      strings.add( in.nextLine() );
    }
    return strings.toArray( new String[0] );
  }

  static private boolean[] getNecessary( String[] strings ) {
    boolean[] n = new boolean[strings.length - 1];
    int i = 0;
    while ( ! strings[i].contains( "\\begin{document}" ) ) {
      n[i] = true;
      i ++;
    }
    for ( i ++; i < strings.length - 1; i ++ ) {
      if ( i > 0 && strings[i - 1].contains( "%" ) ) {
        n[i - 1] = true;
      }
      String s = strings[i].trim();
      if ( s.length() == 0 ) {
        n[i - 1] = true;
        n[i] = true;
        continue;
      }
      if ( s.startsWith( "$$" ) ) {
        n[i - 1] = true;
      }
      if ( s.endsWith( "$$" ) ) {
        n[i] = true;
      }
      if ( s.charAt( 0 ) == '\\' ) {
        int j = 1;
        for ( ; j < s.length() && Character.isLetter( s.charAt( j ) ); j ++ );
        if ( j >= s.length() ) {
          n[i - 1] = true;
          n[i] = true;
        }
        if ( j < s.length() && s.charAt( j ) == '{' && s.charAt( s.length() - 1 ) == '}' ) {
          for ( j ++; j < s.length() - 1 && s.charAt( j ) != '}'; j ++ );
          if ( j == s.length() - 1 ) {
            n[i - 1] = true;
            n[i] = true;
          }
        }
      }
    }
    return n;
  }

  static private void writeFile( File outFile, String[] strings, boolean[] n, boolean test ) throws IOException {
    PrintWriter out = new PrintWriter( outFile );
    for ( int i = 0; i < n.length; i ++ ) {
      out.print( strings[i] );
      if ( n[i] ) {
        if ( test ) {
          out.print( "!" );
        }
        out.println();
      } else {
        if ( test ) {
          out.println();
        } else if ( ! strings[i].endsWith( " " ) && ! strings[i + 1].startsWith( " " ) ) {
          out.print( " " );
        }
      }
    }
    out.println( strings[strings.length - 1] );
    out.close();
  }

  public static void main( String[] args ) throws IOException {
    File from = new File( args[0] );
    File to = from;
    if ( args.length > 1 ) {
      to = new File( args[1] );
    }
    String[] strings = readFile( from );
    writeFile( to, strings, getNecessary( strings ), args.length >= 3 && args[2].equals( "test" ) );
  }
}
