package lzw;

import java.io.*;
import java.math.*;

//////////////////////////////////////////////////////////////////
//               Lzw Compression class                          //
//               ---------------------                          //
//               Name:    Daniel Moore                          //
//               ID:      9865293                               //
//    Features:     Optimized variable length bit packing       //
//                                                              //
////////////////////////////////////////////////////////////////// 
//written with Emacs

/**
 *This class uses the lzw compression algorithm to compress txt files
 *It uses a Trie as a lexical search tree
 *@author Daniel Moore
 *@version 6.0 
 */
public class Compress{



   //MEMBERS
    private File infile = null;
    private File outfile = null;
    private File logfile = new File("LogCompress.txt");

    private InputStream is = null;
    private OutputStream os = null;
    private OutputStream ls = null;
    private Trie root = null;
    private int keyCounter = 256;

    private int maxLength = 32;
    private byte [] buffer = null;
    private int numBits = 10;
    private int bitLimit = 512;
    private byte [] leftOver = new byte[1];
    private int numOldBits = 0;

    private int writeCounter = keyCounter;
    
    

    //CONSTRUCTORS
    /**
     *Takes file and initialises Trie and other members 
     *@param infile The file to read from
     */
    public Compress(File infile){

	this.infile = infile;
	String filename = infile.getName().substring(0, (infile.getName().length()-3)) + "lzw";
	this.outfile = new File(filename);
	this.root = new Trie(true);
	this.buffer = new byte[maxLength];

	try{
	    this.is = new FileInputStream(this.infile);    
	    this.os = new FileOutputStream(this.outfile);
	    //this.ls = new FileOutputStream(this.logfile);
	}catch(Exception e){
	    System.out.println("file access error");
	    System.exit(1);
	}
    }



