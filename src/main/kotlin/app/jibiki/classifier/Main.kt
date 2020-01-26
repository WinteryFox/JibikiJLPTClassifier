package app.jibiki.classifier

import java.sql.Connection
import java.sql.DriverManager

class Main

fun main() {
    println("Enter the IP of your server")
    val ip = readLine()

    println("Enter the target database name")
    val database = readLine()

    println("Enter the username")
    val username = readLine()

    println("Enter the password")
    val password = readLine()

    Main::class.java.getResource("jlpt-n5.csv")

    val connection = DriverManager
        .getConnection("jdbc:postgresql://$ip/$database", username, password)

    connection
        .prepareStatement("ALTER TABLE IF EXISTS entr ADD COLUMN IF NOT EXISTS jlpt INTEGER DEFAULT NULL")
        .execute()

    for (i in 1..5) {
        readCsv(connection, "jlpt-n${i}.csv", i)
    }

    connection.close()
    println("Done!")
}

fun readCsv(connection: Connection, file: String, level: Int) {
    val statement = connection.createStatement()

    Main::class.java.getResource("/$file").readText().lines().forEach {
        if (it != "") {
            statement
                .addBatch("UPDATE entr SET jlpt = $level WHERE seq = $it")
        }
    }

    statement.executeBatch()
    statement.close()
}