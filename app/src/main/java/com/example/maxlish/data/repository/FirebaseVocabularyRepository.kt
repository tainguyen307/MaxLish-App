package com.example.maxlish.data.repository

import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.data.model.VocabularyWord
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseVocabularyRepository(
    private val firestore: FirebaseFirestore
) : VocabularyRepository {

    // ======================
    // COLLECTIONS
    // ======================

    private val setCollection =
        firestore.collection("vocabulary_sets")

    private val wordCollection =
        firestore.collection("vocabulary_words")

    // ======================
    // SET
    // ======================

    fun observeVocabularySets(userId: String) =
        callbackFlow {

            val listener =
                firestore.collection("vocabulary_sets")
                    .whereEqualTo("ownerId", userId)
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }

                        val sets =
                            snapshot?.documents?.mapNotNull { doc ->
                                doc.toObject(VocabularySet::class.java)?.copy(
                                    setId = doc.id
                                )
                            } ?: emptyList()

                        trySend(sets)
                    }

            awaitClose { listener.remove() }
        }

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

            val docRef = setCollection.document()

            val finalData = vocabularySet.copy(
                setId = docRef.id,
                wordCount = 0,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
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

            // delete words first
            val words = wordCollection
                .whereEqualTo("setId", setId)
                .get()
                .await()

            val batch = firestore.batch()

            words.documents.forEach {
                batch.delete(it.reference)
            }

            batch.delete(setCollection.document(setId))

            batch.commit().await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ======================
    // WORD (FLAT MODEL)
    // ======================

    override suspend fun getWordsBySetId(
        setId: String
    ): Result<List<VocabularyWord>> {

        return try {

            val snapshot = wordCollection
                .whereEqualTo("setId", setId)
                .get()
                .await()

            val words = snapshot.documents.mapNotNull {
                it.toObject(VocabularyWord::class.java)?.copy(
                    wordId = it.id
                )
            }

            Result.success(words)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWordById(
        setId: String,
        wordId: String
    ): Result<VocabularyWord> {

        return try {

            val doc = wordCollection
                .document(wordId)
                .get()
                .await()

            val data = doc.toObject(VocabularyWord::class.java)

            if (data != null) {
                Result.success(data.copy(wordId = doc.id))
            } else {
                Result.failure(Exception("Word not found"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createWord(
        setId: String,
        word: VocabularyWord
    ): Result<String> {

        return try {

            val docRef = wordCollection.document()

            val finalWord = word.copy(
                wordId = docRef.id,
                setId = setId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            docRef.set(finalWord).await()

            // increment counter (SAFE + FAST)
            setCollection.document(setId)
                .update("wordCount", FieldValue.increment(1))
                .await()

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

            wordCollection
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

            wordCollection
                .document(wordId)
                .delete()
                .await()

            // decrement counter
            setCollection.document(setId)
                .update("wordCount", FieldValue.increment(-1))
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}