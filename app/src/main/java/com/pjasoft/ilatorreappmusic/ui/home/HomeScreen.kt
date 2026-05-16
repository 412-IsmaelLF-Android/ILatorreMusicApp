package com.pjasoft.ilatorreappmusic.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pjasoft.ilatorreappmusic.data.model.Album
import com.pjasoft.ilatorreappmusic.data.repository.MusicRepository
import com.pjasoft.ilatorreappmusic.ui.components.AlbumCarouselCard
import com.pjasoft.ilatorreappmusic.ui.components.MiniPlayer
import com.pjasoft.ilatorreappmusic.ui.components.RecentlyPlayedItem
import com.pjasoft.ilatorrefappmusic.ui.theme.DeepPurple
import com.pjasoft.ilatorrefappmusic.ui.theme.LightBackground
import com.pjasoft.ilatorrefappmusic.ui.theme.PrimaryPurple
import kotlinx.coroutines.launch
import com.pjasoft.ilatorreappmusic.data.repository.Result
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAlbumClick: (String) -> Unit
) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var miniPlayerAlbum by remember { mutableStateOf<Album?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            when (val result = MusicRepository.getAlbums()) {
                is Result.Success -> {
                    albums = result.data
                    miniPlayerAlbum = result.data.firstOrNull()
                    errorMessage = null
                }
                is Result.Error -> {
                    errorMessage = result.message
                }
                is Result.Loading -> {}
            }
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PrimaryPurple, DeepPurple)
                            ),
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Good Morning!",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Ismael Latorre",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }
                }
            }

            // Albums section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Albums",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A2E)
                    )
                    TextButton(onClick = {}) {
                        Text(
                            text = "See more",
                            color = PrimaryPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Albums LazyRow
            item {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryPurple)
                        }
                    }
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error loading albums",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        scope.launch {
                                            isLoading = true
                                            errorMessage = null
                                            when (val result = MusicRepository.getAlbums()) {
                                                is Result.Success -> {
                                                    albums = result.data
                                                    miniPlayerAlbum = result.data.firstOrNull()
                                                }
                                                is Result.Error -> errorMessage = result.message
                                                else -> {}
                                            }
                                            isLoading = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    else -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(albums) { album ->
                                AlbumCarouselCard(
                                    album = album,
                                    onClick = {
                                        miniPlayerAlbum = album
                                        onAlbumClick(album.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Recently Played section header
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recently Played",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A2E)
                    )
                    TextButton(onClick = {}) {
                        Text(
                            text = "See more",
                            color = PrimaryPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Recently Played LazyColumn items (nested inside outer LazyColumn as individual items)
            if (!isLoading && errorMessage == null) {
                items(albums) { album ->
                    RecentlyPlayedItem(
                        album = album,
                        onClick = {
                            miniPlayerAlbum = album
                            onAlbumClick(album.id)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // Mini Player pinned at the bottom
        MiniPlayer(
            album = miniPlayerAlbum,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}
