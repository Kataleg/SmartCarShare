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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy800)
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
            text = "Smart CarShare",
            style = MaterialTheme.typography.headlineMedium,
            color = TextHi,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (state.isLoginMode) "Жүйеге кіру" else "Жаңа тіркелгі жасау",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMid,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        if (!state.isLoginMode) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Аты-жөні") },
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
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
                label = { Text("Телефон нөмірі") },
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
            label = { Text("Құпия сөз") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = textFieldColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

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
                if (state.isLoginMode) "Кіруде..." else "Тіркелуде..."
            } else {
                if (state.isLoginMode) "Кіру" else "Тіркелу"
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
                text = if (state.isLoginMode) "Аккаунт жоқ па? " else "Аккаунт бар ма? ",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMid
            )
            Text(
                text = if (state.isLoginMode) "Тіркелу" else "Кіру",
                style = MaterialTheme.typography.bodyMedium,
                color = Ice,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
