package lzw;


//////////////////////////////////////////////////////////////////
//                  Lzw Trie class                              //
//               ---------------------                          //
//               Name:    Daniel Moore                          //
//               ID:      9865293                               //
//                                                              //
////////////////////////////////////////////////////////////////// 
//written with Emacs


/**
 *This class is used for trie nodes in a lexical search tree data structure
 *@author Daniel Moore
 *@version 1.0;
 */
public class Trie{

    //MEMBERS
    private Trie [] node = null;
    private int size = 256;
    private String phrase = null;
    private int key = -1;  //root keeps this sentinal value

    //CONSTRUCTORS

    /**
     *constructor
     *If root node,  this will initialise a full array of nodes
     *@param isRoot boolean inidicating node is root
     */
    public Trie(boolean isRoot){
	if(isRoot){
	    //initialise root node here
	    this.node = new Trie[this.size];
	    for(int i=0; i<size; i++){
		this.addNode(i, i);
	    }
	}
    }


    /**
     *Used to create a new leaf node
     *@param key the key value for this node
     *@param phr the new phrase for this node
     */
    public Trie(int key, String phr){
	this.key = key;
	this.phrase = phr;
    }


    ////METHODS

    /**
     *returns the key value for this node
     *@return integer key value
     */
    public int getKey(){
	return this.key;
    }


    /**
     *returns the phrase for this node
     *@return String phrase
     */
    public String getPhrase(){
	return this.phrase;
    }


    /**
     *returns the node at a given index or null if empty
     *@param index the index to look up
     *@return the Trie node or null at index
     */
    public Trie getNode(int index){
	if(this.node == null)return null;

	if(index < size && index >= 0) 
	    return this.node[index];
	return null;
    }

    
    /**
     *lets you know whether the phrase pointer is null
     *@return boolean inidicating 'is null'
     */
    public boolean phraseIsNull(){
	if(this.phrase == null)
	    return true;
	return false;
    }


    /**
     *adds a new node to Trie
     *@param index the index to add the node
     *@param key the key value for the new node
     */
    public void addNode(int index, int key){

	//set up new phrase
	byte [] b = new byte[1];
	b[0] = new Integer(index).byteValue();
		
	String s = null;
	if(this.key == -1)
	    s = new String(b);
	else
	    s = this.phrase + new String(b);
	
	//add new node
	//wait until we need to , to initialise the node Trie array
	if(this.node == null)
	    this.node = new Trie[this.size];
	this.node[index] = new Trie(key, s);
    }


} 
