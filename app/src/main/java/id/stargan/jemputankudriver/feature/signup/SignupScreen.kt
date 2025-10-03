package id.stargan.jemputankudriver.feature.signup

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
import id.stargan.jemputankudriver.core.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit = {},
    onGoogleSignup: (() -> Unit)? = null,
    onNavigateToLogin: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoadingState by authViewModel.isLoading.collectAsState()
    val errorState by authViewModel.errorMessage.collectAsState()
    val signupSuccess by authViewModel.loginSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(signupSuccess) {
        if (signupSuccess) onSignupSuccess()
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
                text = "Daftar Akun Baru",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Lengkap") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                onClick = { /* Call your email/password signup here if needed */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoadingState && name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            ) {
                if (isLoadingState) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text("Daftar")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { onGoogleSignup?.invoke() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoadingState
            ) {
                Text("Daftar dengan Google")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { onNavigateToLogin() }) {
                Text("Sudah punya akun? Masuk")
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
fun SignupScreenPreview() {
    SignupScreen()
}
