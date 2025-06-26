package lzw;

import java.io.*;

//////////////////////////////////////////////////////////////////
//                  Lzw Entry class                             //
//               ---------------------                          //
//               Name:    Daniel Moore                          //
//               ID:      9865293                               //
//                                                              //
////////////////////////////////////////////////////////////////// 
//written with Emacs


/**
 *Entry class to Lzw compression program 
 *@author Daniel Moore
 *@version 3.0
 */
public class Lzw{

    ///MEMBERS
    private File file = null;
    private boolean squash = true;


    public static void main(String [] args){
	Lzw l = new Lzw(args);
    }

    ///CONSTRUCTORS
    /**
     *program is launched from here
     *@param args command line params
     */
    public Lzw(String [] args){
	if(this.isValid(args))
	    this.run();
	else
	    this.printUsage();
    }

    /**
     *Prints useful usage information
     */
    public void printUsage(){
	System.out.println("");
	System.out.println("Usage: Lzw <filepath>");
    }


    /**
     *Validates args to see if params are ok
     *@param args the String array to process
     *@return boolean inidcating args is ok to process
     */
    public boolean isValid(String [] args){
	if(args.length < 1)
	    return false;
	file = new File(args[0]);
	if(file.exists())
	    return true;
	System.out.println("Cant read file.");
	return false;
    }



    /**
     *Start proccessing file here
     */
    public void run(){

	//check file extension
	String ext = file.getName();

	if(ext.endsWith(".txt")){
	    //compress
	    Compress c = new Compress(file);
	    c.run();	 
	    
	}
	else if(ext.endsWith(".lzw")){
	    //decompress
	    Decompress d = new Decompress(file);
	    d.run();	    
	    
	}
	else{
	    //exit
	    System.out.println("Unrecognized file format.");
	    return;
	}

    }

 
}
