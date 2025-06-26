package lzw;

import java.io.*;

//////////////////////////////////////////////////////////////////
//               Lzw log tester class                           //
//               ---------------------                          //
//               Name:    Daniel Moore                          //
//               ID:      9865293                               //
//                                                              //
////////////////////////////////////////////////////////////////// 
//written with Emacs


/**
 *simple class that tests output of log files from compression and decompression classed
 *This was used for debugging only
 *@author Daniel Moore
 *@version 1.0
 */
public class logTester{

    


    /**
     *matches contents of log files against each other and outputs the line there is a mismatch
     *log file is in the format:
     *<code> \n  where code is a string of length 8 and \n is endline
     */
    public static void main(String [] args){
	//initialise variables
	File comp = new File("logCompress.txt");
	File decomp = new File("logDecompress.txt");
	//buffers to hold codes
	byte [] cb = new byte[8];
	byte [] db = new byte[8];
	//buffer to read in endline characters
	byte [] lf = new byte[1];
	
	int c=0, d =0;
	int counter = 0;

	try{
	    InputStream cs = new FileInputStream(comp);
	    InputStream ds = new FileInputStream(decomp);

	    //while inputs match
	    while(c == d){
		cs.read(cb);
		cs.read(lf);
		ds.read(db);
		ds.read(lf);
		c = Integer.parseInt(new String(cb));
		d = Integer.parseInt(new String(db));
		counter++;
	    }
	    //output line number mismatch is at
	    if(c != d)
		System.out.println("Match error at line: " + counter);

	}catch(Exception e){
	    e.printStackTrace();
	}

    }

}
