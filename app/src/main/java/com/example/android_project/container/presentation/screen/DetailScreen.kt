package com.example.android_project.container.presentation.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.android_project.data.local.FavoriteDatabase
import com.example.android_project.data.local.FavoritePokemonCard
import com.example.android_project.extractTypeWords
import com.example.android_project.typeIconMap
import com.example.android_project.viewmodel.PokemonViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cardId: String,
    navController: NavController,
    viewModel: PokemonViewModel = viewModel( factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val card by viewModel.selectedCard.collectAsState()
    val weakness = extractTypeWords(card?.weaknesses.orEmpty())
    val retreat = extractTypeWords(card?.retreatCost.orEmpty())
    val pokemonType = card?.types.orEmpty()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isFavorite = remember { mutableStateOf(false) }


    LaunchedEffect(cardId) {
        viewModel.loadCardById(cardId)
        isFavorite.value = FavoriteDatabase.getDatabase(context)
            .favoriteDao()
            .isFavorite(cardId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Информация") },
                actions = {
                    IconButton(onClick = {
                        card?.let {
                            scope.launch {
                                val dao = FavoriteDatabase.getDatabase(context).favoriteDao()

                                val favoriteEntity = FavoritePokemonCard(
                                    id = it.id,
                                    name = it.name,
                                    subtypes = it.subtypes.joinToString(","),
                                    types = it.types.joinToString(","),
                                    hp = it.hp,
                                    weaknesses = it.weaknesses.mapNotNull { map -> map["type"] }.joinToString(","),
                                    retreatCost = it.retreatCost.joinToString(","),
                                    imageSmall = it.images.small,
                                    imageLarge = it.images.large,
                                    series = it.set.series,
                                    attackName = it.attacks.getOrNull(0)?.name.orEmpty(),
                                    attackCost = it.attacks.getOrNull(0)?.cost?.joinToString(",").orEmpty(),
                                    attackDamage = it.attacks.getOrNull(0)?.damage.orEmpty(),
                                    attackText = it.attacks.getOrNull(0)?.text.orEmpty()
                                )
                                if (isFavorite.value) {
                                    dao.delete(favoriteEntity)
                                } else {
                                    dao.insert(favoriteEntity)
                                }
                                isFavorite.value = !isFavorite.value
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (isFavorite.value) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Избранное"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = card?.images?.large,
                contentDescription = "Pokemon image",
                modifier = Modifier.fillMaxSize()
            )
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(4.dp, Color.LightGray, shape = RoundedCornerShape(12.dp))
            ) {
                val (nameText, statsColumn, infoBox) = createRefs()
                Text(
                    text = card?.name.orEmpty(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .constrainAs(nameText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(20.dp)
                )
                Column(
                    modifier = Modifier.constrainAs(statsColumn) {
                        top.linkTo(nameText.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    StatRowWithText("Pokemon", card?.subtypes.toString().removePrefix("[").removeSuffix("]"))
                    StatRowWithImage("Type", pokemonType)
                    StatRowWithText("HP", card?.hp.orEmpty())
                    StatRowWithImage("Weakness", weakness)
                    StatRowWithImage("Retreat", retreat)
                    StatRowWithText("Series", card?.set?.series.orEmpty())
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(2.dp, Color(0x10000000), shape = RoundedCornerShape(12.dp))
                        .constrainAs(infoBox) {
                            top.linkTo(statsColumn.bottom, margin = 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    ) {
                        card?.attacks?.getOrNull(0)?.cost.orEmpty().forEach { type ->
                            typeIconMap[type]?.let { iconUrl ->
                                AsyncImage(
                                    model = iconUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = card?.attacks?.getOrNull(0)?.name.orEmpty(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = card?.attacks?.getOrNull(0)?.text.orEmpty(),
                        modifier = Modifier
                            .padding(start = 25.dp, end = 20.dp, top = 60.dp),
                        fontSize = 14.sp
                    )
                    Text(
                        text = card?.attacks?.getOrNull(0)?.damage.orEmpty(),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(start = 20.dp, end = 20.dp, top = 25.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StatRowWithText(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .height(40.dp)
            .border(2.dp, Color(0x10000000), shape = RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(Color(0x15000000)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = label, modifier = Modifier.padding(start = 8.dp))
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun StatRowWithImage(statName: String, types: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .height(40.dp)
            .border(2.dp, Color(0x10000000), shape = RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(Color(0x15000000)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = statName, modifier = Modifier.padding(8.dp))
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                types.forEach { type ->
                    typeIconMap[type]?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "$type icon",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}


