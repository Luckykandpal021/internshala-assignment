import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.internshal_lucky_assessment.Note
import com.example.internshal_lucky_assessment.R
import java.util.UUID

class NotesAdapter(
    private val notes: MutableList<Note>,
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit,
    private val onSaveClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: EditText = itemView.findViewById(R.id.edit_text_title)
        val content: EditText = itemView.findViewById(R.id.edit_content_title)
        val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
        val saveButton: ImageButton = itemView.findViewById(R.id.save_button)
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        val contentTitle: TextView = itemView.findViewById(R.id.content_title)
        val itemDivider: com.google.android.material.divider.MaterialDivider = itemView.findViewById(R.id.item_divider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.setText(note.title)
        holder.content.setText(note.content)
        holder.textTitle.text = "Title: ${note.title}"
        holder.contentTitle.text = "Content: ${note.content}"

        var isEditMode = false
        holder.editButton.setOnClickListener {
            isEditMode = !isEditMode
            if (isEditMode) {
                holder.title.visibility = View.VISIBLE
                holder.content.visibility = View.VISIBLE
                holder.deleteButton.visibility = View.VISIBLE
                holder.saveButton.visibility = View.VISIBLE
                holder.textTitle.visibility = View.GONE
                holder.itemDivider.visibility = View.GONE
                holder.contentTitle.visibility = View.GONE
            } else {
                holder.title.visibility = View.GONE
                holder.content.visibility = View.GONE
                holder.deleteButton.visibility = View.GONE
                holder.itemDivider.visibility = View.VISIBLE
                holder.saveButton.visibility = View.GONE
                holder.textTitle.visibility = View.VISIBLE
                holder.contentTitle.visibility = View.VISIBLE
            }
        }

        holder.saveButton.setOnClickListener {
            note.title = holder.title.text.toString()
            note.content = holder.content.text.toString()
            holder.textTitle.text = note.title
            holder.contentTitle.text = note.content
            onSaveClick(note)
            isEditMode = false
            holder.title.visibility = View.GONE
            holder.content.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
            holder.saveButton.visibility = View.GONE
            holder.textTitle.visibility = View.VISIBLE
            holder.contentTitle.visibility = View.VISIBLE
        }

        holder.deleteButton.setOnClickListener { onDeleteClick(note) }

        holder.title.visibility = View.GONE
        holder.content.visibility = View.GONE
        holder.deleteButton.visibility = View.GONE
        holder.saveButton.visibility = View.GONE
        holder.textTitle.visibility = View.VISIBLE
        holder.contentTitle.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int = notes.size

    fun addNewNote() {
        val id = UUID.randomUUID().toString()
        notes.add(Note(id, "", ""))
        notifyItemInserted(notes.size - 1)
    }
}
