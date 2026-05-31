package com.example.maxlish.data.helper

import com.example.maxlish.data.model.VocabularyWord
import java.util.UUID

object CsvHelper {
    fun parseCsv(
        csvText: String,
        ownerId: String,
        setId: String
    ): List<VocabularyWord> {
        val words = mutableListOf<VocabularyWord>()
        val lines = mutableListOf<String>()
        
        // Simple robust line splitting that ignores newlines inside quotes
        val currentLine = StringBuilder()
        var inQuotes = false
        for (char in csvText) {
            if (char == '"') {
                inQuotes = !inQuotes
            }
            if (char == '\n' && !inQuotes) {
                lines.add(currentLine.toString())
                currentLine.clear()
            } else {
                currentLine.append(char)
            }
        }
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        if (lines.isEmpty()) return emptyList()

        // Header validation and mapping index
        val headerLine = lines.first()
        val headers = parseCsvLine(headerLine).map { it.lowercase() }
        
        val wordIndex = headers.indexOf("word")
        val pronunciationIndex = headers.indexOf("pronunciation")
        val meaningIndex = headers.indexOf("meaning")
        val descriptionIndex = headers.indexOf("description")
        val exampleIndex = headers.indexOf("example")
        val collocationsIndex = headers.indexOf("collocations")
        val relatedWordsIndex = headers.indexOf("relatedwords")
        val noteIndex = headers.indexOf("note")
        val difficultyIndex = headers.indexOf("difficulty")

        // Standard validation: if we don't have word and meaning, we can't import
        if (wordIndex == -1 || meaningIndex == -1) {
            return emptyList()
        }

        for (i in 1 until lines.size) {
            val line = lines[i]
            if (line.isBlank()) continue
            val tokens = parseCsvLine(line)
            
            val wordText = if (wordIndex in tokens.indices) tokens[wordIndex] else ""
            val meaningText = if (meaningIndex in tokens.indices) tokens[meaningIndex] else ""
            
            if (wordText.isBlank() || meaningText.isBlank()) continue

            val pronunciationText = if (pronunciationIndex in tokens.indices) tokens[pronunciationIndex] else ""
            val descriptionText = if (descriptionIndex in tokens.indices) tokens[descriptionIndex] else ""
            val exampleText = if (exampleIndex in tokens.indices) tokens[exampleIndex] else ""
            
            val collocationsList = if (collocationsIndex in tokens.indices && tokens[collocationsIndex].isNotBlank()) {
                tokens[collocationsIndex].split(";").map { it.trim() }.filter { it.isNotEmpty() }
            } else emptyList()

            val relatedList = if (relatedWordsIndex in tokens.indices && tokens[relatedWordsIndex].isNotBlank()) {
                tokens[relatedWordsIndex].split(";").map { it.trim() }.filter { it.isNotEmpty() }
            } else emptyList()

            val noteText = if (noteIndex in tokens.indices) tokens[noteIndex] else ""
            val difficultyText = if (difficultyIndex in tokens.indices) tokens[difficultyIndex] else "Easy"

            words.add(
                VocabularyWord(
                    wordId = UUID.randomUUID().toString(),
                    setId = setId,
                    ownerId = ownerId,
                    word = wordText,
                    pronunciation = pronunciationText,
                    meaning = meaningText,
                    description = descriptionText,
                    example = exampleText,
                    collocations = collocationsList,
                    relatedWords = relatedList,
                    note = noteText,
                    difficulty = difficultyText,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
        return words
    }

    fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val currentToken = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    currentToken.append('"')
                    i++ // Skip double double quote escape
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == ',' && !inQuotes) {
                result.add(currentToken.toString().trim())
                currentToken.clear()
            } else {
                currentToken.append(c)
            }
            i++
        }
        result.add(currentToken.toString().trim())
        return result
    }

    fun exportToCsv(words: List<VocabularyWord>): String {
        val sb = java.lang.StringBuilder()
        sb.append("Word,Pronunciation,Meaning,Description,Example,Collocations,RelatedWords,Note,Difficulty\n")
        for (word in words) {
            sb.append(escapeCsv(word.word)).append(",")
            sb.append(escapeCsv(word.pronunciation)).append(",")
            sb.append(escapeCsv(word.meaning)).append(",")
            sb.append(escapeCsv(word.description)).append(",")
            sb.append(escapeCsv(word.example)).append(",")
            sb.append(escapeCsv(word.collocations.joinToString(";"))).append(",")
            sb.append(escapeCsv(word.relatedWords.joinToString(";"))).append(",")
            sb.append(escapeCsv(word.note)).append(",")
            sb.append(escapeCsv(word.difficulty)).append("\n")
        }
        return sb.toString()
    }

    private fun escapeCsv(value: String): String {
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains(";")) {
            return "\"" + value.replace("\"", "\"\"") + "\""
        }
        return value
    }
}
