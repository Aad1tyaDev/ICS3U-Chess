# ♟️ Java Chess (ICS3U Independent Project)

A fully playable two-player chess game written in Java using AWT and the Applet API. Built independently outside of class as a personal challenge and skill showcase, targeting the **Ready to Program** environment used in ICS3U (Grade 11 Computer Science).

---

## Features

- Legal move enforcement for all six piece types
- Check and checkmate detection
- Stalemate detection
- **Special rules:** castling (both sides, both colours), en passant, pawn promotion
- Undo system backed by a move history stack
- Click-to-select piece highlighting
- On-screen status messages for turn tracking and game state
- Three button controls: **New Game**, **Undo**, **Reset Board**
- Everything in a single `.java` file with zero external dependencies

---

## Tech Stack

| Component | Details |
|-----------|---------|
| Language | Java (1.4.2 compatible) |
| UI | AWT Graphics + Applet |
| Input | `MouseListener` + `ActionListener` |
| Board | `char[8][8]` (uppercase = White, lowercase = Black) |
| Move History | `Vector` of `Move` objects |
| Rendering | Manual 2D board and piece drawing |

---

## How It Works

**Board representation:** An 8x8 `char` array where uppercase letters are White pieces and lowercase are Black. Empty squares are stored as `.`.

**Move system:** A custom `Move` inner class stores everything needed for a move and its undo: source/destination coordinates, captured piece, en passant flag, promotion flag, rook movement data for castling, and the previous board state.

**Legality checking:** Before any move is committed, the engine simulates it and verifies the moving player's king is not left in check. This covers standard moves, castling paths, and en passant edge cases.

---

## Running the Project

> Applet support was removed in Java 9+, so you need Java 8 or lower.

**1. Compile:**
```bash
javac ChessApplet.java
```

**2. Run with an applet viewer:**
```bash
appletviewer ChessApplet.html
```

Or open it directly in **Ready to Program**.

---

## Planned Improvements

- Real chess piece images instead of text characters
- Move highlighting by threat level
- Basic AI opponent using minimax
- PGN (Portable Game Notation) export
- Swing rewrite to drop the legacy Applet dependency
- Network multiplayer (stretch goal)

---

## Why I Built This

I dropped ICS3U mid-semester to make room for another course, but still wanted to complete the core projects independently. Chess is one of the more complex game-logic challenges at this level, involving recursive state simulation, directional search algorithms, and careful edge case handling. Building it without any class guidance was a good test of everything I'd been learning on my own.

---

## License

MIT License. Free to use, modify, and build on.
