import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * Full chess game implemented as an applet for Ready to Program (Java 1.4.2).
 * Everything is contained in this single file and uses only AWT drawing.
 */
public class ChessApplet extends Applet implements MouseListener, ActionListener {
    char[][] board = new char[8][8];
    boolean whiteToMove = true;
    int selectedRow = -1, selectedCol = -1;
    Vector moveHistory = new Vector();
    int enPassantRow = -1, enPassantCol = -1;
    boolean whiteKingMoved = false, blackKingMoved = false;
    boolean whiteRookAMoved = false, whiteRookHMoved = false;
    boolean blackRookAMoved = false, blackRookHMoved = false;
    Button newGameBtn, undoBtn, resetBtn;
    String statusMessage = "";
    Font pieceFont = new Font("Arial", Font.BOLD, 32);

    public void init() {
	setSize(520, 520);
	setLayout(new BorderLayout());
	Panel top = new Panel();
	newGameBtn = new Button("New Game");
	undoBtn = new Button("Undo");
	resetBtn = new Button("Reset Board");
	top.add(newGameBtn);
	top.add(undoBtn);
	top.add(resetBtn);
	add(top, BorderLayout.NORTH);
	addMouseListener(this);
	newGameBtn.addActionListener(this);
	undoBtn.addActionListener(this);
	resetBtn.addActionListener(this);
	initBoard();
    }

    /* Set up the starting chess position. */
    void initBoard() {
	int r, c;
	for (r = 0; r < 8; r++) {
	    for (c = 0; c < 8; c++) {
		board[r][c] = '.';
	    }
	}
	String back = "rnbqkbnr";
	for (c = 0; c < 8; c++) {
	    board[0][c] = back.charAt(c);
	    board[1][c] = 'p';
	    board[6][c] = 'P';
	    board[7][c] = Character.toUpperCase(back.charAt(c));
	}
	whiteToMove = true;
	selectedRow = -1;
	selectedCol = -1;
	moveHistory.removeAllElements();
	enPassantRow = -1;
	enPassantCol = -1;
	whiteKingMoved = false;
	blackKingMoved = false;
	whiteRookAMoved = false;
	whiteRookHMoved = false;
	blackRookAMoved = false;
	blackRookHMoved = false;
	statusMessage = "New game started.";
	repaint();
    }

    public void paint(Graphics g) {
	drawBoard(g);
	drawStatus(g);
    }

    void drawBoard(Graphics g) {
	int size = 50;
	int offsetX = 20;
	int offsetY = 60;
	int r, c;
	g.setFont(pieceFont);
	for (r = 0; r < 8; r++) {
	    for (c = 0; c < 8; c++) {
		boolean light = (r + c) % 2 == 0;
		if (light) {
		    g.setColor(new Color(240, 217, 181));
		} else {
		    g.setColor(new Color(181, 136, 99));
		}
		int x = offsetX + c * size;
		int y = offsetY + r * size;
		g.fillRect(x, y, size, size);

		if (r == selectedRow && c == selectedCol) {
		    g.setColor(new Color(0, 255, 0, 120));
		    g.fillRect(x, y, size, size);
		}
		Vector moves = null;
		if (selectedRow != -1 && selectedCol != -1) {
		    moves = getLegalMoves(selectedRow, selectedCol);
		}
		if (moves != null) {
		    for (int k = 0; k < moves.size(); k++) {
			Move m = (Move) moves.elementAt(k);
			if (m.toR == r && m.toC == c) {
			    g.setColor(new Color(0, 0, 255, 120));
			    g.fillRect(x, y, size, size);
			}
		    }
		}

		char p = board[r][c];
		if (p != '.') {
		    drawPiece(g, p, x, y, size);
		}
	    }
	}
    }

    void drawStatus(Graphics g) {
	g.setColor(Color.BLACK);
	g.setFont(new Font("SansSerif", Font.PLAIN, 14));
	int y = 470;
	g.drawString("Turn: " + (whiteToMove ? "White" : "Black"), 20, y);
	boolean whiteCheck = isInCheck(true);
	boolean blackCheck = isInCheck(false);
	if (whiteCheck) {
	    g.drawString("White is in check", 120, y);
	}
	if (blackCheck) {
	    g.drawString("Black is in check", 270, y);
	}
	if (isCheckmate(whiteToMove)) {
	    g.drawString("Checkmate! " + (whiteToMove ? "Black" : "White") + " wins.", 20, y + 20);
	} else if (isStalemate(whiteToMove)) {
	    g.drawString("Stalemate!", 20, y + 20);
	}
	g.drawString(statusMessage, 20, y + 40);
    }

