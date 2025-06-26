## LZW Compression (java)


## Overview
This project is a Java implementation of the LZW compression algorithm, developed as a university assignment in 2004. Built without access to modern tools like AI or Stack Overflow, it demonstrates a from-scratch approach to compression and decompression using a `Trie` data structure and optimized bit-packing. The project serves as an educational reference for understanding LZW and dictionary-based compression techniques.

The implementation supports compressing `.txt` files into `.lzw` format and decompressing them back, with a focus on variable-length code handling and efficient dictionary management. A debugging tool, `logTester.java`, verifies compression/decompression consistency.

## Purpose
This project was created to deepen my understanding of data compression and data structures during my university studies. It showcases problem-solving, algorithm implementation, and bit manipulation in Java, making it a valuable portfolio piece for demonstrating foundational coding skills.

## Features
- **LZW Compression/Decompression**: Implements the LZW algorithm for text files, supporting variable-length codes (10-32 bits).
- **Trie-Based Dictionary**: Uses a `Trie` for efficient prefix matching in compression, with a `String` array fallback in decompression for faster lookups.
- **Bit Packing**: Handles variable-length code output with optimized bit manipulation.
- **Debugging Support**: Includes `logTester.java` to compare compression/decompression logs for debugging.
- **Error Handling**: Validates input files and handles I/O errors gracefully.
- **Documentation**: Comprehensive Javadoc and in-code comments explain complex logic.

## Historical Context
Developed in 2004 as a university assignment, this project was built using only provided pseudocode, textbooks, and Emacs. Without access to modern resources like Stack Overflow or AI tools, it reflects independent problem-solving and a deep dive into LZW's mechanics.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 1.4 or higher (compatible with 2004 standards)
- Basic understanding of Java and compression algorithms

### Installation
- Clone the repository:
   ```bash
   git clone https://github.com/yourusername/lzw-compression.git
- Navigate to the project directory:cd lzw-compression
- Compile the Java files:javac lzw/*.java



## Usage

Run the program with a .txt file to compress or a .lzw file to decompress:java lzw.Lzw input.txt
Compresses input.txt to input.lzw.
Decompresses input.lzw to input2.txt.
To debug, generate log files by uncommenting ls initialization in Compress.java and Decompress.java, then run:java lzw.logTester
Compares logCompress.txt and logDecompress.txt for mismatches.

## Project Structure

Lzw.java: Entry point, validates input and routes to compression/decompression.
Compress.java: Implements LZW compression using a Trie dictionary.
Decompress.java: Implements LZW decompression with Trie and String array dictionaries.
Trie.java: Defines the Trie data structure for dictionary operations.
logTester.java: Debugging tool to compare compression/decompression logs.

## Limitations

Performance: The Trie in decompression is slow for large dictionaries; mitigated with a String array but could be further optimized.
Memory: The String array in Decompress.java may use excessive memory for large files.
Testing: Limited to log comparison; lacks automated unit tests.
File Formats: Only supports .txt input and .lzw output.
Error Messages: Minimal feedback for invalid inputs or errors.

## Potential Improvements

Add unit tests with JUnit for Trie and compression/decompression correctness.
Optimize Trie lookups with a hash-based structure.
Simplify bit-packing logic with a bit buffer.
Use modern Java features (e.g., try-with-resources) for file I/O.
Add a build system (e.g., Maven) for easier compilation.

## Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a feature branch (git checkout -b feature/YourFeature).
Commit changes (git commit -m 'Add YourFeature').
Push to the branch (git push origin feature/YourFeature).
Open a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
Acknowledgments

Developed in 2004 at Waikato University. 
Inspired by the LZW algorithm and its applications in data compression.
Thanks to my professor for providing clear pseudocode to guide this implementation.

∞ Daniel Kereama ∞

