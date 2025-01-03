package com.core.common.constants

object MainFeature{
    const val nestedRoute = "main_feature"
    const val mainScreenRoute = "main_screen"
}
object AuthFeature{
    const val nestedRoute = "auth_feature"
    const val loginScreen = "login_screen"
    const val registerScreen = "register_screen"
}
object ProfileFeature{
    const val nestedRoute = "profile_feature"
    const val profileScreen = "profile_screen"
    const val profileScreenDeepLink = "podcastapp://profile_screen_deep_link"
    const val profileEditScreen = "profile_edit_screen"
}
object PodcastDetailedFeature {
    const val nestedRoute = "podcast_detailed_feature"
    const val podcastScreen = "podcast_screen"
}
object EpisodeDetailedFeature {
    const val nestedRoute = "episode_detailed_feature"
    const val episodeScreen = "episode_screen"
}
object PlayerFeature {
    const val nestedRoute = "player_full_screen_feature"
    const val playerScreen = "player_screen"
    const val playerWithIdScreen = "player_screen/{episode_id}"
    const val playerScreenDeepLink = "podcastapp://player_screen_deep_link"
}

object SearchFeature {
    const val nestedRoute = "search_feature"
    const val searchScreen = "search_screen"
}