    boolean isWhitePiece(char p) {
	return p >= 'A' && p <= 'Z';
    }

    boolean isBlackPiece(char p) {
	return p >= 'a' && p <= 'z';
    }

    /* Convert a board character into the letter we draw on the board. */
    String pieceToLabel(char p) {
	switch (Character.toUpperCase(p)) {
	    case 'K': return "K";
	    case 'Q': return "Q";
	    case 'R': return "R";
	    case 'B': return "B";
	    case 'N': return "N";
	    case 'P': return "P";
	    default: return "";
	}
    }

    /* Determine the primary fill color for a piece. */
    Color getPieceFillColor(char p) {
	if (isWhitePiece(p)) {
	    return Color.WHITE;
	}
	return Color.BLACK;
    }

    /* Determine the outline color to keep text readable. */
    Color getPieceOutlineColor(char p) {
	if (isWhitePiece(p)) {
	    return Color.BLACK;
	}
	return new Color(245, 245, 245);
    }

    /* Draw a piece letter centered in its square with an outline for readability. */
    void drawPiece(Graphics g, char p, int x, int y, int size) {
	String label = pieceToLabel(p);
	g.setFont(pieceFont);
	FontMetrics fm = g.getFontMetrics(pieceFont);
	int textWidth = fm.stringWidth(label);
	int textHeight = fm.getAscent() + fm.getDescent();
	int drawX = x + (size - textWidth) / 2;
	int drawY = y + (size - textHeight) / 2 + fm.getAscent();
	Color outline = getPieceOutlineColor(p);
	Color fill = getPieceFillColor(p);
	// Outline
	g.setColor(outline);
	g.drawString(label, drawX + 1, drawY + 1);
	// Fill
	g.setColor(fill);
	g.drawString(label, drawX, drawY);
    }

