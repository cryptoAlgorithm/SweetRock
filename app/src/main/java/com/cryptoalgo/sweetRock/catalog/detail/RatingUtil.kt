package com.cryptoalgo.sweetRock.catalog.detail

import com.cryptoalgo.sweetRock.model.Rating

object RatingUtil {
    fun ratingFrequency(ratings: List<Rating>): HashMap<Int, Float> {
        val sums = hashMapOf<Int, Int>()
        ratings.forEach {
            sums[it.rating.toInt()] = (sums[it.rating.toInt()] ?: 0) + 1
        }
        val freq = hashMapOf<Int, Float>()
        sums.entries.forEach {
            freq[it.key] = it.value.toFloat() / ratings.size.toFloat()
        }
        return freq
    }

    fun average(ratings: List<Rating>): Float {
        var sum = 0f
        ratings.forEach { sum += it.rating }
        return sum / ratings.size.toFloat()
    }
}