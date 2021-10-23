package me.fetsh.geekbrains.pd.model

import com.github.kittinunf.forge.core.*
import com.github.kittinunf.forge.util.create

data class DataModel(
    val query: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val translation: Translation,
    val imageUrl: String
)
data class Translation(
    val text: String
)

val dataModelDeserializer = { json: JSON ->
    ::DataModel.create
        .map(json at "text")
        .apply(json.list("meanings", ::meaningDeserializer))
}

fun meaningDeserializer(json: JSON) =
    ::Meaning.create.
        map(json.at("translation", translationDeserializer)).
        apply(json at "imageUrl")

val translationDeserializer = { json: JSON ->
    ::Translation.create.
        map(json at "text")
}