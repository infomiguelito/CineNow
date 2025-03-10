package com.cinenow

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRecommender(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val modelFile = "movie_recommender.tflite"
    
    init {
        loadModel()
    }
    
    private fun loadModel() {
        try {
            val modelPath = File(context.getExternalFilesDir(null), modelFile)
            interpreter = Interpreter(modelPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    data class MovieFeatures(
        val id: Int,
        val genres: List<String>,
        val rating: Float,
        val releaseYear: Int,
        val popularity: Float
    )
    
    suspend fun getRecommendations(
        watchedMovies: List<MovieFeatures>,
        userPreferences: Map<String, Float>
    ): List<Int> = withContext(Dispatchers.Default) {
        val inputBuffer = ByteBuffer.allocateDirect(1 * 128 * 4) // Ajuste o tamanho conforme seu modelo
        inputBuffer.order(ByteOrder.nativeOrder())
        
        // Preparar os dados de entrada com base no histórico e preferências
        prepareInputData(inputBuffer, watchedMovies, userPreferences)
        
        // Buffer para os resultados
        val outputBuffer = ByteBuffer.allocateDirect(1 * 10 * 4) // Top 10 recomendações
        outputBuffer.order(ByteOrder.nativeOrder())
        
        // Executar a inferência
        interpreter?.run(inputBuffer, outputBuffer)
        
        // Converter o resultado em IDs de filmes
        return@withContext parseOutputBuffer(outputBuffer)
    }
    
    private fun prepareInputData(
        buffer: ByteBuffer,
        watchedMovies: List<MovieFeatures>,
        userPreferences: Map<String, Float>
    ) {
        // Implementar a lógica de preparação dos dados
        // Este é um exemplo simplificado
        watchedMovies.forEach { movie ->
            buffer.putFloat(movie.rating)
            buffer.putFloat(movie.releaseYear.toFloat())
            buffer.putFloat(movie.popularity)
        }
        
        userPreferences.forEach { (_, value) ->
            buffer.putFloat(value)
        }
    }
    
    private fun parseOutputBuffer(buffer: ByteBuffer): List<Int> {
        val movieIds = mutableListOf<Int>()
        buffer.rewind()
        
        repeat(10) {
            movieIds.add(buffer.getInt())
        }
        
        return movieIds
    }
} 