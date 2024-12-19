package com.features.main.data.repo

import com.core.network.dataproviders.PodcastDataProviders
import com.features.main.data.mapper.toDomainPodcastList
import com.features.main.domain.model.Podcast
import com.features.main.domain.repo.PodcastRepository
import com.features.main.domain.repo.PodcastType
import javax.inject.Inject

class PodcastRepoImpl
@Inject constructor(private val podcastDataProvider: PodcastDataProviders) : PodcastRepository {
    override suspend fun getPodcastList(podcastType: PodcastType): List<Podcast> {
        return when(podcastType) {
            PodcastType.Popular -> podcastDataProvider.getPodcastListPopular().toDomainPodcastList()
            PodcastType.Featured -> podcastDataProvider.getPodcastListFeatured().toDomainPodcastList()
        }
    }
}