package com.example.android_project.container.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.android_project.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController, viewModel: PokemonViewModel = viewModel()) {
    val cards by viewModel.cards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isFiltersDialogVisible by viewModel.isFiltersDialogVisible.collectAsState()
    val hasBadge by viewModel.hasBadge.collectAsState()
    val selectedFilters by viewModel.selectedFilters.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = {
                                viewModel.setSearchQuery(it)
                                viewModel.searchCardsByName(it)
                            },
                            placeholder = { Text("Поиск...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Список карточек", fontSize = 22.sp)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.setSearchQuery("")
                        viewModel.searchCardsByName("")
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }

                    BadgedBox(
                        badge = { if (hasBadge) Badge() },
                        modifier = Modifier.padding(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Фильтры",
                            modifier = Modifier
                                .clickable { viewModel.onFiltersClicked() }
                                .size(40.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    CircularProgressIndicator()
                }
            }

            cards.isEmpty() -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text("Кажется таких карт нет")
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(cards) { _, card ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.7f)
                                .clickable {
                                    navController.navigate("detail/${card.id}")
                                }
                        ) {
                            AsyncImage(
                                model = card.images.small,
                                contentDescription = "Pokemon image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        if (isFiltersDialogVisible) {
            ShowTypeDialog(
                selectedFilters = selectedFilters,
                onDismiss = { viewModel.onFiltersClicked() },
                onToggleFilter = { viewModel.toggleFilter(it) },
                onApply = { viewModel.applySelectedFilters() },
                isVisible = true
            )
        }
    }
}

@Composable
fun ShowTypeDialog(
    isVisible: Boolean,
    selectedFilters: Set<String>,
    onDismiss: () -> Unit,
    onToggleFilter: (String) -> Unit,
    onApply: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Выберите фильтры") },
            text = {
                Column {
                    val filterOptions = listOf("Fire", "Water", "Grass", "Metal", "Lightning", "Dragon", "Psychic", "Colorless", "Fighting", "Darkness", "Fairy")
                    filterOptions.forEach { filter ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedFilters.contains(filter),
                                onCheckedChange = { onToggleFilter(filter) }
                            )
                            Text(text = filter)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onApply) {
                    Text("Применить")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        )
    }
}




