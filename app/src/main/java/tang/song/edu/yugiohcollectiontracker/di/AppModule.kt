package tang.song.edu.yugiohcollectiontracker.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tang.song.edu.yugiohcollectiontracker.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRequestItems(): RequestOptions =
        RequestOptions
            .placeholderOf(R.drawable.img_cardback)
            .error(R.drawable.img_cardback)

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager =
        Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
}