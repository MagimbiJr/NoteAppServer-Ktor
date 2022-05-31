package com.tana.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {

    val id = varchar("id", 512)
    val email = varchar("email", 512).references(UserTable.email)
    val noteTitle = text("noteTitle")
    val noteDescription = text("noteDescription")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}