package kz.smartcarshare.app.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.data.repository.AuthRepository
import kz.smartcarshare.app.data.repository.EmailSender

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val isLoginMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistered: Boolean = false,
    val currentUser: LocalUser? = null,
    val isVerificationEmailSent: Boolean = false,
    val isForgotPasswordDialogVisible: Boolean = false,
    val forgotPasswordSuccessMessage: String? = null,
    val isEditProfileDialogVisible: Boolean = false,
    val profileUpdateSuccessMessage: String? = null,
    val isOtpDialogVisible: Boolean = false,
    val enteredOtp: String = "",
    val generatedOtp: String = "7788",
    val lang: Lang = Lang.KZ
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    val currentLang: Lang
        get() = _uiState.value.lang

    fun setLanguage(lang: Lang) {
        _uiState.value = _uiState.value.copy(lang = lang)
    }

    fun toggleLanguage() {
        val newLang = if (_uiState.value.lang == Lang.KZ) Lang.RU else Lang.KZ
        _uiState.value = _uiState.value.copy(lang = newLang)
    }

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

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value, errorMessage = null)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value, errorMessage = null)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value, errorMessage = null)
    }

    fun onOtpChange(value: String) {
        _uiState.value = _uiState.value.copy(enteredOtp = value, errorMessage = null)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(
            isLoginMode = !_uiState.value.isLoginMode,
            errorMessage = null,
            isVerificationEmailSent = false,
            forgotPasswordSuccessMessage = null,
            isOtpDialogVisible = false
        )
    }

    fun showForgotPasswordDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            isForgotPasswordDialogVisible = show,
            forgotPasswordSuccessMessage = null,
            errorMessage = null
        )
    }

    fun showOtpDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            isOtpDialogVisible = show,
            errorMessage = null
        )
    }

    fun updateProfile(firstName: String, lastName: String, phone: String, email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        val fullName = "$firstName $lastName".trim()
        val lang = _uiState.value.lang

        viewModelScope.launch {
            val result = repository.updateUserProfile(
                name = fullName,
                phone = phone,
                email = email
            )
            result.onSuccess { updatedUser ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEditProfileDialogVisible = false,
                    currentUser = updatedUser,
                    profileUpdateSuccessMessage = if (lang == Lang.KZ) "Профиль сәтті жаңартылды" else "Профиль успешно обновлен"
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: (if (lang == Lang.KZ) "Профильді жаңарту сәтсіз аяқталды" else "Не удалось обновить профиль")
                )
            }
        }
    }

    fun sendPasswordReset() {
        val state = _uiState.value
        val email = state.email.trim()
        val lang = state.lang
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = state.copy(errorMessage = if (lang == Lang.KZ) "Дұрыс email енгізіңіз" else "Введите корректный email")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.sendPasswordResetEmail(email)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isForgotPasswordDialogVisible = false,
                    forgotPasswordSuccessMessage = if (lang == Lang.KZ) "Құпия сөзді қалпына келтіру сілтемесі $email поштасына жіберілді" else "Ссылка для сброса пароля отправлена на почту $email"
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: (if (lang == Lang.KZ) "Құпия сөзді қалпына келтіру хаты жіберілмеді" else "Не удалось отправить письмо сброса пароля")
                )
            }
        }
    }

    fun registerOrLogin() {
        val state = _uiState.value

        val validationError = validate(state)
        if (validationError != null) {
            _uiState.value = state.copy(errorMessage = validationError)
            return
        }

        if (!state.isLoginMode) {
            val code = (1000..9999).random().toString()
            _uiState.value = state.copy(
                generatedOtp = code,
                isOtpDialogVisible = true,
                isLoading = true
            )
            viewModelScope.launch {
                try {
                    EmailSender.sendOtpEmail(state.email.trim(), code, state.firstName.trim())
                } catch (_: Exception) {}
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
            return
        }

        executeLogin(state)
    }

    private fun executeLogin(state: RegisterUiState) {
        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.login(
                email = state.email.trim(),
                password = state.password
            )
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true,
                    currentUser = user
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: (if (state.lang == Lang.KZ) "Қате орын алды" else "Произошла ошибка")
                )
            }
        }
    }

    fun verifyAndCompleteRegistration() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, isOtpDialogVisible = false, errorMessage = null)
        val fullName = "${state.firstName.trim()} ${state.lastName.trim()}".trim()

        viewModelScope.launch {
            val result = repository.register(
                email = state.email.trim(),
                password = state.password,
                name = fullName,
                phone = state.phone.trim()
            )

            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true,
                    currentUser = user,
                    isVerificationEmailSent = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true,
                    currentUser = LocalUser(state.email, "", fullName, state.phone),
                    isVerificationEmailSent = true
                )
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = RegisterUiState(
            currentUser = null,
            isRegistered = false,
            lang = _uiState.value.lang
        )
    }

    private fun validate(state: RegisterUiState): String? {
        val lang = state.lang
        if (!state.isLoginMode && (state.firstName.isBlank() || state.lastName.isBlank())) {
            return if (lang == Lang.KZ) "Аты мен тегін толтырыңыз" else "Заполните имя и фамилию"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            return if (lang == Lang.KZ) "Дұрыс email енгізіңіз" else "Введите корректный email"
        }
        if (state.password.length < 6) {
            return if (lang == Lang.KZ) "Құпия сөз кемінде 6 таңбадан тұруы керек" else "Пароль должен быть не короче 6 символов"
        }
        if (!state.isLoginMode && state.phone.isBlank()) {
            return if (lang == Lang.KZ) "Телефон нөмірін енгізіңіз" else "Введите номер телефона"
        }
        return null
    }
}
