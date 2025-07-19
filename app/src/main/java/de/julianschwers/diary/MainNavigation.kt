package de.julianschwers.diary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.feature.journal.journalEditor
import de.julianschwers.diary.feature.journal.navigateToJournalEditor
import de.julianschwers.diary.feature.timeline.ROUTE_TIMELINE
import de.julianschwers.diary.feature.timeline.timeline
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun MainNavigationLayer(
    appState: AppState,
    appEnvironment: AppEnvironment,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = appState.mainNavController,
        startDestination = ROUTE_TIMELINE
    ) {
        timeline(
            repository = appEnvironment.journalRepository,
            onCreateNew = { appState.mainNavController.navigateToJournalEditor(JournalEntry()) },
            onEdit = { appState.mainNavController.navigateToJournalEditor(it.uid) },
            navBack = { appState.mainNavController.popBackStack() },
        )
        journalEditor(
            repository = appEnvironment.journalRepository,
            navBack = { appState.mainNavController.popBackStack() },
        )
    }
}