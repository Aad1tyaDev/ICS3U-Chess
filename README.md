# ♟️ICS3U-Chess♟️
Fully-playable chess game written in Java (AWT + Applet) as an independent ICS3U-style project. Includes legal move validation, check/checkmate logic, castling, en-passant, promotion, and undo support.
ors)






♟️ Java Chess Game (ICS3U Project – Independent Skill Development)

This repository contains a full chess game implemented in Java 1.4.2 using AWT + Applet, originally designed for the Ready to Program environment used in ICS3U (Grade 11 Computer Science).

Although I’m no longer enrolled in ICS3U, I chose to complete class-style projects independently to show initiative, strengthen OOP fundamentals, build problem-solving skills, and demonstrate programming growth.

This project demonstrates experience with:

•Low-level graphics

•Event-driven programming

•Mouse interaction

•Board-game logic

•Move validation and game rules

•Data structures (Vector, arrays, etc.)

•State tracking and undo systems

**All written in pure Java, no external libraries, inside a single runnable applet.**











**🚀 Features**

✔️ Fully playable chess for two human players

✔️ Legal move enforcement for all chess pieces

✔️ Game-state tracking:

•Turn order

•Check detection

•Checkmate and stalemate


✔️ Special rules implemented:

•Castling (both sides, both colours)

•Pawn promotion

•En-passant


✔️ Move-undo system using a history stack (Vector)

✔️ On-screen board drawing with selectable highlight indicators

✔️ Button controls:

•New Game

•Undo Move

•Reset Board



**🛠 Tech Used**


Component	Description

Language	Java (1.4.2 compatible)

UI	AWT Graphics + Applet

Input	MouseListener + ActionListener

Structures	char[][] board, Vector move history

Rendering	Manual board/piece drawing

**This runs under Ready to Program, JDK 1.4–8, or any Applet viewer.**



**📦 How the Game Works**

Board Representation

•char[8][8] board

•Uppercase = White pieces

•Lowercase = Black pieces

•. = empty square

Move System

•A custom inner class Move stores:

•From/To coordinates

•Captured piece

•En-passant flag

•Promotion flag

•Rook-movement data (for castling)

•Previous board state (for undo)

•Rule Checking Includes

•In-check calculation

•Square-attack scanning

•Move simulation + undo to verify legality




**🎯 Why This Project Matters**

This project reflects essential computer science fundamentals typically covered in ICS3U and beyond.

✔️ Thinking & Problem-Solving

•Chess logic

•Movement algorithms

•Conditional reasoning



✔️ Algorithmic Design

•Directional search (rook/bishop/queen vectors)

•Boundary validation

•Threat evaluation



✔️ Software Design

•Game-state management

•Undo/rollback logic

•Modular rule enforcement


✔️ Independent Learning

**Completed outside of class to prepare for ICS4U and post-secondary programming.**



**🖥️ Running the Program**

**Install Java 8 or lower (for Applet support).**

Compile:

javac ChessApplet.java



Run using:

Ready to Program, or

An Applet viewer:

appletviewer ChessApplet.java


**Modern Java removed Applet support, but it still runs fine in legacy environments.**




**🧩 Future Improvements**

Planned upgrades:

•Real chess piece images

•PGN output

•Move-highlighting by threat level

•**Basic AI opponent (minimax)**

**Stretch goals:**

•Network multiplayer

•Rewrite using Swing (Applet-free modern UI)



🏅 Skills Demonstrated

•Java OOP fundamentals

•Event-driven programming

•Manual 2D rendering

•Game-loop design

•Independent project execution

•ICS3U curriculum expectations and beyond



Independent CS learner • ICS3U skill showcase • Game development hobbyist

📝 License
MIT License — free to use, modify, and build upon.
