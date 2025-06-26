package lzw;


import java.io.*;

//////////////////////////////////////////////////////////////////
//               Lzw Decompression class                        //
//               ---------------------                          //
//               Name:    Daniel Moore                          //
//               ID:      9865293                               //
//    Features:     Optimized variable length bit packing       //
//                                                              //
////////////////////////////////////////////////////////////////// 
//written with Emacs



/**
 *This class performs Lzw decompression on an lzw compressed file
 *@author Daniel Moore
 *@version 6.0
 */
public class Decompress{

   


    //MEMBERS
    private File infile = null;
    private File outfile = null;
    private File logfile = new File("logDecompress.txt");
    private InputStream is = null;
    private OutputStream os = null;
    private OutputStream ls = null;

    private Trie root = null;
    private int keyCounter = 256;
    private String [] table = null;
    private int eof = 0;
    private int len = 0;

    private int bitLimit = 512;
    private byte [] leftOver = new byte[1];;
    private byte [] buffer = null;
    private int numBits = 10;
    private int numOldBits = 0;

    private int readCounter = keyCounter;
    //    private byte [] watcher = new byte[10000];
    //private int ct = 0;

    //CONSTRUCTORS

    /**
     *Takes file and initialises Trie and other members 
     *This Decompressor uses 2 alternate data Structures to work
     *the first one was a trie but code lookups were very slow when the trie got large
     *so I implemented a string array dictionary that uses indexing as code reference
     *@param infile The file to read from
     */
    public Decompress(File infile){

	this.infile = infile;
	this.outfile = new File(infile.getName().substring(0, (infile.getName().length()-4)) + "2.txt");
	this.root = new Trie(true);

	//dictionary
	int k = new Long(infile.length()).intValue();	
	this.table = new String[(k*2)+256];
	//initialise dictionary
	byte [] b = null;
	
	for(int i=0; i<256; i++){
	    b = new byte[1];
	    b[0] = new Integer(i).byteValue();
	    table[i] = new String(b);
	}

	leftOver[0] = (byte)0;

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
     *run() performs the lzw algorithm
     */
    public void run(){

	int cW, pW = 0;
	String P, C, T = null;
	
	try{
	
	    //1. Dictionary contains all possible roots'
	    
	    //2. cW := first code in codestream (it denotes a root)
	    cW = getCode();
	    //3. output the string.cW to the charstream
	    output(decode(cW));
	    
	    while(eof >= 0){
		//4. pW := cW
		pW = cW;
		//5. cW := next code word in the codestream
		cW = getCode();
		//6. Is the string.cW present in the dictionary?
		if((C = decode(cW)) != null){
		    //   if it is....
		    //i. output the String.cW to the charStream
		    output(C);
		    //ii. P:= string.pW
		    P = decode(pW);
		    //iii. C:= the first char of the string.cW
		    C = C.charAt(0) + ""; 
		    //iv. add the string P+C to the dictionary
		    add(P+C);
		}
		else{
		    //   if not ....
		    //i. P:= string.pW
		    P = decode(pW);
		    //ii. C:= the first char of string.pW
		    C = P.charAt(0) + "";
		    //iii. output the string P+C to the charStream 
		    output(P+C);
		    //iv. add string P+C to dictionary
		    add(P+C);
		}
	//7.Are there more codes in the codestream?
	   
	//if not .. END
		
	//if so .. go back to step 4
	}

	
	}catch(Exception e){
	    System.out.println("keyCounter: " + keyCounter);
	    System.out.println("readCounter: " + readCounter);
	    System.out.println("bitLimit: " + bitLimit);
	    System.out.println("numBits: " + numBits);
	}
    }




    /**
     *This is the class that deals with bit twiddling
     *It reads in bytes and constructs codes depending on the number of bits a code should have 
     *and the number of bits left over from the previous processing cycle. If there are left overs
     *they will be saved for the next time arround
     */    
    private int getCode(){
	
	//check if we need to increase the number of bits
	readCounter++;
	if(readCounter == bitLimit-1){
	    bitLimit *= 2;
	    numBits++;
	}
	
	//initialise variables
	int code = 0;
	int numBitsToRead = numBits - numOldBits;
	int numBytesToRead = 0;
	int numBytesToWrite = 0;

	if(numBitsToRead % 8 == 0)
	    numBytesToRead = numBitsToRead / 8;
	else
	    numBytesToRead = (numBitsToRead / 8)+1;

	if(numBits % 8 == 0)
	    numBytesToWrite = numBits / 8; 
	else
	    numBytesToWrite = (numBits / 8)+1; 

	int shiftLeft = (numBits -numOldBits)%8;
	int shiftRight = 8-shiftLeft;
	buffer = new byte[numBytesToRead];        //this holds the bytes read in from file
	byte [] temp = new byte[numBytesToWrite]; //this holds the bytes that will be written

	//read in bits- eof will be -1 if we are at EOF 
	try{
	    eof = is.read(buffer);
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}

	//for(int i=0; i<buffer.length; i++, ct++)
	    //	    watcher[ct] = buffer[i];

	//Im using these (r, k) because they are shorter to write 
	int r = numBytesToRead; 
	int k = numBytesToWrite;
	int i = 0;

	//are there are only full bytes to write?
	if(shiftLeft == 0){
	    //first check if there are old bits to write
	    if(numOldBits != 0){
		temp[0] = leftOver[0];
		for(int z=1; z<numBytesToWrite; z++)
		    temp[z] = buffer[z-1];
	    }
	    //if there are no old bits to write, we can grab bytes straight out of the array
	    else
		for(int z=0; z<numBytesToWrite; z++)
		    temp[z] = buffer[z];

	    //reset left over variables
	    leftOver[0] = (byte)0;
	    numOldBits = 0;
	}

	//this means that we need to shift bytes around to make new bytes for output
	else{
	
	    //cycle through bytes only in buffer
	    //ie. buffer[n] | [buffer[n-1]
	    while((r-2-i >= 0) && (k-1-i < numBytesToWrite )){
		temp[k-1-i] = (byte)(((buffer[r-1-i] & 0xff) >> shiftRight) | (buffer[r-2-i] << shiftLeft));
		i++;
	    }
	    
	    //if there are still two more bytes to write they are made of the following:
	    //1. buffer[0] | leftOver[0]
	    //2. leftOver[0] | 0
	    if(k-i == 2){
		temp[1] = (byte)(((buffer[0] & 0xff) >> shiftRight) | (leftOver[0] << shiftLeft));
		temp[0] = (byte)(((leftOver[0] & 0xff) >> shiftRight) | (byte)0); 
	    }
	    
	    //else there is only one more byte made of this:
	    //1. buffer[0] | leftOver[0]
	    else
		temp[0] = (byte)(((buffer[0]& 0xff) >> shiftRight) | (leftOver[0] << shiftLeft));
	    
		    
	    //calculate number of bits left over for next code
	    numOldBits = 8 - ((8-numOldBits+numBits)%8);
	    //prepare leftOver byte by removing unwanted bits from byte ie. shift left then back right
	    leftOver[0] = (byte)((buffer[r-1] <<(8-numOldBits))) ;
	    leftOver[0] = (byte)(((leftOver[0] & 0xff) >> (8-numOldBits)) & 0xff);
	}
				      	      
       code = buildInt(temp);
       return code;  
    }


    /**
     *log method I was using for debugging
     *it outputs String representations of the codes, each on their own line
     *I also wrote a class to test the file this produces - logTester.java
     *@param n the integer to output
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



    /**
     *writes a dictionary phrase to file
     *@param s the String to output
     */
    private void output(String s){
	try{
	    os.write(s.getBytes());
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    ////////////////////////
    ///  Table methods   ///
    ////////////////////////
    //These 2 methods use a String array as the dictionary 
    //It is much faster than the Trie but may not be as efficient with memory

    /**
     *Adds a phrase to the dictionary 
     *@param s the phrase to add
     */
    private void add(String s){
	table[keyCounter] = s;
	keyCounter++;
    }

    /**
     *returns the phrase from the dictionary at a given index
     *@param n the index in the array
     *@return decode the corrosponding string to n
     */
    private String decode(int n){
	return this.table[n];
    }


    //////////////////////////////
    ////    Trie methods      ////
    //////////////////////////////
    //These 2 methods use a Trie as a dictionary
    

    /**
     *This is the add method to use when using the Trie as a dictionary
     *@param P the phrase currently in the dictionary
     *@param C the post-fix to the new phrase to be added to the dictionary
     */
    
    private void add(String P, String C){
	
	//check if we are at maximum number of bits
	//if so, dont add any more phrases
	if(numBits > 32)
	    return;

	Trie temp = root;
	for(int i=0; i<P.length(); i++)
	    temp = temp.getNode(P.charAt(i)*1);
	
 	temp.addNode(C.charAt(0)*1, keyCounter);
	keyCounter++;
    }
    

    /**
     *Recursive method that finds a phrase given a code
     *This is the method that is really slow
     *@param node the current node being proccessed
     *@param n the key value we are searching for
     */
    private String decode(Trie node, int n){

	String s = null;
	Trie temp = node;
	
	if(node == null)return null;            //base case
	if(node.getKey() > n)return null;       //we can stop searching child nodes of this node early 
	if(node.getKey() == n)return node.getPhrase(); //match
	//else recurse
	for(int i=0; i<256; i++){
	    temp = node.getNode(i);
	    if(temp != null){
		s = decode(temp, n);
		if(s != null)
		    return s;
	    }
	}
	return null;
    }
    /////////////////////////

    

    /**
     *This method builds an int type from a byte array of indefinite length
     *note. Since all bytes are signed in java we need to AND it with 0xff 
     *@param b the byte array to build the int from
     *@return an unsigned integer
     */
    private int buildInt(byte [] b){
	int num = 0;
	byte [] t = new byte[4];

	//I used a case by case solution here because I was confusing myself with the general 
	//iterative solution when I was debugging
	if(b.length == 1){
	    t[0] = 0;
	    t[1] = 0;
	    t[2] = 0;
	    t[3] = b[0];
	}
	else if(b.length == 2){
	    t[0] = 0;
	    t[1] = 0;
	    t[2] = b[0];
	    t[3] = b[1];
	}
	else if(b.length == 3){
	    t[0] = 0;
	    t[1] = b[0];
	    t[2] = b[1];
	    t[3] = b[2];
	}
	else if(b.length == 4){
	    t[0] = b[0];
	    t[1] = b[1];
	    t[2] = b[2];
	    t[3] = b[3];
	}

	//calculate int total
	for(int i=0; i<4; i++){
	    num = num << 8;
	    num = num + (t[i] & 0xff);
	}

	return num;
    } 



}
