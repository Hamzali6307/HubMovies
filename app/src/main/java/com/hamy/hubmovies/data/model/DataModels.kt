package com.hamy.hubmovies.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdrop_path: String? = null,
    @SerializedName("belongs_to_collection")
    val belongs_to_collection: BelongsToCollection? = null,
    @SerializedName("budget")
    val budget: Int? = null,
    @SerializedName("genres")
    val genres: List<Genre>? = null,
    @SerializedName("homepage")
    val homepage: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("imdb_id")
    val imdb_id: String? = null,
    @SerializedName("origin_country")
    val origin_country: List<String>? = null,
    @SerializedName("original_language")
    val original_language: String? = null,
    @SerializedName("original_title")
    val original_title: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("popularity")
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val poster_path: String? = null,
    @SerializedName("production_companies")
    val production_companies: List<ProductionCompany>? = null,
    @SerializedName("production_companies")
    val production_countries: List<ProductionCountry>? = null,
    @SerializedName("release_date")
    val release_date: String? = null,
    @SerializedName("revenue")
    val revenue: Int? = null,
    @SerializedName("runtime")
    val runtime: Int? = null,
    @SerializedName("spoken_languages")
    val spoken_languages: List<SpokenLanguage>? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("tagline")
    val tagline: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("video")
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val vote_average: Double? = null,
    @SerializedName("vote_count")
    val vote_count: Int? = null
)

data class BelongsToCollection(
    val id: Int? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null
)

data class Genre(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null
)

data class ProductionCompany(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("logo_path")
    val logo_path: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("origin_country")
    val origin_country: String? = null
)

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso_3166_1: String? = null,
    @SerializedName("name")
    val name: String? = null
)

data class SpokenLanguage(
    @SerializedName("english_name")
    val english_name: String? = null,
    @SerializedName("iso_639_1")
    val iso_639_1: String? = null,
    @SerializedName("name")
    val name: String? = null
)

data class Movies(
    val page: Int?,
    val results: List<Results?>?
) {

    data class Results(
        val id: Long?,
        val original_title: String?,
        val overview: String?,
        val poster_path: String?,
        val vote_average: Float?
    )
}

data class TrendingMovies(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("total_pages")
    val total_pages: Int? = null,
    @SerializedName("total_results")
    val total_results: Int? = null,
    @SerializedName("results")
    val results: List<MovieDetail>? = null
)

data class VideoPlayAbleLink(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("results")
    val results: List<PlayAbleLink>? = null
)

data class PlayAbleLink(
    @SerializedName("iso_639_1")
    val iso_639_1: String? = null,
    @SerializedName("iso_3166_1")
    val iso_3166_1: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("key")
    val key: String? = null,
    @SerializedName("site")
    val site: String? = null,
    @SerializedName("size")
    val size: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("official")
    val official: Boolean? = null,
    @SerializedName("published_at")
    val published_at: String? = null,
    @SerializedName("id")
    val id: String? = null
)
