package id.afif.binarchallengech4.adapter

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.afif.binarchallengech4.R
import id.afif.binarchallengech4.database.Note

class NoteAdapter(private val listener: ActionButtonListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

   private val difCallback = object : DiffUtil.ItemCallback<Note>(){
       override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
           return oldItem == newItem
       }

       override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
           return oldItem.hashCode() == newItem.hashCode()
       }

   }

    private val differ = AsyncListDiffer(this,difCallback)

    fun updateData(notes : List<Note>) = differ.submitList(notes)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_note,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val tvId = view.findViewById<TextView>(R.id.tv_id_value)
        private val tvJudul = view.findViewById<TextView>(R.id.tv_judul_value)
        private val tvNote = view.findViewById<TextView>(R.id.tv_note_value)
        private val btnEdit = view.findViewById<ImageView>(R.id.btn_edit)
        private val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)

        fun bind(note : Note){
            tvId.text = note.id.toString()
            tvJudul.text = "Judul : ${note.judul.toString()}"
            tvNote.text = "Note : \n${ note.catatan.toString() }"

            btnEdit.setOnClickListener {
                listener.btnEdit(note)
            }

            btnDelete.setOnClickListener {
                listener.btnDelete(note)
            }

        }

    }

}

interface ActionButtonListener{
    fun btnEdit(note: Note)
    fun btnDelete(note: Note)

}