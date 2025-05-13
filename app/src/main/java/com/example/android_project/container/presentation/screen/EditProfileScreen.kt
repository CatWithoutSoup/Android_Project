package com.example.android_project.container.presentation.screen

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.android_project.ReminderReceiver
import com.example.android_project.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val activity = context as Activity

    val fullName by viewModel.fullName.collectAsState()
    val position by viewModel.position.collectAsState()
    val email by viewModel.email.collectAsState()
    val deckurl by viewModel.deckUrl.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val favoriteTime by viewModel.favoriteTime.collectAsState()
    val favoriteTimeError by viewModel.favoriteTimeError.collectAsState()
    val showTimePicker = remember { mutableStateOf(false) }
    val canSave = favoriteTimeError == null
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }



    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri.value = it
            viewModel.onProfileImageChanged(it.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let {
                viewModel.onProfileImageChanged(it.toString())
            }
        }
    }

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val cameraPermission = Manifest.permission.CAMERA

    var hasStoragePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED
        )
    }

    var showStorageDialog by remember { mutableStateOf(!hasStoragePermission) }
    var showCameraDialog by remember { mutableStateOf(!hasCameraPermission) }
    var showOpenSettingsDialog by remember { mutableStateOf(false) }

    fun shouldShowRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        showStorageDialog = false
        if (granted) {
            hasStoragePermission = true
            showCameraDialog = !hasCameraPermission
        } else {
            if (!shouldShowRationale(storagePermission)) {
                showOpenSettingsDialog = true
            } else {
                navController.popBackStack()
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        showCameraDialog = false
        if (granted) {
            hasCameraPermission = true
        } else {
            if (!shouldShowRationale(cameraPermission)) {
                showOpenSettingsDialog = true
            } else {
                navController.popBackStack()
            }
        }
    }


    if (showTimePicker.value) {
        val timeParts = favoriteTime.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 12
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

        TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                val time = "%02d:%02d".format(selectedHour, selectedMinute)
                viewModel.onFavoriteTimeChanged(time)
                showTimePicker.value = false
            },
            hour, minute, true
        ).show()
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun scheduleReminder(context: Context, calendar: Calendar, name: String) {
        Log.d("Reminder", "Alarm set for: ${calendar.time}")
        Log.d("Reminder", "Scheduling reminder for $name")
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("name", name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Нет разрешения на точные уведомления", Toast.LENGTH_LONG).show()
                return
            }
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    when {
        showStorageDialog -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Доступ к файлам") },
                text = { Text("Для загрузки фото профиля требуется доступ к файлам устройства.") },
                confirmButton = {
                    TextButton(onClick = { storagePermissionLauncher.launch(storagePermission) }) {
                        Text("Разрешить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showStorageDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Отмена")
                    }
                }
            )
            return
        }

        showCameraDialog -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Доступ к камере") },
                text = { Text("Для съемки фото требуется разрешение на камеру.") },
                confirmButton = {
                    TextButton(onClick = { cameraPermissionLauncher.launch(cameraPermission) }) {
                        Text("Разрешить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCameraDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Отмена")
                    }
                }
            )
            return
        }

        showOpenSettingsDialog -> {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Открыть настройки") },
                text = { Text("Вы запретили доступ. Разрешите вручную в настройках приложения.") },
                confirmButton = {
                    TextButton(onClick = {
                        showOpenSettingsDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }) {
                        Text("Открыть настройки")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showOpenSettingsDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Отмена")
                    }
                }
            )
            return
        }
    }


    var showImageSourceDialog by remember { mutableStateOf(false) }
    Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Редактирование профиля") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    actions = {
                        TextButton(onClick = {
                            viewModel.saveProfileData(fullName, position, email)
                            navController.popBackStack()
                            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val date = formatter.parse(favoriteTime)
                            val calendar = Calendar.getInstance().apply {
                                time = date!!
                                set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
                                set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
                                set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                                if (before(Calendar.getInstance())) {
                                    add(Calendar.DAY_OF_MONTH, 1)
                                }
                            }
                            scheduleReminder(
                                context = context,
                                calendar = calendar,
                                name = fullName)
                        },
                            enabled = canSave) {
                            Text("Сохранить")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Фото профиля",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { showImageSourceDialog = true },
                    contentScale = ContentScale.Crop
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { viewModel.onFullNameChanged(it) },
                    label = { Text("ФИО") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = position,
                    onValueChange = { viewModel.onPositionChanged(it) },
                    label = { Text("Должность") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("Электронная почта") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    )
                )
                OutlinedTextField(
                    value = deckurl,
                    onValueChange = { viewModel.onDeckUrlChanged(it) },
                    label = { Text("Ссылка на колоду") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = favoriteTime,
                    onValueChange = { viewModel.onFavoriteTimeChanged(it) },
                    label = { Text("Время любимой пары") },
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker.value = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Выбрать время")
                        }
                    },
                    isError = favoriteTimeError != null,
                    supportingText = favoriteTimeError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
                )
            }
        }

    fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }

    fun takePhotoFromCamera() {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File(context.cacheDir, "camera_image.jpg").apply { createNewFile() }
        )
        cameraImageUri.value = uri
        cameraLauncher.launch(uri)
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Выберите источник изображения") },
            text = { Text("Откуда вы хотите загрузить фото?") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    takePhotoFromCamera()
                }) {
                    Text("Камера")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    pickFromGallery()
                }) {
                    Text("Галерея")
                }
            }
        )
    }

}


