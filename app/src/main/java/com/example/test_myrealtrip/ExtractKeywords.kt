package com.example.test_myrealtrip

import android.util.Log
import java.util.*

class ExtractKeywords(description: String){

    val keywords = ArrayList<String>()

    init {
        val wordToken = StringTokenizer(description, "\\\n\t ()!?[]{}\"\',.“”’‘…▲=",false)
        val wordMap = mutableMapOf<String, Int>()

        while(wordToken.hasMoreTokens()){
            val word = wordToken.nextToken()

            if(word.length > 1){
                if(word in wordMap.keys){
                    val num = wordMap.get(word) as Int
                    wordMap.set(word, num + 1)
                }
                else{
                    wordMap.put(word,1)
                }
            }
        }

        val sortedList = wordMap.toSortedMap(compareBy { it }).toList().sortedWith(
            compareByDescending ({it.second})
        )

        for (i in 0..2) {
            keywords.add(sortedList[i].first)
        }
    }

}