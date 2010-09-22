import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

public class Gen {

	public static void rec( Scanner in, PrintWriter out ) {
		while ( in.hasNextLine() ) {
			String s = in.nextLine();
			if ( s.contains( "input" ) ) {
				if ( s.substring( 0, s.indexOf( "input" ) ).contains( "%" ) ) {
					continue;
				}
				String t = s.substring( s.indexOf( "input" ) + "input".length() ).trim() + ".tex";
				try {
					rec( new Scanner( new File( t ) ), out );
				} catch ( FileNotFoundException e ) {
					System.err.println( "File not found " + t );
				}	
			} else {
				out.println( s );
			}
		}
	}

	public static boolean compile( String s ) {
		try {
			Scanner in = new Scanner( new File( s ) );
			PrintWriter out = new PrintWriter( new File( s.split( "\\." )[0] + "c." + s.split( "\\." )[1] ) );
			rec( in, out );
			out.close();
			return true;
		} catch ( FileNotFoundException e ) {
			return false;
		}	
	}

	public static void main( String[] args ) {
		if ( args.length > 1 ) {
			for ( int i = 1; i < args.length; i ++ ) {
				if ( ! compile( args[i] ) ) {
					System.err.println( "No file with name " + args[i] );
				}
			}
		} else {
			Scanner in;
			try {
				in = new Scanner( new File( "gen.ini" ) );
			} catch ( FileNotFoundException e ) {
				System.err.println( "Ini file not found" );
				return;
			}
			while ( in.hasNextLine() ) {
				String s = in.nextLine();
				if ( ! compile( s ) ) {
					System.err.println( "No file with name " + s );
				}
			}
		}
	}
}