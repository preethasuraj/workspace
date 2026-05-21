package com.example.employeelistwithsearch.ui.list

import com.example.employeelistwithsearch.ui.EmployeeUIEntity

sealed class UiState {
    data object Loading: UiState()
    data object Empty: UiState()
    data class Error(val message: String): UiState()
    data class Success(val employees: List<EmployeeUIEntity>): UiState()
}