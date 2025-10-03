package id.stargan.jemputankudriver.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import id.stargan.jemputankudriver.feature.login.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onGoogleLogin: (() -> Unit)? = null,
    onNavigateToSignup: () -> Unit = {}
) {
    val viewModel: LoginViewModel = viewModel()
    val isLoadingState by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorMessage.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) onLoginSuccess()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Masuk ke Jemputanku",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) "Sembunyikan" else "Lihat"
                    TextButton(onClick = { passwordVisible = !passwordVisible }) { Text(icon) }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoadingState && email.isNotBlank() && password.isNotBlank()
            ) {
                if (isLoadingState) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { onGoogleLogin?.invoke() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoadingState
            ) {
                Text("Login dengan Google")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { onNavigateToSignup() }) {
                Text("Belum punya akun? Daftar")
            }
            if (errorState != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = errorState ?: "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
