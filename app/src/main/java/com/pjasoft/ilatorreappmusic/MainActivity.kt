package com.pjasoft.ilatorreappmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.pjasoft.ilatorreappmusic.navigation.MusicNavGraph
import com.pjasoft.ilatorrefappmusic.ui.theme.ILatorremusicappTheme
import com.pjasoft.ilatorrefappmusic.ui.theme.LightBackground
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context)
                    .components {
                        add(OkHttpNetworkFetcherFactory(
                            callFactory = {
                                OkHttpClient.Builder()
                                    .addInterceptor { chain ->
                                        val request = chain.request().newBuilder()
                                            .header("User-Agent", "ILatorremusicapp/1.0")
                                            .build()
                                        chain.proceed(request)
                                    }
                                    .build()
                            }
                        ))
                    }
                    .crossfade(true)
                    .build()
            }

            ILatorremusicappTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MusicNavGraph()
                }
            }
        }
    }
}
