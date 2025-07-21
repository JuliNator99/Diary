package de.julianschwers.diary.feature.journal

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import de.julianschwers.diary.core.model.JournalEntry
import de.julianschwers.diary.core.common.NavigationArgumentRegister
import de.julianschwers.diary.data.repository.JournalRepository

internal val navArgRegister = NavigationArgumentRegister<JournalEntry>()

const val ROUTE_JOURNAL_EDITOR = "editor/journal"
private const val ARG_JOURNAL_EDITOR = "jid"

fun NavController.navigateToJournalEditor(storedJournalUid: String) = navigate(route = "$ROUTE_JOURNAL_EDITOR/$storedJournalUid")
fun NavController.navigateToJournalEditor(default: JournalEntry) {
    val id = navArgRegister.store(default)
    navigate(route = "$ROUTE_JOURNAL_EDITOR/$id")
}

fun NavGraphBuilder.journalEditor(
    repository: JournalRepository,
    navBack: () -> Unit,
) = composable(
    route = "$ROUTE_JOURNAL_EDITOR/{$ARG_JOURNAL_EDITOR}",
    arguments = listOf(
        navArgument(name = ARG_JOURNAL_EDITOR) { type = NavType.StringType },
    )
) {
    val uid = it.arguments?.getString(ARG_JOURNAL_EDITOR) ?: throw NullPointerException("No ID was supplied.")
    
    val vm = viewModel {
        JournalEditorVM(
            journalUid = uid,
            repository = repository
        )
    }
    
    val state by vm.state.collectAsStateWithLifecycle(JournalEditorState.Loading)
    
    state.let { state ->
        if (state !is JournalEditorState.Editor) return@let
        JournalEditor(
            state = state,
            onDiscard = navBack,
            onSave = { vm.save { navBack() } },
            change = vm::update
        )
    }
}