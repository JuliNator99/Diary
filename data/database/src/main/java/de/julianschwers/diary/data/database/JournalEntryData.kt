package de.julianschwers.diary.data.database

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
interface JournalEntryData {
    var text: String?
    var moodUid: String?
    var timeMillis: Long
    var attachmentsList: String?
    
    var createdMillis: Long
    var uid: String
}

fun JournalEntryData.copyImpl(): JournalEntryDataImpl =
    JournalEntryDataImpl(
        text = text,
        moodUid = moodUid,
        timeMillis = timeMillis,
        createdMillis = createdMillis,
        uid = uid
    )

data class JournalEntryDataImpl @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    override var text: String? = null,
    override var moodUid: String? = null,
    override var timeMillis: Long = Clock.System.now().toEpochMilliseconds(),
    override var attachmentsList: String? = null,
    
    override var createdMillis: Long = Clock.System.now().toEpochMilliseconds(),
    override var uid: String = Uuid.random().toString(),
) : JournalEntryData


fun JournalEntryData.copyAsImpl(): JournalEntryDataImpl = copy(JournalEntryDataImpl())
fun <T : JournalEntryData> JournalEntryData.copy(destination: T): T = destination.apply {
    this@apply.text = this@copy.text
    this@apply.moodUid = this@copy.moodUid
    this@apply.timeMillis = this@copy.timeMillis
    this@apply.attachmentsList = this@copy.attachmentsList
    
    this@apply.createdMillis = this@copy.createdMillis
    this@apply.uid = this@copy.uid
}