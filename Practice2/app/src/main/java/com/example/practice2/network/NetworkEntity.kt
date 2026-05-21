package com.example.practice2.network

import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("full_name")
    val name: String,
    @SerializedName("email_address")
    val email: String,
    val uuid: String,
    @SerializedName("photo_url_small")
    val smallUrl: String,
    @SerializedName("photo_url_large")
    val largeUrl: String,
    @SerializedName("employee_type")
    val type: EmployeeType,
)

enum class EmployeeType {
    @SerializedName("FULL_TIME")
    FullTime,

    @SerializedName("PART_TIME")
    PartTime,

    @SerializedName("CONTRACTOR")
    Contractor,

    Unknown;

    fun toUi(): String {
        return when(this){
            EmployeeType.FullTime -> "FullTime"
            EmployeeType.PartTime -> "TODO()"
            EmployeeType.Contractor -> "TODO"
            EmployeeType.Unknown -> "TODO()"
        }
    }

}

data class EmployeesList(
    val employees: List<Employee>
)