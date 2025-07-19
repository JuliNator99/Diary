package de.julianschwers.diary.feature.timeline

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.data.repository.JournalRepository

const val ROUTE_TIMELINE = "timeline"

fun NavController.navigateToTimeline(storedJournalUid: String) = navigate(route = ROUTE_TIMELINE)

fun NavGraphBuilder.timeline(
    repository: JournalRepository,
    onCreateNew: () -> Unit,
    onEdit: (JournalEntry) -> Unit,
    navBack: () -> Unit,
) = composable(route = ROUTE_TIMELINE) {
    val vm = viewModel {
        TimelineVM(repository = repository)
    }
    
    val state by vm.state.collectAsStateWithLifecycle()
    
    TimelineScreen(
        state = state,
        onCreateNew = onCreateNew,
        onDelete = vm::delete,
        onEdit = onEdit
    )
}