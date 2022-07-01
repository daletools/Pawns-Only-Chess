package chess

import kotlin.system.exitProcess

var p1: String = ""
var p2: String = ""
var turnCount = 0
val moveSyntax = Regex("\\b[a-h][1-8][a-h][1-8]\\b")
var last = mutableListOf<Int?>(null, null)
var origin = mutableListOf<Int>()
var originText = ""
var dest = mutableListOf<Int>()
enum class Color(val id: Int,val character: Char,val homeRow: Int){
    WHITE(1, 'w', 1),
    BLACK(-1, 'b', 6)
}

val board = mutableListOf(
    mutableListOf<Char?>(null, null, null, null, null, null, null, null),
    mutableListOf<Char?>('w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'),
    mutableListOf<Char?>(null, null, null, null, null, null, null, null),
    mutableListOf<Char?>(null, null, null, null, null, null, null, null),
    mutableListOf<Char?>(null, null, null, null, null, null, null, null),
    mutableListOf<Char?>(null, null, null, null, null, null, null, null),
    mutableListOf<Char?>('b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'),
    mutableListOf<Char?>(null, null, null, null, null, null, null, null)
)

fun draw() {
    println("  +---+---+---+---+---+---+---+---+")
    for (r in board.indices.reversed()) {
        print("${r+1} | ")
        for (c in board[r].indices) {
            if(board[r][c] != null) {
                print("${board[r][c]?.uppercase()} | ")
            } else {
                print("  | ")
            }
        }
        println("\n  +---+---+---+---+---+---+---+---+")
    }
    println("    a   b   c   d   e   f   g   h")
    winCheck()
}

fun setup() {
    println(" Pawns-Only Chess\n")
    println("First Player's name:")
    p1 = readln()
    println("Second Player's name:")
    p2 = readln()
    draw()
}

fun prompt() {
    turnCount++

    if (turnCount % 2 != 0) {
        println("$p1's turn:")
    } else {
        println("$p2's turn:")
    }

    val move: String = readln()
    if (move.matches(moveSyntax)) {
        legalize(move, (turnCount % 2))
    } else if (move == "exit") {
        println("Bye!")
        exitProcess(0)
    } else {
        turnCount--
        println("Invalid Input at prompt")
        prompt()
    }
}

fun legalize(move: String, player: Int) {

    origin = mutableListOf(move[0].code - 97, move[1].digitToInt() - 1)
    originText = move.substring(0,2)
    dest = mutableListOf(move[2].code - 97, move[3].digitToInt() - 1)
    val opp: Color
    val color: Color
    if (player == 1) {
        color = Color.WHITE
        opp = Color.BLACK
    } else {
        color = Color.BLACK
        opp = Color.WHITE
    }


    if (board[origin[1]][origin[0]] == color.character) {
        if (dest[0] == origin[0]) {
            if (dest[1] == origin[1] + color.id && board[dest[1]][dest[0]] == null) {
                board[origin[1]][origin[0]] = null
                board[dest[1]][dest[0]] = color.character
                last[0] = dest[0]
                last[1] = dest[1]
                draw()
            } else if (origin[1] == color.homeRow && dest[1] == origin[1] + (2 * color.id) && board[dest[1]][dest[0]] == null) { //dbl space move
                board[origin[1]][origin[0]] = null
                board[dest[1]][dest[0]] = color.character
                last[0] = dest[0]
                last[1] = dest[1]
                draw()
            } else {
                println("Invalid Input")
                turnCount--
                prompt()
            }
        } else if (dest[0] in origin[0] - 1..origin[0] + 1){
            if (board[dest[1]][dest[0]] != color.character && board[dest[1]][dest[0]] != null) {
                board[origin[1]][origin[0]] = null
                board[dest[1]][dest[0]] = color.character
                last[0] = null
                last[1] = null
                draw()
            } else if (dest[0] == last[0] && dest[1] == last[1]!! + color.id && board[last[1]!!][last[0]!!] == opp.character) {
                board[origin[1]][origin[0]] = null
                board[last[1]!!][last[0]!!] = null
                board[dest[1]][dest[0]] = color.character
                last[0] = null
                last[1] = null
                draw()
            } else {
                println("Invalid Input")
                turnCount--
                prompt()
            }
        } else {
            println("Invalid Input")
            turnCount--
            prompt()
        }
    } else {
        println("No ${color.name.lowercase()} pawn at $originText")
        turnCount--
        prompt()
    }
}

fun winCheck() {
    if (board[0].contains('b')) {
        println("Black Wins!")
        println("Bye!")
        exitProcess(0)
    } else if (board[7].contains('w')) {
        println("White Wins!")
        println("Bye!")
        exitProcess(0)
    }

    if (!board.flatten().contains('b')) {
        println("White Wins!")
        println("Bye!")
        exitProcess(0)
    }

    if (!board.flatten().contains('w')) {
        println("Black Wins!")
        println("Bye!")
        exitProcess(0)
    }

            //stalemate detection, if every piece of either color is opposed by a foe, AND cannot capture diagonally
    var bMove = 0
    var wMove = 0
    for (row in board.indices) {
        for (col in board.indices) {
            if (board[row][col] == 'b' && (board[row - 1][col] == null || board[row - 1].getOrNull(col + 1) == 'w' || board[row - 1].getOrNull(col - 1) == 'w')) {
                bMove++
            } else if (board[row][col] == 'w' && (board[row + 1][col] == null || board[row + 1].getOrNull(col + 1) == 'b' || board[row + 1].getOrNull(col - 1) == 'b')) {
                wMove++
            }
        }
    }

    if (bMove == 0 || wMove == 0) {
        println("Stalemate!\nBye!")
        exitProcess(0)
    }

    prompt()
}

fun main() {
    setup()
}