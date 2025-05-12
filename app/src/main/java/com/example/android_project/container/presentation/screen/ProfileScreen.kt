package com.example.android_project.container.presentation.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.android_project.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {

    val fullName by viewModel.fullName.collectAsState()
    val position by viewModel.position.collectAsState()
    val email by viewModel.email.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("editProfile")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать профиль")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Фото пользователя через AsyncImage
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото профиля",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "ФИО", fontWeight = FontWeight.Bold)
            Text(text = fullName)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Должность", fontWeight = FontWeight.Bold)
            Text(text = position)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Email", fontWeight = FontWeight.Bold)
            Text(text = email)
            Spacer(modifier = Modifier.height(16.dp))

//            Text(text = "Ссылка на колоду", fontWeight = FontWeight.Bold)
//            Text(text = viewModel.deckUrl.collectAsState().value)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val url = viewModel.deckUrl.value
                if (url.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    ContextCompat.startActivity(context, intent, null)
                }
            }) {
                Text("Открыть резюме")
            }
        }
    }
}