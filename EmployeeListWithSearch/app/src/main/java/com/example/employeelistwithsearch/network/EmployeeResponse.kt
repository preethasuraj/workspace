package com.example.employeelistwithsearch.network

import com.example.employeelistwithsearch.ui.EmployeeUIEntity
import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
    val employees: List<Employee>,
)

data class Employee(
    @SerializedName("full_name")
    val name: String,
    val uuid: String,
    @SerializedName("photo_url_small")
    val smallUrl: String,
    @SerializedName("photo_url_large")
    val largeUrl: String,
)

fun Employee.toEmployeeUIEntity(): EmployeeUIEntity {
    return EmployeeUIEntity(
        name = this.name,
        uuid = this.uuid,
        smallUrl = this.smallUrl,
        largeUrl = this.largeUrl
    )
}