package com.example.internshal_lucky_assessment

import NotesAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID
class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private var notes: MutableList<Note> = mutableListOf()
    private var userId: String? = null
    private lateinit var sign_out:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        sign_out=view.findViewById(R.id.sign_out)
        userId = getCredentials(requireContext())
        val addButton = view.findViewById<Button>(R.id.add_button)

        Log.e("userIdgetCredentials",userId.toString())
        if (userId != null) {
            notes = context?.let { getNotes(it.applicationContext, userId!!) }!!
            Log.e("notesData", notes.toString())
            setupRecyclerView()
            addButton.setOnClickListener {
                addNewNote()
            }

            sign_out.setOnClickListener {
                signOut()

            }

        }

        return view
    }
    private fun signOut() {
        val masterKeyAlias = MasterKey.Builder(requireContext())
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            requireContext(),
            "user_auth",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(sharedPreferences.edit()) {
            remove("ID_TOKEN")
            apply()
        }

        showFragment(LoginFragment(),requireActivity().supportFragmentManager)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        notesAdapter = NotesAdapter(notes, this::editNote, this::deleteNote, this::saveNote)
        recyclerView.adapter = notesAdapter
    }

    private fun addNewNote() {
        notesAdapter.addNewNote()
    }

    private fun editNote(note: Note) {
        val editedNote = note.copy(title = note.title, content = note.content)
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = editedNote
            notesAdapter.notifyItemChanged(index)
            userId?.let { saveNotes(requireContext(), it, notes) }
        }
    }

    private fun deleteNote(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes.removeAt(index)
            notesAdapter.notifyItemRemoved(index)
            userId?.let { saveNotes(requireContext(), it, notes) } // Save notes after deletion
        }
    }

    private fun saveNote(note: Note) {
        Log.e("saveNote",note.toString())
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note.copy(title = note.title, content = note.content)

            notesAdapter.notifyItemChanged(index)

            userId?.let { saveNotes(requireContext(), it, notes) }
        }
    }

}
