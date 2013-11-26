import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnFormulae {
    
//  final static String[] FORMULA_START = { "$$", "$", "\\begin{equation}", "\\begin{equation*}", "\\begin{eqnarray}", "\\begin{eqnarray*}", "\\begin{multline}", "\\begin{multline*}", "\\begin{picture}", "\\begin{align}", "\\begin{align*}", "\\begin{tabular}", "\\begin{tabular*}", "\\begin{gather}", "\\begin{gather*}" };
//  final static String[] FORMULA_END = { "$$", "$", "\\end{equation}", "\\end{equation*}", "\\end{eqnarray}", "\\end{eqnarray*}", "\\end{multline}", "\\end{multline*}", "\\end{picture}", "\\end{align}", "\\end{align*}", "\\end{tabular}", "\\end{tabular*}", "\\end{gather}", "\\end{gather*}" };
  final static String[] FORMULA_START = { "$$", "$" };
  final static String[] FORMULA_END = { "$$", "$" };
    
  public static void main( String[] args ) throws IOException {
    String name = args[0];
    String original = readFile( "..\\" + name );
    String translation = readFile( name );

//    $a$ $b$
//    translation = translation.replace( "$ $", "$" );

    Map < String, String > formulaeMap = new HashMap<>();
    List < String > formulae = getFormulae( original );
    for ( String formula : formulae ) {
      formulaeMap.put( removeWhitespace( formula ), formula );
    }

    formulae = getFormulae( translation );
    for ( String formula : formulae ) {
        String replacement = formulaeMap.get( removeWhitespace( formula ) );
        if ( replacement != null ) {
          translation = translation.replace( formula, replacement );
        }
    }

    PrintWriter out = new PrintWriter( new File( name + "f" ) );
    out.println( translation );
    out.close();
  }

  private static String readFile( String name ) throws IOException {
    BufferedReader in = new BufferedReader( new FileReader( new File( name ) ) );
    StringBuilder result = new StringBuilder();
    while ( true ) {
      String s = in.readLine();
      if ( s == null ) {
        break;
      }
      result.append( s ).append( '\n' );
    }
    return result.toString();
  }

  private static String removeWhitespace( String s ) {
    return s.replaceAll( "[ \t\n\r]", "" ).toLowerCase();
  }

  private static List < String > getFormulae( String s ) throws IOException {
    List < String > result = new ArrayList<>();
    for ( int i = 0; i < s.length(); i ++ ) {
      for ( int j = 0; j < FORMULA_START.length; j ++ ) {
        if ( s.substring( i ).startsWith( FORMULA_START[j] ) ) {
//            System.out.println( "!!!" );
//            System.out.println( FORMULA_START[j] );
//            System.out.println( s.substring( i, Math.min( i + 100, s.length() ) ) );
            int endIndex = s.indexOf( FORMULA_END[j], i + FORMULA_START[j].length() ) + FORMULA_END[j].length();
            result.add( s.substring( i, endIndex ) );
//            System.out.println( "------" );
//            System.out.println( result.get( result.size() - 1 ) );
//            System.in.read();
            i = endIndex;
        }
      }
    }
    return result;
  }
}