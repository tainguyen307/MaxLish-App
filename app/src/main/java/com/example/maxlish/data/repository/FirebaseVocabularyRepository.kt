package com.example.maxlish.data.repository

import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.data.model.VocabularyWord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseVocabularyRepository(
    private val firestore: FirebaseFirestore
) : VocabularyRepository {

    // ======================
    // COLLECTIONS
    // ======================

    private val setCollection =
        firestore.collection("vocabulary_sets")

    private fun wordCollection(setId: String) =
        firestore.collection("vocabulary_sets")
            .document(setId)
            .collection("words")

    // ======================
    // SET
    // ======================

    override suspend fun getVocabularySets(
        ownerId: String
    ): Result<List<VocabularySet>> {
        return try {

            val snapshot = setCollection
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()

            val sets = snapshot.documents.mapNotNull { doc ->
                doc.toObject(VocabularySet::class.java)?.copy(
                    setId = doc.id
                )
            }

            Result.success(sets)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVocabularySetById(
        setId: String
    ): Result<VocabularySet> {

        return try {

            val doc = setCollection
                .document(setId)
                .get()
                .await()

            val data = doc.toObject(VocabularySet::class.java)

            if (data != null) {
                Result.success(data.copy(setId = doc.id))
            } else {
                Result.failure(Exception("Set not found"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createVocabularySet(
        vocabularySet: VocabularySet
    ): Result<String> {

        return try {

            val docRef = if (vocabularySet.setId.isBlank()) {
                setCollection.document()
            } else {
                setCollection.document(vocabularySet.setId)
            }

            val finalData = vocabularySet.copy(
                setId = docRef.id
            )

            docRef.set(finalData).await()

            Result.success(docRef.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateVocabularySet(
        vocabularySet: VocabularySet
    ): Result<Unit> {

        return try {

            setCollection
                .document(vocabularySet.setId)
                .set(vocabularySet)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteVocabularySet(
        setId: String
    ): Result<Unit> {

        return try {

            setCollection
                .document(setId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    // ======================
    // WORD
    // ======================

    override suspend fun getWordsBySetId(
        setId: String
    ): Result<List<VocabularyWord>> {

        return try {

            val snapshot = wordCollection(setId)
                .get()
                .await()

            val words = snapshot.documents.mapNotNull { doc ->
                doc.toObject(VocabularyWord::class.java)?.copy(
                    wordId = doc.id
                )
            }

            Result.success(words)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createWord(
        setId: String,
        word: VocabularyWord
    ): Result<String> {

        return try {

            val docRef = if (word.wordId.isBlank()) {
                wordCollection(setId).document()
            } else {
                wordCollection(setId).document(word.wordId)
            }

            val finalWord = word.copy(
                wordId = docRef.id
            )

            docRef.set(finalWord).await()

            updateWordCount(setId)

            Result.success(docRef.id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateWord(
        setId: String,
        word: VocabularyWord
    ): Result<Unit> {

        return try {

            wordCollection(setId)
                .document(word.wordId)
                .set(word)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteWord(
        setId: String,
        wordId: String
    ): Result<Unit> {

        return try {

            wordCollection(setId)
                .document(wordId)
                .delete()
                .await()

            updateWordCount(setId)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ======================
    // HELPER
    // ======================

    private suspend fun updateWordCount(
        setId: String
    ) {

        val snapshot = wordCollection(setId)
            .get()
            .await()

        val count = snapshot.size()

        setCollection
            .document(setId)
            .update("wordCount", count)
            .await()
    }
}