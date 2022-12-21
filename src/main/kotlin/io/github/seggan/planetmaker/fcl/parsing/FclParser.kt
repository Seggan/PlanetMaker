package io.github.seggan.planetmaker.fcl.parsing

import io.github.seggan.planetmaker.fcl.FclObject

class FclParser(
    private val tokens: List<Token>,
    private val predefined: Map<String, Any> = emptyMap(),
    private val parentheticalHandler: (String, Any, String) -> Any = { _, value, _ -> value }
) {

    private var pos = 0

    private val currentToken: Token
        get() = tokens[pos]

    private val nextToken: Token
        get() = if (pos + 1 < tokens.size) tokens[pos + 1] else throw FclParseException(
            "Unexpected end of file",
            currentToken
        )

    fun parse(): FclObject {
        return parseObject()
    }

    private fun parseObject(): FclObject {
        val name = consumeToken(TokenType.IDENTIFIER).value
        val properties = mutableMapOf<String, Any>()
        val objects = mutableListOf<FclObject>()

        if (nextToken.type == TokenType.COLON) {
            pos++
        }

        consumeToken(TokenType.LBRACE)

        while (currentToken.type != TokenType.RBRACE) {
            val key = consumeToken(TokenType.IDENTIFIER)
            when (currentToken.type) {
                TokenType.LBRACE -> {
                    pos--
                    objects.add(parseObject())
                }
                TokenType.COLON -> {
                    pos++
                    properties[key.value] = parseValue(name)
                }
                else -> throw FclParseException("Unexpected token '${nextToken.value}'", nextToken)
            }
            if (currentToken.type == TokenType.COMMA) {
                pos++
            }
        }

        consumeToken(TokenType.RBRACE)

        return FclObject(name, properties, objects)
    }

    private fun parseValue(name: String): Any {
        val token = tokens[pos++]
        val value = when (token.type) {
            TokenType.STRING -> token.value
            TokenType.NUMBER -> token.value.toBigDecimal()
            TokenType.TRUE -> true
            TokenType.FALSE -> false
            TokenType.IDENTIFIER -> predefined[token.value] ?: throw FclParseException(
                "Unknown identifier '${token.value}'",
                token
            )
            else -> throw FclParseException("Unexpected token '${token.value}'", token)
        }
        if (currentToken.type == TokenType.PARENTHETICAL) {
            return parentheticalHandler(name, value, currentToken.value)
        }
        return value
    }

    private fun consumeToken(type: TokenType): Token {
        if (pos >= tokens.size) {
            throw FclParseException("Unexpected end of file when parsing token of type '$type'", tokens[pos - 1])
        }

        val token = tokens[pos]
        if (token.type != type) {
            throw FclParseException("Unexpected token '${token.value}' when parsing token of type '$type'", token)
        }
        pos++
        return token
    }
}