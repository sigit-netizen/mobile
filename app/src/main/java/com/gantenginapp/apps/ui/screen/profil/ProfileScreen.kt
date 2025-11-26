// app/src/main/java/com/gantenginapp/apps/ui/screen/profil/ProfileScreen.kt
package com.gantenginapp.apps.ui.screen.profil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gantenginapp.apps.R
import com.gantenginapp.apps.ui.screen.profil.ProfileViewModel.*

@Composable
fun ProfileScreen(
    userId: Int,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val showDeleteConfirmation by viewModel.showDeleteConfirmation.collectAsState()
    val showSaveSuccess by viewModel.showSaveSuccess.collectAsState()

    // Validasi lokal
    val usernameError by viewModel.usernameError.collectAsState()
    val noHpError by viewModel.noHpError.collectAsState() // ✅ Ganti: phoneError -> noHpError
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    // ✅ Tambahkan visibility password
    val passwordVisible by viewModel.passwordVisible.collectAsState()

    LaunchedEffect(userId) {
        if (!isEditing) {
            viewModel.fetchUserProfile(userId)
        }
    }

    // Handle snackbar sukses
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(showSaveSuccess) {
        if (showSaveSuccess) {
            snackbarHostState.showSnackbar("Profil berhasil diperbarui!")
            viewModel.resetSaveSuccess()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error: $error", color = Color.Red)
        }
        return
    }

    // AlertDialog konfirmasi hapus akun
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelDeleteAccount() },
            title = { Text("Konfirmasi Hapus Akun") },
            text = { Text("Apakah Anda yakin ingin menghapus akun secara permanen? Aksi ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.confirmDeleteAccount(userId) },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelDeleteAccount() }) {
                    Text("Batal")
                }
            }
        )
    }

    ProfileScreenContent(
        userProfile = userProfile,
        isEditing = isEditing,
        usernameError = usernameError,
        noHpError = noHpError, // ✅ Ganti: phoneError -> noHpError
        emailError = emailError,
        passwordError = passwordError,
        passwordVisible = passwordVisible,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onEditProfileClick = onEditProfileClick,
        onDeleteAccountClick = { viewModel.deleteAccount(userId) },
        onSaveClick = { viewModel.saveProfile() },
        onCancelClick = { viewModel.cancelEditing() },
        onUsernameChange = { viewModel.updateUsername(it) },
        onNoHpChange = { viewModel.updateNoHp(it) }, // ✅ Ganti: onPhoneChange -> onNoHpChange
        onEmailChange = { viewModel.updateEmail(it) },
        onPasswordChange = { viewModel.updatePassword(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() }
    )
}

@Composable
fun ProfileScreenContent(
    userProfile: UserProfile,
    isEditing: Boolean,
    usernameError: String?,
    noHpError: String?, // ✅ Ganti: phoneError -> noHpError
    emailError: String?,
    passwordError: String?,
    passwordVisible: Boolean,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onNoHpChange: (String) -> Unit, // ✅ Ganti: onPhoneChange -> onNoHpChange
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                if (!isEditing) {
                    IconButton(onClick = onEditProfileClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                } else {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.gantengin),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = userProfile.username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = usernameError != null,
                    supportingText = {
                        if (usernameError != null) {
                            Text(
                                text = usernameError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = userProfile.noHp, // ✅ Ganti: phone -> noHp
                    onValueChange = onNoHpChange, // ✅ Ganti: onPhoneChange -> onNoHpChange
                    label = { Text("No. HP") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = noHpError != null, // ✅ Ganti: phoneError -> noHpError
                    supportingText = {
                        if (noHpError != null) { // ✅ Ganti: phoneError -> noHpError
                            Text(
                                text = noHpError, // ✅ Ganti: phoneError -> noHpError
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = userProfile.email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(
                                text = emailError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // ✅ Ganti OutlinedTextField untuk password dengan ikon mata
                OutlinedTextField(
                    value = userProfile.password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password (opsional)") },
                    // ✅ Visual transformation dinamis
                    visualTransformation = if (passwordVisible) {
                        androidx.compose.ui.text.input.VisualTransformation.None
                    } else {
                        androidx.compose.ui.text.input.PasswordVisualTransformation()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) {
                            Text(
                                text = passwordError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    // ✅ Tambahkan trailing icon (mata)
                    trailingIcon = {
                        val image = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        }
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = image,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                )
            } else {
                ProfileItem(icon = Icons.Default.Person, text = userProfile.username.ifEmpty { "-" })
                ProfileItem(icon = Icons.Default.Phone, text = userProfile.noHp.ifEmpty { "-" }) // ✅ Ganti: phone -> noHp
                ProfileItem(icon = Icons.Default.Email, text = userProfile.email.ifEmpty { "-" })
                ProfileItem(icon = Icons.Default.Lock, text = "••••••••")
                ProfileItem(icon = Icons.Default.VerifiedUser, text = userProfile.role.ifEmpty { "-" })
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isEditing) {
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simpan", color = Color.White, fontWeight = FontWeight.Medium)
                }
            } else {
                Button(
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hapus Akun", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun ProfileItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile_EditingWithError() {
    val dummy = UserProfile(
        id = 1,
        username = "fi",
        noHp = "123", // ✅ Ganti: phone -> noHp
        email = "invalid-email",
        password = "123",
        role = "user"
    )
    val snackbarHostState = remember { SnackbarHostState() }
    ProfileScreenContent(
        userProfile = dummy,
        isEditing = true,
        usernameError = "Username minimal 4 karakter",
        noHpError = "Nomor HP harus 10–13 digit", // ✅ Ganti: phoneError -> noHpError
        emailError = "Format email tidak valid",
        passwordError = "Password minimal 4 karakter dan harus mengandung huruf serta angka.",
        passwordVisible = false,
        snackbarHostState = snackbarHostState,
        onBackClick = {},
        onEditProfileClick = {},
        onDeleteAccountClick = {},
        onSaveClick = {},
        onCancelClick = {},
        onUsernameChange = {},
        onNoHpChange = {}, // ✅ Ganti: onPhoneChange -> onNoHpChange
        onEmailChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {}
    )
}