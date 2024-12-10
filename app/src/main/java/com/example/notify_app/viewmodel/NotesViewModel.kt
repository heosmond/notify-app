package com.example.notify_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify_app.api.SpotifyRepository
import com.example.notify_app.data.Note
import com.example.notify_app.data.NoteDao
import com.example.notify_app.events.NoteEvent
import com.example.notify_app.state.NoteState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/* !!ViewModel handles events and data changes!!
* TODO: Consider approach to connecting data gotten from API to the
*  Dao fields, I think we just need to plug the values into here
*  !!research data caching methods with room to see how this is done*/

class NotesViewModel(private val dao: NoteDao) : ViewModel() {
    // DATABASE
    //like a get request but for notes from DB
    private val isSortedByDate = MutableStateFlow(true)

    //flip how notes are sorted, I think there's a better way to do this
    //Dunno why this needs stateIn
    @OptIn(ExperimentalCoroutinesApi::class)
    private var notes = isSortedByDate.flatMapLatest { sort ->
        if (sort) {
            dao.getNotesOrderedByLastModified()
        } else {
            dao.getNotesOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    //get state object
    val _state = MutableStateFlow(NoteState())


    val state = combine(_state, notes, isSortedByDate) { state, notes, isSortedByDate ->
        state.copy(
            notes = notes,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    // API
    private val repository = SpotifyRepository()

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: MutableLiveData<String?> get() = _accessToken

    fun getAccessToken() {
        viewModelScope.launch {
            val token = repository.fetchAccessToken()
            _accessToken.postValue(token)
        }
    }


    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                //process: just get rid of it! It'll be fine! (it actually is fine)
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NoteEvent.SaveNote -> {
                //process: create new note object, use object to update DB, update state (effects UI)
                val title = state.value.title
                val content = state.value.content

                //No blank fields
                if (title.isBlank() || content.isBlank()) {
                    return
                }

                val note = Note(
                    title = title,
                    song = "placeholder", //TODO plug in values from API here??
                    artist = "placeholder",
                    year = "placeholder",
                    genre = "placeholder",
                    content = content,
                    imagePath = "R.drawable.placeholder.jpg",
                    lastModified = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.upsertNote(note)
                }
                //resets state after saving note
                _state.update {
                    it.copy(
                        title = "",
//                        song = "",
//                        artist = "",
//                        year = "",
//                        genre = "",
                        content = "",
//                        imagePath = "",
                    )
                }
            }
            is NoteEvent.SetContent -> {
                _state.update {
                    it.copy(
                        content = event.content
                    )
                }
            }
            is NoteEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            //flip-flop how notes are sorted?
            NoteEvent.SortNotes -> {
                isSortedByDate.value = !isSortedByDate.value
            }
        }
    }

}