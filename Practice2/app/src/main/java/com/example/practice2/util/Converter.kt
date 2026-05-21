package com.example.practice2.util

import com.example.practice2.database.EmployeeEntity
import com.example.practice2.network.Employee
import com.example.practice2.network.EmployeeType

fun EmployeeEntity.toEmployee(): Employee = Employee(
    this.name,
    this.name,
    this.uuid,
    this.imageUrl,
    this.imageUrlLarge,
    EmployeeType.FullTime
)

fun Employee.toEmployeeEntity(): EmployeeEntity = EmployeeEntity(
    this.name,
    this.uuid,
    this.smallUrl,
    this.largeUrl,
    this.type.toUi()
)