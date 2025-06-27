# LZW Compression (Java)

A from-scratch Java implementation of the LZW compression algorithm, originally developed as a university assignment in 2004. This project showcases foundational understanding of compression, data structures, and low-level bit manipulation—written without modern conveniences like Stack Overflow or AI.

---

## 📘 Overview

This project demonstrates how to compress and decompress `.txt` files using **Lempel–Ziv–Welch (LZW)**. The implementation features a `Trie`-based dictionary for encoding and optimized bit-packing for output size efficiency.

The code supports:
- Compressing `.txt` files into `.lzw`
- Decompressing `.lzw` files back to `.txt`
- Debugging with a custom log comparison tool (`logTester.java`)

---

## 🎯 Purpose

Created to deepen understanding of:
- Algorithmic problem solving
- Bit-level data manipulation
- Data compression techniques
- Java fundamentals under constrained tooling

The project reflects independent design, coding, and debugging—valuable for showcasing problem-solving discipline in your portfolio.

---

## ✨ Features

- **Full LZW implementation** for `.txt` ↔ `.lzw` file formats
- **Variable-length codes** (10–32 bits) for compression
- **Trie-based dictionary** for fast prefix matching during compression
- **Fallback string array** in decompression for performance
- **Bit-packing logic** with efficient memory use
- **Error handling** for invalid input and I/O failures
- **Debugging tools** (`logTester`) for log comparison
- **Thorough inline comments** and Javadoc

---

## 🕰️ Historical Context

- Developed in 2004 at the University of Waikato
- Built using **Emacs**, pseudocode, and textbooks
- Written before Stack Overflow or GitHub Copilot existed
- A true reflection of manual problem-solving and algorithmic understanding

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 1.4 or higher (backward-compatible with original source)
- Basic Java knowledge

### Installation

1. Clone the repository  
   `git clone https://github.com/yourusername/lzw-compression.git`

2. Navigate to the project directory  
   `cd lzw-compression`

3. Compile the source files  
   `javac lzw/*.java`

---

## ⚙️ Usage

To **compress** a file:  
`java lzw.Lzw input.txt`  
→ Outputs: `input.lzw`

To **decompress** a file:  
`java lzw.Lzw input.lzw`  
→ Outputs: `input2.txt`

To **debug** using log files:  
1. Uncomment the `ls` log initialization in `Compress.java` and `Decompress.java`  
2. Run: `java lzw.logTester`  
→ Compares `logCompress.txt` and `logDecompress.txt`

---

## 📁 Project Structure

| File              | Description                                              |
|-------------------|----------------------------------------------------------|
| `Lzw.java`        | Main entry point; determines compress or decompress mode |
| `Compress.java`   | Core LZW compression logic with Trie dictionary          |
| `Decompress.java` | Decompression logic using Trie and String array fallback |
| `Trie.java`       | Custom Trie implementation for dictionary management     |
| `logTester.java`  | Debugging tool to compare logs from compress/decompress  |

---

## ⚠️ Limitations

- **Performance**: Trie during decompression can be slow for large files  
- **Memory Use**: String arrays may consume excessive memory  
- **Testing**: No unit tests, manual log comparison only  
- **Input Format**: Only supports `.txt` and `.lzw`  
- **Error Feedback**: Minimal messages for invalid input

---

## 🔧 Potential Improvements

- Add JUnit-based unit tests
- Replace Trie with hash-based structure for faster lookups
- Refactor bit-packing logic into reusable class
- Upgrade I/O with modern Java features (e.g. `try-with-resources`)
- Add Maven/Gradle support for cleaner build management

---

## 🤝 Contributing

Contributions are welcome!  
1. Fork the repo  
2. Create a new branch  
   `git checkout -b feature/YourFeature`  
3. Commit and push changes  
4. Open a pull request 🚀

---

## 📄 License

This project is licensed under the [MIT License](./LICENSE).

---

## 🙏 Acknowledgments

- Developed at the University of Waikato (2004)  
- Inspired by the classic LZW algorithm  
- Thanks to my professor for providing foundational guidance and pseudocode

---

∞ Daniel Kereama ∞
