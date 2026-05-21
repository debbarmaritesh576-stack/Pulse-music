package com.pulse.music.tageditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pulse.music.mediastore.MetadataExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TagEditorState(
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val albumArtist: String = "",
    val genre: String = "",
    val year: String = "",
    val trackNumber: String = "",
    val composer: String = "",
    val comment: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val filePath: String = ""
)

@HiltViewModel
class TagEditorViewModel @Inject constructor(
    private val metadataExtractor: MetadataExtractor
) : ViewModel() {

    private val _state = MutableStateFlow(TagEditorState())
    val state: StateFlow<TagEditorState> = _state

    fun loadSong(path: String) {
        _state.value = _state.value.copy(isLoading = true, filePath = path)
        viewModelScope.launch {
            val metadata = withContext(Dispatchers.IO) {
                metadataExtractor.extractMetadata(android.net.Uri.parse(path))
            }
            _state.value = _state.value.copy(
                title = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_TITLE] ?: "",
                artist = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST] ?: "",
                album = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM] ?: "",
                albumArtist = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST] ?: "",
                genre = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_GENRE] ?: "",
                year = metadata[android.media.MediaMetadataRetriever.METADATA_KEY_YEAR] ?: "",
                isLoading = false
            )
        }
    }

    fun updateTitle(v: String) { _state.value = _state.value.copy(title = v) }
    fun updateArtist(v: String) { _state.value = _state.value.copy(artist = v) }
    fun updateAlbum(v: String) { _state.value = _state.value.copy(album = v) }
    fun updateAlbumArtist(v: String) { _state.value = _state.value.copy(albumArtist = v) }
    fun updateGenre(v: String) { _state.value = _state.value.copy(genre = v) }
    fun updateYear(v: String) { _state.value = _state.value.copy(year = v) }
    fun updateTrack(v: String) { _state.value = _state.value.copy(trackNumber = v) }
    fun updateComposer(v: String) { _state.value = _state.value.copy(composer = v) }
    fun updateComment(v: String) { _state.value = _state.value.copy(comment = v) }

    fun saveTags() {
        _state.value = _state.value.copy(isSaving = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val tagWriter = TagWriter()
                tagWriter.writeTags(_state.value)
            }
            _state.value = _state.value.copy(isSaving = false)
        }
    }
}