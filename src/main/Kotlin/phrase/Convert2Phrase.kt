package edu.udel.phrase

import java.lang.StringBuilder

fun updateMap(map: HashMap<Int, Pair<String, String>>):  HashMap<Int, Pair<String, String>>{

    val convertMap = HashMap<String, String>()

    convertMap["CC"] = "CoordinatingConjunction"
    convertMap["CD"] = "Number"
    convertMap["DT"] = "Determiner"
    convertMap["EX"] = "There"
    convertMap["FW"] = "ForeignWord"
    convertMap["IN"] = "Preposition"
    convertMap["JJ"] = "Adjective"
    convertMap["JJR"] = "Adjective"
    convertMap["JJS"] = "Adjective"
    convertMap["LS"] = "ItemMarker"
    convertMap["NN"] = "Noun"
    convertMap["NNS"] = "Noun"
    convertMap["NNP"] = "Noun"
    convertMap["NNPS"] = "Noun"
    convertMap["RB"] = "Adverb"
    convertMap["RBS"] = "Adverb"
    convertMap["RBR"] = "Adverb"
    convertMap["SYM"] = "Symbol"
    convertMap["TO"] = "To"
    convertMap["VB"] = "Verb"
    convertMap["VBD"] = "Verb"
    convertMap["VBG"] = "Verb"
    convertMap["VBN"] = "Verb"
    convertMap["VBP"] = "Verb"
    convertMap["VBZ"] = "Verb"
    convertMap["RP"] = "Particle"

    val result =  HashMap<Int, Pair<String, String>>()

    for (i in 0 until map.keys.size){

        val curWord = map[i]?.first.orEmpty()
        val curTag = map[i]?.second.orEmpty()

        if (convertMap.containsKey(curTag)){

            result[i] = Pair(curWord, convertMap[curTag].orEmpty())
        }
        else{

            result[i] = Pair(curWord, "Unknown")
        }
    }

    return result
}

fun convert2Phrase(map: HashMap<Int, Pair<String, String>>): String {

    val convertMap = HashMap<String, String>()

    convertMap["CC"] = "CoordinatingConjunction"
    convertMap["CD"] = "Number"
    convertMap["DT"] = "Determiner"
    convertMap["EX"] = "There"
    convertMap["FW"] = "ForeignWord"
    convertMap["IN"] = "Preposition"
    convertMap["JJ"] = "Adjective"
    convertMap["JJR"] = "Adjective"
    convertMap["JJS"] = "Adjective"
    convertMap["LS"] = "ItemMarker"
    convertMap["NN"] = "Noun"
    convertMap["NNS"] = "Noun"
    convertMap["NNP"] = "Noun"
    convertMap["NNPS"] = "Noun"
    convertMap["RB"] = "Adverb"
    convertMap["RBS"] = "Adverb"
    convertMap["RBR"] = "Adverb"
    convertMap["SYM"] = "Symbol"
    convertMap["TO"] = "To"
    convertMap["VB"] = "Verb"
    convertMap["VBD"] = "Verb"
    convertMap["VBG"] = "Verb"
    convertMap["VBN"] = "Verb"
    convertMap["VBP"] = "Verb"
    convertMap["VBZ"] = "Verb"
    convertMap["RP"] = "Particle"

    val result = StringBuilder()

    for (i in 0 until map.keys.size){

        val curWord = map[i]?.first.orEmpty()
        val curTag = map[i]?.second.orEmpty()

        if (i == 0){

            result.append(curWord + "-" + "<" + convertMap.getOrDefault(curTag, "Unknown") + ">")
            continue
        }

        result.append("-"  + curWord + "-" + "<" + convertMap.getOrDefault(curTag, "Unknown") + ">")
    }

    return result.toString()
}