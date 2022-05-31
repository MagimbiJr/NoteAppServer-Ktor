package com.tana.repository

import com.tana.data.model.note.Note
import com.tana.data.model.user.User
import com.tana.data.table.NoteTable
import com.tana.data.table.UserTable
import com.tana.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NoteServerDao {

    /** <---------------- User -----------------> **/

    private fun rowToUser(row: ResultRow): User = User(
        email = row[UserTable.email],
        hashPassword = row[UserTable.hashPassword],
        name = row[UserTable.name]
    )

    suspend fun addUser(user: User) = dbQuery {
        UserTable.insert { userTable ->
            userTable[email] = user.email
            userTable[hashPassword] = user.hashPassword
            userTable[name] = user.name
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }


    /** <---------------- Note -----------------> **/

    private fun rowToNote(row: ResultRow): Note = Note(
        id = row[NoteTable.id],
        noteTitle = row[NoteTable.noteTitle],
        noteDescription = row[NoteTable.noteDescription],
        date = row[NoteTable.date]
    )

    suspend fun getNotes(email: String) = dbQuery {
        NoteTable.select { NoteTable.email.eq(email) }
            .mapNotNull { rowToNote(it) }
    }

    suspend fun addNote(note: Note, email: String) = dbQuery {
        NoteTable.insert { noteTable ->
            noteTable[id] = note.id
            noteTable[NoteTable.email] = email
            noteTable[noteTitle] = note.noteTitle
            noteTable[noteDescription] = note.noteDescription
            noteTable[date] = note.date
        }
    }
    suspend fun editNote(note: Note, email: String) = dbQuery {
        NoteTable.update({ NoteTable.id.eq(note.id) and NoteTable.email.eq(email) }) { noteTable ->
            noteTable[noteTitle] = note.noteTitle
            noteTable[noteDescription] = note.noteDescription
            noteTable[date] = note.date
        }
    }

    suspend fun deleteNote(id: String, email: String) = dbQuery {
        NoteTable.deleteWhere { NoteTable.id.eq(id) and NoteTable.email.eq(email) }
    }


}