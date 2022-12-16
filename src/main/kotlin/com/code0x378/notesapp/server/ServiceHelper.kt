package com.code0x378.notesapp.server

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
object ServiceHelper {
    private val dispatcher: CoroutineContext

    init {
        dispatcher = newFixedThreadPoolContext(5, "database-pool")
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ): T =
        withContext(dispatcher) {
            transaction { block() }
        }

    //    Skip thread pool...
    //        suspend fun <T> dbQuery(
    //                block: () -> T): T =
    //                transaction { block() }
}