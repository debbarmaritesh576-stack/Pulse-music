package com.pulse.music.player

import com.pulse.music.database.entity.SongEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueManager @Inject constructor() {
    
    private val _queue = MutableStateFlow<List<SongEntity>>(emptyList())
    val queue: StateFlow<List<SongEntity>> = _queue
    
    private val _currentIndex = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex

    fun setQueue(songs: List<SongEntity>, startIndex: Int = 0) {
        _queue.value = songs
        _currentIndex.value = startIndex
    }

    fun addToQueue(song: SongEntity) {
        _queue.value = _queue.value + song
    }

    fun addToQueueNext(song: SongEntity) {
        val current = _queue.value.toMutableList()
        current.add(_currentIndex.value + 1, song)
        _queue.value = current
    }

    fun removeFromQueue(index: Int) {
        val current = _queue.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _queue.value = current
            if (index < _currentIndex.value) _currentIndex.value--
        }
    }

    fun moveItem(from: Int, to: Int) {
        val current = _queue.value.toMutableList()
        if (from in current.indices && to in current.indices) {
            val item = current.removeAt(from)
            current.add(to, item)
            _queue.value = current
            if (from == _currentIndex.value) _currentIndex.value = to
        }
    }

    fun clearQueue() {
        _queue.value = emptyList()
        _currentIndex.value = -1
    }

    fun getNext(): SongEntity? {
        val idx = _currentIndex.value + 1
        return if (idx < _queue.value.size) {
            _currentIndex.value = idx
            _queue.value[idx]
        } else null
    }

    fun getPrevious(): SongEntity? {
        val idx = _currentIndex.value - 1
        return if (idx >= 0) {
            _currentIndex.value = idx
            _queue.value[idx]
        } else null
    }

    fun getCurrent(): SongEntity? = _queue.value.getOrNull(_currentIndex.value)
}