package ir.mehdi.imposter.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object GameSetup : Screen("game_setup")
    data object PlayerCard : Screen("player_card")
    data object DiscussionStart : Screen("discussion_start")
    data object GameFinished : Screen("game_finished")
    data object Rules : Screen("rules")
    data object About : Screen("about")
    data object Settings : Screen("settings")
}
