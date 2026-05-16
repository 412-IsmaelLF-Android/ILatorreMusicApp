package com.pjasoft.ilatorreappmusic.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.pjasoft.ilatorreappmusic.data.model.Album
import com.pjasoft.ilatorreappmusic.data.repository.MusicRepository
import com.pjasoft.ilatorreappmusic.ui.components.MiniPlayer
import com.pjasoft.ilatorreappmusic.ui.components.TrackItem
import com.pjasoft.ilatorrefappmusic.ui.theme.DeepPurple
import com.pjasoft.ilatorrefappmusic.ui.theme.LightBackground
import com.pjasoft.ilatorrefappmusic.ui.theme.OverlayPurple
import com.pjasoft.ilatorrefappmusic.ui.theme.PrimaryPurple
import kotlinx.coroutines.launch
import com.pjasoft.ilatorreappmusic.data.repository.Result

@Composable
fun DetailScreen(
    albumId: String,
    onBackClick: () -> Unit
) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(albumId) {
        scope.launch {
            isLoading = true
            when (val result = MusicRepository.getAlbumById(albumId)) {
                is Result.Success -> {
                    album = result.data
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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryPurple)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error loading album", color = Color.Red, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMessage ?: "", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    when (val r = MusicRepository.getAlbumById(albumId)) {
                                        is Result.Success -> album = r.data
                                        is Result.Error -> errorMessage = r.message
                                        else -> {}
                                    }
                                    isLoading = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Text("Retry")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onBackClick) {
                            Text("Go Back", color = PrimaryPurple)
                        }
                    }
                }
            }
            album != null -> {
                val currentAlbum = album!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(360.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(currentAlbum.image)
                                    .build(),
                                contentDescription = currentAlbum.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                OverlayPurple.copy(alpha = 0.3f),
                                                DeepPurple.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 48.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = onBackClick,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = { isFavorite = !isFavorite },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (isFavorite) Color(0xFFFF6B6B) else Color.White
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 20.dp, vertical = 20.dp)
                            ) {
                                Text(
                                    text = currentAlbum.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentAlbum.artist,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                        shape = CircleShape,
                                        modifier = Modifier.size(52.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Button(
                                        onClick = {},
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        shape = CircleShape,
                                        modifier = Modifier.size(52.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Shuffle,
                                            contentDescription = "Shuffle",
                                            tint = Color.Black,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "About this album",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DeepPurple
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentAlbum.description.ifEmpty {
                                        "A unique album by ${currentAlbum.artist} that blends different musical styles into a memorable listening experience."
                                    },
                                    fontSize = 14.sp,
                                    color = Color(0xFF4A4A6A),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Row {
                                        Text(
                                            text = "Artist: ",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DeepPurple
                                        )
                                        Text(
                                            text = currentAlbum.artist,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color.White
                                )
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    items(10) { index ->
                        TrackItem(
                            coverUrl = currentAlbum.image,
                            trackTitle = "${currentAlbum.title} • Track ${index + 1}",
                            artist = currentAlbum.artist,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        MiniPlayer(
            album = album,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

