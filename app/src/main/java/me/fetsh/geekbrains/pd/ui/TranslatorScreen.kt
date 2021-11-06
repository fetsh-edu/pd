package me.fetsh.geekbrains.pd.ui

enum class TranslatorScreen(
) {
   Search,
   Details;

   companion object {
      fun fromRoute(route: String?): TranslatorScreen =
         when (route?.substringBefore("/")) {
            Search.name -> Search
            Details.name -> Details
            null -> Search
            else -> throw IllegalArgumentException("Route $route is not recognized.")
         }
   }
}