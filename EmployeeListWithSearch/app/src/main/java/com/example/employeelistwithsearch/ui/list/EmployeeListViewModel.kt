package com.example.employeelistwithsearch.ui.list

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeelistwithsearch.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    val repository: EmployeeRepository
): ViewModel() {
    var _searchText = MutableStateFlow<String>("")
    val searchText =_searchText.asStateFlow()
    private  var _uiState = MutableStateFlow<UiState>(UiState.Loading)
      val uiState = combine(_uiState, _searchText) { state, text ->
          when(state) {
              is UiState.Success ->
                  UiState.Success(state.employees.filter { it.name.contains(text, ignoreCase = true) })
              else -> state
          }
          
      }.stateIn<UiState>(
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000),
          initialValue = UiState.Loading
      )

    init {
        fetchEmployees()
    }

    fun fetchEmployees() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getEmployees()
                .onSuccess { result ->


                    _uiState.value = UiState.Success(
                        result
                    )
                }
                .onFailure {
                    _uiState.value = if(it.message == "Empty"){
                        UiState.Empty
                    } else {
                        UiState.Error(it.message ?: "Error in fetching employees")
                    }
                }
        }
    }
    
    fun updateSearch(text: String){
        _searchText.value = text
    }

}