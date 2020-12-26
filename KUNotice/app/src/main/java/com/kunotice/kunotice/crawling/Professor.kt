package com.kunotice.kunotice.crawling

data class Professor(
    val name: String,
//    val major: String,
    val office: String,
    val tel: String,
    val email: String
) : Comparable<Professor> {
    override fun compareTo(other: Professor): Int {
        return this.name.compareTo(other.name)
    }

}