package io.github.seggan.planetmaker.fcl.parsing

class FclLexer(private val input: String) {

    private var pos = 0
    private var line = 1
    private var col = 1

    private val currentChar: Char
        get() = input[pos]

    private val nextChar: Char
        get() = input[pos + 1]

    fun lex(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (pos < input.length) {
            tokens.add(
                when (currentChar) {
                    '{' -> Token(TokenType.LBRACE, "{", line, col).also(::advance)
                    '}' -> Token(TokenType.RBRACE, "}", line, col).also(::advance)
                    '[' -> Token(TokenType.LBRACKET, "[", line, col).also(::advance)
                    ']' -> Token(TokenType.RBRACKET, "]", line, col).also(::advance)
                    '(' -> {
                        try {
                            lexParenthetical()
                        } catch (e: IndexOutOfBoundsException) {
                            throw FclParseException("Unexpected end of file when lexing parenthetical", line, col)
                        }
                    }
                    '"' -> {
                        try {
                            lexString()
                        } catch (e: IndexOutOfBoundsException) {
                            throw FclParseException("Unexpected end of file when lexing string", line, col)
                        }
                    }
                    ':' -> Token(TokenType.COLON, ":", line, col).also(::advance)
                    ',' -> Token(TokenType.COMMA, ",", line, col).also(::advance)
                    in '0'..'9', '.' -> lexNumber()
                    in 'a'..'z', in 'A'..'Z', '_' -> lexIdentifier()
                    else -> {
                        if (currentChar.isWhitespace()) {
                            pos++
                            col++
                            continue
                        } else if (currentChar == '/' && nextChar == '/') {
                            pos += 2
                            col += 2
                            while (currentChar != '\n') {
                                pos++
                                col++
                            }
                            continue
                        }
                        throw FclParseException("Unexpected character '${currentChar}'", line, col)
                    }
                }
            )
        }

        return tokens
    }

    private fun lexString(): Token {
        val startLine = line
        val startCol = col
        val sb = StringBuilder()

        advance()

        while (true) {
            when (currentChar) {
                '"' -> return Token(TokenType.STRING, sb.toString(), startLine, startCol).also(::advance)
                '\\' -> {
                    advance()
                    sb.append(
                        when (currentChar) {
                            '"' -> '"'
                            '\\' -> '\\'
                            'n' -> '\n'
                            'r' -> '\r'
                            't' -> '\t'
                            'u' -> {
                                pos++
                                col++
                                val code = input.substring(pos, pos + 4)
                                pos += 4
                                col += 4
                                code.toInt(16).toChar()
                            }
                            else -> throw FclParseException("Invalid escape sequence '\\$currentChar'", line, col)
                        }
                    )
                    advance()
                }
                '\n' -> {
                    pos++
                    line++
                    col = 1
                    sb.append('\n')
                }
                else -> sb.append(currentChar).also(::advance)
            }
        }
    }

    private fun lexParenthetical(): Token {
        val startLine = line
        val startCol = col
        val sb = StringBuilder()

        advance()

        while (currentChar != ')') {
            sb.append(currentChar)
            advance()
        }

        return Token(TokenType.PARENTHETICAL, sb.toString(), startLine, startCol).also(::advance)
    }

    private fun lexNumber(): Token {
        val startLine = line
        val startCol = col

        val sb = StringBuilder()

        while (pos < input.length && (currentChar in '0'..'9' || currentChar == '.')) {
            sb.append(currentChar)
            advance()
        }

        val number = sb.toString()
        if (number.count { it == '.' } > 1) {
            throw FclParseException("Invalid number '$number'", startLine, startCol)
        }

        return Token(TokenType.NUMBER, number, startLine, startCol)
    }

    private fun lexIdentifier(): Token {
        val startLine = line
        val startCol = col

        val sb = StringBuilder()

        while (pos < input.length &&
            (currentChar in 'a'..'z' || currentChar in 'A'..'Z'
                    || currentChar in '0'..'9' || currentChar == '_')
        ) {
            sb.append(currentChar)
            pos++
            col++
        }

        val identifier = sb.toString()
        val type = when (identifier.lowercase()) {
            "true" -> TokenType.TRUE
            "false" -> TokenType.FALSE
            "null" -> TokenType.NULL
            else -> TokenType.IDENTIFIER
        }

        return Token(type, identifier, startLine, startCol)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun advance(unused: Any? = null) {
        pos++
        col++
    }
}

enum class TokenType {
    IDENTIFIER,
    STRING,
    NUMBER,
    LBRACE,
    RBRACE,
    LBRACKET,
    RBRACKET,
    COMMA,
    COLON,
    TRUE,
    FALSE,
    NULL,
    PARENTHETICAL
}

data class Token(val type: TokenType, val value: String, val line: Int, val col: Int)