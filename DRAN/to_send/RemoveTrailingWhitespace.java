import java.io.*;
import java.util.*;

public class RemoveTrailingWhitespace {
  public static void main( String[] args ) throws IOException {
    File file = new File( args[0] );
    ArrayList < String > s = new ArrayList < String > ();
    Scanner in = new Scanner( file );
    while ( in.hasNextLine() ) {
      String t = in.nextLine();
      int i = t.length();
      do {
        i --;
      } while ( i >= 0 && t.charAt( i ) <= ' ' );
      s.add( t.substring( 0, i + 1 ) );
    }
    PrintWriter out = new PrintWriter( file );
    for ( String t : s ) {
      out.println( t );
    }
    out.close();
  }
}
