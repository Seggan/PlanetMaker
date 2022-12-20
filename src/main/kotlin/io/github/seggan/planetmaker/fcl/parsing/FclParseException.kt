package io.github.seggan.planetmaker.fcl.parsing

import io.github.seggan.planetmaker.fcl.parsing.Token

class FclParseException : RuntimeException {
    constructor(message: String, line: Int, col: Int) : super("(line $line, col $col): $message")
    constructor(message: String, token: Token) : this(message, token.line, token.col)
    constructor(message: String) : super(message)
}