    /**
     *runs main lzw algorithm here
     */
    public void run(){
	try{
	
	    String P, C = null;
	    byte [] b = new byte[1];
	    int eof = 0;
	    int len = is.available();
	    
	    //1. P: = empty
	    P = "";

	    for(int i=0; i<len; i++){
		//2. C: = next char in charStream
		eof = is.read(b);
		C = new String(b);	    
		//3. Is the string P+C in the dictonary?
		if(inDict(P+C)){
		    //a. If it is P = P+C
		    P = P + C;
		}
		else{
		    //b. If it isnt ..
		    //i. output the code for P
		    output(P);
		    //ii. add the String P+C to the dictionary
		    add(P, C);
		    //iii. P:= C (P only contains character C)
		    P = C;
		}
		//c. are there more characters in the charstream?
		if(i == len-1){
		
		    //if not:
		    //i. output the code word which denotes P to codestream
		    output(P);
		    //ii. END 
		    return;
		}
		//if yes go back to step 2 
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }


    /**
     *Adds a new phrase to the dictionary
     *@param P an existing phrase in the dictionary (the prefix to the new phrase)
     *@param C the post fix to the new phrase
     */
    private void add(String P, String C){

	//check if we are at maximum number of bits
	//if so, dont add any more phrases
	if(numBits > maxLength)
	    return;

	Trie temp = root;
	for(int i=0; i<P.length(); i++)
	    temp = temp.getNode(P.charAt(i)*1);

	temp.addNode(C.charAt(0)*1, keyCounter);
	keyCounter++;
	
    }



    /**
     *This method traverses the trie to get a code given a phrase
     *@param s the phrase to get the code for
     *@return the corrosponding code to s
     */
    private int getCode(String s){
	Trie temp = root;
	for(int i=0; i<s.length(); i++)
	    temp = temp.getNode(s.charAt(i)*1);
	return temp.getKey();
    }

    /**
     *Indicates whether a given string exists in the dictionary
     *@param s The phrase to test
     @return boolean inidicating whether phrase is in dictionary 
     */
    private boolean inDict(String s){

	Trie temp = root;
	for(int i=0; i<s.length(); i++)
	    if((temp = temp.getNode(s.charAt(i)*1)) == null)
		return false;
	
	return true;
    }




    /**
     *This is the bit twiddling method to pack codes of n number of bits into full bytes
     *@param s the phrase corrosponding to a code that will be packed into bytes
     */
    private void output(String s){

	//check if we need to increase the number of bits
	writeCounter++;
	if(writeCounter == bitLimit-1){
	    bitLimit *= 2;
	    numBits++;
	}

	//get the code into a byte array
	int n = getCode(s);
	//writeLog(n);

	byte [] temp = new byte[4];   //this holds the integer in a byte array
	for(int i=0; i<4; i++)
	    temp[i] = (byte)((n  & 0xffffffff) >> ((3-i)*8));

	//initialise variables
	int numBitsToWrite = numBits + numOldBits;
	int numBytesToWrite = numBitsToWrite / 8;
	int shiftLeft = 8-numOldBits-(numBits%8);
	int shiftRight = 8-shiftLeft;
	int msbByte = 0;
	if(numBits%8 == 0)
	    msbByte = 4 - (numBits / 8);
	else
	    msbByte = 3 - (numBits / 8);

	//when writing the bytes we start at the left over  bits if any and prefix it to
	//the MSB of the current byte array and shift bits all the way to the LSB writing as we go.
	//This first byte made of current and leftover is called the prefix byte.
	//left over bits are prepared for next time.

	//check there left over bits
	if(numOldBits != 0){
	    int numPrefixBits = numOldBits + (numBits%8);
	    if(numBits%8 == 0)
		numPrefixBits = numOldBits + 8;
	    byte [] prefix = new byte[1];
	    //if there are left over bits buil the prefix byte
	    //3 cases
	    if(numPrefixBits >= 8){
		//we take part of the MSB to make the prefix, after that we can treat it as if there 
		//were no leftovers
		prefix[0] = (byte)((leftOver[0] | ((temp[msbByte] & 0xff) >> shiftRight%8)));
		write(prefix[0]);
		//if there were more than 8 bits in the prefix bits then there all stil more bits
		//in the msb Byte to be written else we can look at the next byte 
		if(numPrefixBits == 8)
		    msbByte++;
		if(shiftLeft < 0){
		    shiftLeft = 8-(shiftLeft * -1);
		    shiftRight = 8-shiftLeft;
		}
	    
	    }
	    else if(numPrefixBits < 8){
	    //we need to build the prefix byte out of 3 bytes: left over, MSB byte, MSB-1 byte
		prefix[0] = (byte)(leftOver[0] | (temp[msbByte]<< shiftLeft) | ((temp[msbByte+1] & 0xff) >> shiftRight));
		write(prefix[0]);
		if(numBits%8 != 0)
		    msbByte++;
	    }
	}

	//now that the prefix Byte has (not needed to have been)/been written, we iterate over the rest of the array
	//if there are going to be leftovers
	if(numBitsToWrite%8 != 0){
	    byte [] b = new byte[1];
	    while((msbByte + 1) <= 3){
		b[0] = (byte)((temp[msbByte] << shiftLeft) | ((temp[msbByte+1] & 0xff) >> shiftRight));
		write(b[0]);
		msbByte++;
	    }	
	    //prepare leftOver
	    leftOver[0] = (byte)(temp[3] << shiftLeft);
	    numOldBits = numBitsToWrite % 8;
	}
	//else write bytes straight out of the array
	else{
	    for(int i=0; msbByte < 4; msbByte++)
		write(temp[msbByte]);

	    numOldBits = 0;
	    leftOver[0] = (byte)0;
	}	
    }



    /**
     *outputs a byte to file
     *@param b a byte to be output to file
     */
    private void write(byte b){
	try{
	    byte [] b1 = new byte[1];
	    b1[0] = b;
	    os.write(b1[0]);
	}catch(Exception e){
	    System.exit(1);
	}
    }



    /**
     *This method was used during debugging
     *it outputs String represtations of codes to a log file logCompress.txt
     *currently there are no calls to it.
     *@param n the integer to output as a string
     */
    private void writeLog(int n){
	try{
	    String s = Integer.toString(n);
	    while(s.length() < 8)
		s = 0 + s;
	    ls.write(s.getBytes());
	    byte [] b = new byte[1];
	    b[0] = 10;
	    ls.write(b);
	}catch(Exception e){
	    System.exit(1);
	}
    }




}
















