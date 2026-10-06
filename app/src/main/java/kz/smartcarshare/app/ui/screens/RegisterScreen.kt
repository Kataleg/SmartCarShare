package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.ui.components.IconBadge
import kz.smartcarshare.app.ui.components.PrimaryButton
import kz.smartcarshare.app.ui.theme.*
import kz.smartcarshare.app.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.lang

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            onRegisterSuccess()
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Surface1,
        unfocusedContainerColor = Surface1,
        disabledContainerColor = Surface1,
        focusedBorderColor = Ice,
        unfocusedBorderColor = BorderColor,
        focusedLabelColor = Ice,
        unfocusedLabelColor = TextMid,
        focusedTextColor = TextHi,
        unfocusedTextColor = TextHi
    )

    Box(modifier = Modifier.fillMaxSize().background(Navy800)) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Surface1)
                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                .clickable { viewModel.toggleLanguage() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Filled.Language, contentDescription = null, tint = Ice, modifier = Modifier.size(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "ҚАЗ",
                    color = if (lang == Lang.KZ) Amber else TextMid,
                    fontWeight = if (lang == Lang.KZ) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall
                )
                Text("/", color = TextLow, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "РУС",
                    color = if (lang == Lang.RU) Amber else TextMid,
                    fontWeight = if (lang == Lang.RU) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconBadge(
                icon = Icons.Filled.DirectionsCar,
                bg = Ice,
                tint = Navy900,
                sizeDp = 64
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Strings.get(lang, "app_title"),
                style = MaterialTheme.typography.headlineMedium,
                color = TextHi,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (state.isLoginMode) Strings.get(lang, "login_title") else Strings.get(lang, "register_title"),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMid,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            if (!state.isLoginMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.firstName,
                        onValueChange = viewModel::onFirstNameChange,
                        label = { Text(Strings.get(lang, "first_name")) },
                        shape = RoundedCornerShape(14.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 12.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.lastName,
                        onValueChange = viewModel::onLastNameChange,
                        label = { Text(Strings.get(lang, "last_name")) },
                        shape = RoundedCornerShape(14.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 12.dp),
                        singleLine = true
                    )
                }
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(Strings.get(lang, "email")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )

            if (!state.isLoginMode) {
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = viewModel::onPhoneChange,
                    label = { Text(Strings.get(lang, "phone")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text(Strings.get(lang, "password")) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            if (state.isLoginMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = Strings.get(lang, "forgot_password"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Ice,
                        modifier = Modifier
                            .clickable { viewModel.showForgotPasswordDialog(true) }
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    )
                }
            }

            state.forgotPasswordSuccessMessage?.let { success ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(Success.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, Success, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = success,
                        color = Success,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            state.errorMessage?.let { error ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            PrimaryButton(
                text = if (state.isLoading) {
                    "..."
                } else {
                    if (state.isLoginMode) Strings.get(lang, "login_btn") else Strings.get(lang, "register_btn")
                },
                enabled = !state.isLoading,
                onClick = { viewModel.registerOrLogin() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clickable { viewModel.toggleMode() }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (state.isLoginMode) Strings.get(lang, "no_account") else Strings.get(lang, "has_account"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMid
                )
                Text(
                    text = if (state.isLoginMode) Strings.get(lang, "register_btn") else Strings.get(lang, "login_btn"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Ice,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (state.isOtpDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.showOtpDialog(false) },
            containerColor = Surface1,
            title = {
                Text(if (lang == Lang.KZ) "Поштаны немесе телефонды растау" else "Подтверждение почты или телефона", color = TextHi)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        if (lang == Lang.KZ) "Тіркелуді аяқтау үшін (${state.email}) поштасына жіберілген 4 таңбалы кодты енгізіңіз.\n(Тест коды: ${state.generatedOtp})" else "Введите 4-значный код подтверждения, отправленный на почту (${state.email}).\n(Код для теста: ${state.generatedOtp})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMid
                    )
                    OutlinedTextField(
                        value = state.enteredOtp,
                        onValueChange = viewModel::onOtpChange,
                        label = { Text(if (lang == Lang.KZ) "Растау коды (4 сан)" else "Код подтверждения (4 цифры)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.verifyAndCompleteRegistration() },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(if (lang == Lang.KZ) "Растау және кіру" else "Подтвердить и войти", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showOtpDialog(false) }) {
                    Text(Strings.get(lang, "cancel"), color = TextMid)
                }
            }
        )
    }

    if (state.isForgotPasswordDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.showForgotPasswordDialog(false) },
            containerColor = Surface1,
            title = {
                Text(Strings.get(lang, "forgot_password"), color = TextHi)
            },
            text = {
                Column {
                    Text(
                        if (lang == Lang.KZ) "Поштаңызға құпия сөзді жаңарту сілтемесі жіберіледі. Email мекенжайыңызды енгізіңіз:" else "Ссылка для сброса пароля будет отправлена на почту. Введите ваш Email:",
                        color = TextMid,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.sendPasswordReset() },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text(Strings.get(lang, "send"), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showForgotPasswordDialog(false) }) {
                    Text(Strings.get(lang, "cancel"), color = TextMid)
                }
            }
        )
    }
}
