package id.afif.binarchallengech4.activity

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import id.afif.binarchallengech4.R
import id.afif.binarchallengech4.adapter.ActionButtonListener
import id.afif.binarchallengech4.adapter.NoteAdapter
import id.afif.binarchallengech4.database.Note
import id.afif.binarchallengech4.database.NoteDatabase
import id.afif.binarchallengech4.database.User
import id.afif.binarchallengech4.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NoteAdapter
    private var mDb : NoteDatabase?=null
    private var user : User?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = NoteDatabase.getInstance(requireContext())
        user = HomeFragmentArgs.fromBundle(arguments as Bundle).keyUser
        binding.tvUserLogin.text = "Welcome : ${user?.username}"
        initRecycler()
        addData()
        getAllNote()
    }

    private fun initRecycler(){
        binding.apply {
            noteAdapter = NoteAdapter(action)
            rvNoted.adapter = noteAdapter
            rvNoted.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private val action = object : ActionButtonListener{
        override fun btnEdit(note: Note) {
            showAlertDialog(note)
        }

        override fun btnDelete(note: Note) {
            showDeleteDialog(note)
        }

    }

    private fun showDeleteDialog(note: Note){
        val customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_delete,null,false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(customLayout)
        val dialog = builder.create()

        val btnYes = customLayout.findViewById<Button>(R.id.btn_yes)
        val btnNo = customLayout.findViewById<Button>(R.id.btn_no)

        btnYes.setOnClickListener {
            deleteData(note)
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteData(note:Note){
        CoroutineScope(Dispatchers.IO).launch {
            val result = mDb?.noteDao()?.deleteNote(note)
            if(result!=0){
                getAllNote()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                }
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Gagal Dihapus",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }



    private fun addData(){
        binding.fabAdd.setOnClickListener {
            showAlertDialog(null)
        }
    }
    //alertdialog untuk Add data
    private fun showAlertDialog(note : Note?){
        val customLayout = LayoutInflater.from(requireContext()).inflate(R.layout.custom_layout_add,null,false)

        val tvJudul = customLayout.findViewById<TextView>(R.id.et_judul)
        val tvNote = customLayout.findViewById<TextView>(R.id.et_note)
        val btnSave = customLayout.findViewById<Button>(R.id.btn_save)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(customLayout)
        val dialog = builder.create()

        if(note!=null){
            tvJudul.text = note.judul
            tvNote.text = note.catatan
        }

        btnSave.setOnClickListener {
            val judul=tvJudul.text.toString()
            val catatan=tvNote.text.toString()
            if(note!=null){
                val newNote = Note(note.id, user?.id!!,judul, catatan)
                updateToDb(newNote)
            }else{
                saveToDb(judul,catatan)
            }

            dialog.dismiss()
        }
        dialog.show()

    }
    //insert data
    private fun saveToDb(judul:String,note:String){
        val note = Note(id = null, userId = user?.id ?: 0,judul = judul,catatan = note)
        CoroutineScope(Dispatchers.IO).launch {
            val result = mDb?.noteDao()?.insertNote(note)
            if(result!=null){
                getAllNote()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Data berhasil ditambahkan",Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    //update data
    private fun updateToDb(note: Note){
        CoroutineScope(Dispatchers.IO).launch {
            val result = mDb?.noteDao()?.updateNote(note)
            if(result!=0){
                getAllNote()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Data Berhasil Diupdate",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //get all data
    private fun getAllNote(){
        val id = user?.id
        if(id!=null){
            getNoteFromDb(id)
        }
    }



    private fun getNoteFromDb(userId : Int){
        CoroutineScope(Dispatchers.IO).launch {
            val result = mDb?.noteDao()?.getAllNote(userId)
            if(result!=null){
                noteAdapter.updateData(result)
            }
        }
    }




}