    boolean inBounds(int r, int c) {
	return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    /* Determine if a square is attacked by a side. */
    boolean isSquareAttacked(int row, int col, boolean byWhite) {
	int dr, dc, r, c;
	// Pawns
	if (byWhite) {
	    r = row + 1;
	    if (inBounds(r, col - 1) && board[r][col - 1] == 'P') return true;
	    if (inBounds(r, col + 1) && board[r][col + 1] == 'P') return true;
	} else {
	    r = row - 1;
	    if (inBounds(r, col - 1) && board[r][col - 1] == 'p') return true;
	    if (inBounds(r, col + 1) && board[r][col + 1] == 'p') return true;
	}
	// Knights
	int[] nr = { -2, -2, -1, -1, 1, 1, 2, 2 };
	int[] nc = { -1, 1, -2, 2, -2, 2, -1, 1 };
	for (int i = 0; i < 8; i++) {
	    r = row + nr[i];
	    c = col + nc[i];
	    if (inBounds(r, c)) {
		char p = board[r][c];
		if (byWhite && p == 'N') return true;
		if (!byWhite && p == 'n') return true;
	    }
	}
	// Bishops/Queens diagonals
	int[] drs = { -1, -1, 1, 1 };
	int[] dcs = { -1, 1, -1, 1 };
	for (int dir = 0; dir < 4; dir++) {
	    dr = drs[dir];
	    dc = dcs[dir];
	    r = row + dr;
	    c = col + dc;
	    while (inBounds(r, c)) {
		char p = board[r][c];
		if (p != '.') {
		    if (byWhite && (p == 'B' || p == 'Q')) return true;
		    if (!byWhite && (p == 'b' || p == 'q')) return true;
		    break;
		}
		r += dr;
		c += dc;
	    }
	}
	// Rooks/Queens straight
	int[] drs2 = { -1, 1, 0, 0 };
	int[] dcs2 = { 0, 0, -1, 1 };
	for (int dir = 0; dir < 4; dir++) {
	    dr = drs2[dir];
	    dc = dcs2[dir];
	    r = row + dr;
	    c = col + dc;
	    while (inBounds(r, c)) {
		char p = board[r][c];
		if (p != '.') {
		    if (byWhite && (p == 'R' || p == 'Q')) return true;
		    if (!byWhite && (p == 'r' || p == 'q')) return true;
		    break;
		}
		r += dr;
		c += dc;
	    }
	}
	// King
	for (dr = -1; dr <= 1; dr++) {
	    for (dc = -1; dc <= 1; dc++) {
		if (dr == 0 && dc == 0) continue;
		r = row + dr;
		c = col + dc;
		if (inBounds(r, c)) {
		    char p = board[r][c];
		    if (byWhite && p == 'K') return true;
		    if (!byWhite && p == 'k') return true;
		}
	    }
	}
	return false;
    }

    /* Check if a side is currently in check. */
    boolean isInCheck(boolean white) {
	int r, c;
	for (r = 0; r < 8; r++) {
	    for (c = 0; c < 8; c++) {
		if (white && board[r][c] == 'K') {
		    return isSquareAttacked(r, c, false);
		}
		if (!white && board[r][c] == 'k') {
		    return isSquareAttacked(r, c, true);
		}
	    }
	}
	return false;
    }

    /* Generate legal moves for the piece on the square. */
    Vector getLegalMoves(int r, int c) {
	Vector moves = new Vector();
	if (!inBounds(r, c)) return moves;
	char p = board[r][c];
	if (p == '.') return moves;
	boolean whitePiece = isWhitePiece(p);
	if ((whitePiece && !whiteToMove) || (!whitePiece && whiteToMove)) {
	    return moves;
	}
	int dir = whitePiece ? -1 : 1;
	int startRow = whitePiece ? 6 : 1;
	int forwardR = r + dir;
	// Pawns
	if (Character.toUpperCase(p) == 'P') {
	    if (inBounds(forwardR, c) && board[forwardR][c] == '.') {
		addPawnMove(r, c, forwardR, c, moves, false);
		int doubleR = r + dir * 2;
		if (r == startRow && board[doubleR][c] == '.') {
		    addPawnMove(r, c, doubleR, c, moves, true);
		}
	    }
	    int capR = r + dir;
	    int capC1 = c - 1;
	    int capC2 = c + 1;
	    if (inBounds(capR, capC1) && board[capR][capC1] != '.') {
		if (whitePiece ? isBlackPiece(board[capR][capC1]) : isWhitePiece(board[capR][capC1])) {
		    addPawnMove(r, c, capR, capC1, moves, false);
		}
	    }
	    if (inBounds(capR, capC2) && board[capR][capC2] != '.') {
		if (whitePiece ? isBlackPiece(board[capR][capC2]) : isWhitePiece(board[capR][capC2])) {
		    addPawnMove(r, c, capR, capC2, moves, false);
		}
	    }
	    // En passant
	    if (enPassantCol != -1 && Math.abs(enPassantCol - c) == 1 && enPassantRow == capR) {
		Move m = new Move();
		m.fromR = r; m.fromC = c; m.toR = capR; m.toC = enPassantCol;
		m.enPassant = true;
		moves.addElement(m);
	    }
	} else if (Character.toUpperCase(p) == 'N') {
	    int[] nr = { -2, -2, -1, -1, 1, 1, 2, 2 };
	    int[] nc = { -1, 1, -2, 2, -2, 2, -1, 1 };
	    for (int i = 0; i < 8; i++) {
		int tr = r + nr[i];
		int tc = c + nc[i];
		if (inBounds(tr, tc)) {
		    char t = board[tr][tc];
		    if (t == '.' || (whitePiece ? isBlackPiece(t) : isWhitePiece(t))) {
			Move m = new Move();
			m.fromR = r; m.fromC = c; m.toR = tr; m.toC = tc;
			moves.addElement(m);
		    }
		}
	    }
	} else if (Character.toUpperCase(p) == 'B' || Character.toUpperCase(p) == 'R' || Character.toUpperCase(p) == 'Q') {
	    int[] drs, dcs;
	    if (Character.toUpperCase(p) == 'B') {
		drs = new int[]{ -1, -1, 1, 1 };
		dcs = new int[]{ -1, 1, -1, 1 };
	    } else if (Character.toUpperCase(p) == 'R') {
		drs = new int[]{ -1, 1, 0, 0 };
		dcs = new int[]{ 0, 0, -1, 1 };
	    } else {
		drs = new int[]{ -1, -1, -1, 0, 0, 1, 1, 1 };
		dcs = new int[]{ -1, 0, 1, -1, 1, -1, 0, 1 };
	    }
	    for (int i = 0; i < drs.length; i++) {
		int tr = r + drs[i];
		int tc = c + dcs[i];
		while (inBounds(tr, tc)) {
		    char t = board[tr][tc];
		    if (t == '.') {
			Move m = new Move();
			m.fromR = r; m.fromC = c; m.toR = tr; m.toC = tc;
			moves.addElement(m);
		    } else {
			if (whitePiece ? isBlackPiece(t) : isWhitePiece(t)) {
			    Move m = new Move();
			    m.fromR = r; m.fromC = c; m.toR = tr; m.toC = tc;
			    moves.addElement(m);
			}
			break;
		    }
		    tr += drs[i];
		    tc += dcs[i];
		}
	    }
	} else if (Character.toUpperCase(p) == 'K') {
	    int dr, dc;
	    for (dr = -1; dr <= 1; dr++) {
		for (dc = -1; dc <= 1; dc++) {
		    if (dr == 0 && dc == 0) continue;
		    int tr = r + dr;
		    int tc = c + dc;
		    if (inBounds(tr, tc)) {
			char t = board[tr][tc];
			if (t == '.' || (whitePiece ? isBlackPiece(t) : isWhitePiece(t))) {
			    Move m = new Move();
			    m.fromR = r; m.fromC = c; m.toR = tr; m.toC = tc;
			    moves.addElement(m);
			}
		    }
		}
	    }
	    // Castling
	    if (whitePiece && !whiteKingMoved && !isInCheck(true)) {
		if (!whiteRookHMoved && board[7][5] == '.' && board[7][6] == '.' && !isSquareAttacked(7,5,false) && !isSquareAttacked(7,6,false)) {
		    Move m = new Move();
		    m.fromR = 7; m.fromC = 4; m.toR = 7; m.toC = 6;
		    m.castle = true; m.rookFromC = 7; m.rookToC = 5;
		    moves.addElement(m);
		}
		if (!whiteRookAMoved && board[7][1] == '.' && board[7][2] == '.' && board[7][3] == '.' && !isSquareAttacked(7,2,false) && !isSquareAttacked(7,3,false)) {
		    Move m = new Move();
		    m.fromR = 7; m.fromC = 4; m.toR = 7; m.toC = 2;
		    m.castle = true; m.rookFromC = 0; m.rookToC = 3;
		    moves.addElement(m);
		}
	    }
	    if (!whitePiece && !blackKingMoved && !isInCheck(false)) {
		if (!blackRookHMoved && board[0][5] == '.' && board[0][6] == '.' && !isSquareAttacked(0,5,true) && !isSquareAttacked(0,6,true)) {
		    Move m = new Move();
		    m.fromR = 0; m.fromC = 4; m.toR = 0; m.toC = 6;
		    m.castle = true; m.rookFromC = 7; m.rookToC = 5;
		    moves.addElement(m);
		}
		if (!blackRookAMoved && board[0][1] == '.' && board[0][2] == '.' && board[0][3] == '.' && !isSquareAttacked(0,2,true) && !isSquareAttacked(0,3,true)) {
		    Move m = new Move();
		    m.fromR = 0; m.fromC = 4; m.toR = 0; m.toC = 2;
		    m.castle = true; m.rookFromC = 0; m.rookToC = 3;
		    moves.addElement(m);
		}
	    }
	}
	// Filter out moves that leave king in check
	Vector legal = new Vector();
	for (int i = 0; i < moves.size(); i++) {
	    Move m = (Move) moves.elementAt(i);
	    performMove(m, false);
	    boolean inCheck = isInCheck(whitePiece);
	    revertMove(m);
	    if (!inCheck) {
		legal.addElement(m);
	    }
	}
	return legal;
    }

    /* Add pawn move including promotion detection. */
    void addPawnMove(int fr, int fc, int tr, int tc, Vector moves, boolean doubleStep) {
	Move m = new Move();
	m.fromR = fr; m.fromC = fc; m.toR = tr; m.toC = tc; m.doubleStep = doubleStep;
	if (tr == 0 || tr == 7) {
	    m.promotion = true;
	}
	moves.addElement(m);
    }

    /* Apply a move. If saveHistory is true, push to moveHistory. */
    void performMove(Move m, boolean saveHistory) {
	m.prevEnPassantRow = enPassantRow;
	m.prevEnPassantCol = enPassantCol;
	m.prevWhiteKingMoved = whiteKingMoved;
	m.prevBlackKingMoved = blackKingMoved;
	m.prevWhiteRookAMoved = whiteRookAMoved;
	m.prevWhiteRookHMoved = whiteRookHMoved;
	m.prevBlackRookAMoved = blackRookAMoved;
	m.prevBlackRookHMoved = blackRookHMoved;
	m.movedPiece = board[m.fromR][m.fromC];
	m.captured = board[m.toR][m.toC];
	// Move piece
	board[m.fromR][m.fromC] = '.';
	// En passant capture
	if (m.enPassant) {
	    int capRow = m.fromR + (isWhitePiece(m.movedPiece) ? -1 : 1);
	    m.captured = board[capRow][m.toC];
	    board[capRow][m.toC] = '.';
	}
	// Castling rook move
	if (m.castle) {
	    board[m.toR][m.toC] = m.movedPiece;
	    board[m.fromR][m.fromC] = '.';
	    board[m.toR][m.rookToC] = board[m.toR][m.rookFromC];
	    board[m.toR][m.rookFromC] = '.';
	} else {
	    board[m.toR][m.toC] = m.movedPiece;
	}
	// Promotion
	if (m.promotion) {
	    if (isWhitePiece(m.movedPiece)) {
		board[m.toR][m.toC] = 'Q';
	    } else {
		board[m.toR][m.toC] = 'q';
	    }
	}
	// Update en passant target
	enPassantRow = -1;
	enPassantCol = -1;
	if (Character.toUpperCase(m.movedPiece) == 'P' && m.doubleStep) {
	    enPassantRow = (m.fromR + m.toR) / 2;
	    enPassantCol = m.fromC;
	}
	// Update castling rights
	if (m.movedPiece == 'K') whiteKingMoved = true;
	if (m.movedPiece == 'k') blackKingMoved = true;
	if (m.movedPiece == 'R' && m.fromR == 7 && m.fromC == 0) whiteRookAMoved = true;
	if (m.movedPiece == 'R' && m.fromR == 7 && m.fromC == 7) whiteRookHMoved = true;
	if (m.movedPiece == 'r' && m.fromR == 0 && m.fromC == 0) blackRookAMoved = true;
	if (m.movedPiece == 'r' && m.fromR == 0 && m.fromC == 7) blackRookHMoved = true;
	if (m.movedPiece == 'K' && m.castle) {
	    whiteRookAMoved = true;
	    whiteRookHMoved = true;
	}
	if (m.movedPiece == 'k' && m.castle) {
	    blackRookAMoved = true;
	    blackRookHMoved = true;
	}
	whiteToMove = !whiteToMove;
	if (saveHistory) {
	    moveHistory.addElement(m);
	}
    }

    /* Undo a move after performMove. */
    void revertMove(Move m) {
	whiteToMove = !whiteToMove;
	whiteKingMoved = m.prevWhiteKingMoved;
	blackKingMoved = m.prevBlackKingMoved;
	whiteRookAMoved = m.prevWhiteRookAMoved;
	whiteRookHMoved = m.prevWhiteRookHMoved;
	blackRookAMoved = m.prevBlackRookAMoved;
	blackRookHMoved = m.prevBlackRookHMoved;
	enPassantRow = m.prevEnPassantRow;
	enPassantCol = m.prevEnPassantCol;
	// Revert board
	if (m.castle) {
	    board[m.fromR][m.fromC] = m.movedPiece;
	    board[m.toR][m.toC] = '.';
	    board[m.toR][m.rookFromC] = board[m.toR][m.rookToC];
	    board[m.toR][m.rookToC] = '.';
	} else {
	    board[m.fromR][m.fromC] = m.movedPiece;
	    board[m.toR][m.toC] = m.captured;
	}
	if (m.promotion) {
	    board[m.fromR][m.fromC] = isWhitePiece(m.movedPiece) ? 'P' : 'p';
	}
	if (m.enPassant) {
	    int capRow = m.fromR + (isWhitePiece(m.movedPiece) ? -1 : 1);
	    board[m.toR][m.toC] = '.';
	    board[capRow][m.toC] = m.captured;
	}
    }

    /* Public method to make a move chosen by the player. */
    public void makeMove(Move m) {
	performMove(m, true);
	selectedRow = -1;
	selectedCol = -1;
	statusMessage = "";
	repaint();
    }

    /* Undo the last move if possible. */
    public void undoMove() {
	if (moveHistory.size() == 0) {
	    statusMessage = "Nothing to undo.";
	    repaint();
	    return;
	}
	Move m = (Move) moveHistory.elementAt(moveHistory.size() - 1);
	moveHistory.removeElementAt(moveHistory.size() - 1);
	revertMove(m);
	statusMessage = "Move undone.";
	repaint();
    }

    boolean isCheckmate(boolean white) {
	if (!isInCheck(white)) return false;
	int r, c;
	for (r = 0; r < 8; r++) {
	    for (c = 0; c < 8; c++) {
		char p = board[r][c];
		if (p == '.') continue;
		if (white && isWhitePiece(p)) {
		    if (getLegalMoves(r, c).size() > 0) return false;
		}
		if (!white && isBlackPiece(p)) {
		    if (getLegalMoves(r, c).size() > 0) return false;
		}
	    }
	}
	return true;
    }

    boolean isStalemate(boolean white) {
	if (isInCheck(white)) return false;
	int r, c;
	for (r = 0; r < 8; r++) {
	    for (c = 0; c < 8; c++) {
		char p = board[r][c];
		if (p == '.') continue;
		if (white && isWhitePiece(p)) {
		    if (getLegalMoves(r, c).size() > 0) return false;
		}
		if (!white && isBlackPiece(p)) {
		    if (getLegalMoves(r, c).size() > 0) return false;
		}
	    }
	}
	return true;
    }

    public void mouseClicked(MouseEvent e) {
	int size = 50;
	int offsetX = 20;
	int offsetY = 60;
	int c = (e.getX() - offsetX) / size;
	int r = (e.getY() - offsetY) / size;
	if (!inBounds(r, c)) return;
	if (selectedRow == -1) {
	    if (board[r][c] != '.') {
		if ((whiteToMove && isWhitePiece(board[r][c])) || (!whiteToMove && isBlackPiece(board[r][c]))) {
		    selectedRow = r;
		    selectedCol = c;
		    statusMessage = "Square selected.";
		} else {
		    statusMessage = "Not your piece.";
		}
	    }
	} else {
	    Vector legal = getLegalMoves(selectedRow, selectedCol);
	    boolean found = false;
	    Move chosen = null;
	    for (int i = 0; i < legal.size(); i++) {
		Move m = (Move) legal.elementAt(i);
		if (m.toR == r && m.toC == c) {
		    chosen = m;
		    found = true;
		    break;
		}
	    }
	    if (found) {
		makeMove(chosen);
	    } else {
		selectedRow = -1;
		selectedCol = -1;
		statusMessage = "Illegal move.";
	    }
	}
	repaint();
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == newGameBtn) {
	    initBoard();
	} else if (e.getSource() == undoBtn) {
	    undoMove();
	} else if (e.getSource() == resetBtn) {
	    initBoard();
	}
    }

    /* Move class stores details needed for moves and undo. */
    class Move {
	int fromR, fromC, toR, toC;
	int rookFromC, rookToC;
	char captured;
	char movedPiece;
	boolean castle = false;
	boolean enPassant = false;
	boolean promotion = false;
	boolean doubleStep = false;
	int prevEnPassantRow, prevEnPassantCol;
	boolean prevWhiteKingMoved, prevBlackKingMoved;
	boolean prevWhiteRookAMoved, prevWhiteRookHMoved;
	boolean prevBlackRookAMoved, prevBlackRookHMoved;
    }
}
