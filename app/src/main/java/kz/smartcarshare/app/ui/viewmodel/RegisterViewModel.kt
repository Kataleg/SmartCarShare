package kz.smartcarshare.app.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.data.repository.AuthRepository

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val phone: String = "",
    val isLoginMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistered: Boolean = false,
    val currentUser: LocalUser? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _uiState = MutableStateFlow(
        RegisterUiState(currentUser = repository.getCurrentUser())
    )
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value, errorMessage = null)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value, errorMessage = null)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(
            isLoginMode = !_uiState.value.isLoginMode,
            errorMessage = null
        )
    }

    fun registerOrLogin() {
        val state = _uiState.value

        val validationError = validate(state)
        if (validationError != null) {
            _uiState.value = state.copy(errorMessage = validationError)
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = if (state.isLoginMode) {
                repository.login(
                    email = state.email.trim(),
                    password = state.password
                )
            } else {
                repository.register(
                    email = state.email.trim(),
                    password = state.password,
                    name = state.name.trim(),
                    phone = state.phone.trim()
                )
            }

            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true,
                    currentUser = user
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Қате орын алды"
                )
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = RegisterUiState(
            currentUser = null,
            isRegistered = false
        )
    }

    fun refreshCurrentUser() {
        _uiState.value = _uiState.value.copy(
            currentUser = repository.getCurrentUser()
        )
    }

    private fun validate(state: RegisterUiState): String? {
        if (!state.isLoginMode && state.name.isBlank()) return "Аты-жөніңізді енгізіңіз"
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            return "Дұрыс email енгізіңіз"
        }
        if (state.password.length < 6) return "Құпия сөз кемінде 6 таңбадан тұруы керек"
        if (!state.isLoginMode && state.phone.isBlank()) return "Телефон нөмірін енгізіңіз"
        return null
    }
}
