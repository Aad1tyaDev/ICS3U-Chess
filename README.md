# ICS3U-Chess
Fully-playable chess game written in Java (AWT + Applet) as an independent ICS3U-style project. Includes legal move validation, check/checkmate logic, castling, en-passant, promotion, and undo support.
♟️ Java Chess Game (ICS3U Project – Independent Skill Development)

This repository contains a full chess game implemented in Java 1.4.2 using AWT + Applet, originally designed for the Ready to Program environment used in ICS3U (Grade 11 Computer Science).

Although I’m no longer enrolled in ICS3U, I chose to complete class-style projects independently to show initiative, strengthen OOP fundamentals, build problem-solving skills, and demonstrate my programming growth.

This project proves I can work with:

low-level graphics

event-driven programming

mouse interaction

board-game logic

move validation and game rules

data structures (Vector, arrays, etc.)

state tracking and undo systems

All written in pure Java, no external libraries, inside a single runnable applet.

🚀 Features

✔️ Fully playable chess for two human players
✔️ Legal move enforcement for all pieces
✔️ Game-state tracking:

turn order

check detection

checkmate and stalemate
✔️ Special rules implemented:

Castling (both sides, both colours)

Pawn promotion

En-passant
✔️ Move-undo system using history stack (Vector)
✔️ On-screen board drawing with highlight indicators
✔️ Button controls:

New Game

Undo Move

Reset Board

🛠 Tech Used
Component	Description
Language	Java (1.4.2 compatible)
UI	AWT Graphics + Applet
Input	MouseListener + ActionListener
Data Structures	char[][] board, Vector move history
Rendering	Manual board/piece drawing

This runs fine under Ready to Program, JDK 1.4–8, or an Applet runner.

📦 How the Game Works (Overview)
Board Representation

The board is stored as:

char[8][8] board


Uppercase = White pieces
Lowercase = Black pieces
. = empty square

Move System

A custom inner class Move stores:

from / to coordinates

captured piece

en passant flag

promotion flag

rook movement data (for castling)

previous game state for undo

Rule-checking Includes:

In-check calculation

Square-attack scanning

Move simulation + undo to verify legality

🎯 Why This Project Matters

This game demonstrates computer science fundamentals expected in ICS3U and beyond:

✔ Thinking/Problem Solving

Chess logic

Movement algorithms

Conditional reasoning

✔ Algorithmic Design

Search patterns (rook/bishop/queen vectors)

Boundary testing

Attack checking

✔ Software Design

State management

Undo/rollback systems

Modular rule enforcement

✔ Independent Learning

I completed this without classroom support to challenge myself beyond the course and prepare for ICS4U / post-secondary CS.

🖥️ Running the Program

Install Java 8 or lower (for Applet support)

Compile:

javac ChessApplet.java


Run through:

Ready to Program, or

Any Applet viewer such as:

appletviewer ChessApplet.java


Note: Modern Java removed Applet support, but it still runs in legacy tools.
