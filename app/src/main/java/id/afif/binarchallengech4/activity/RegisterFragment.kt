package id.afif.binarchallengech4.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import id.afif.binarchallengech4.R
import id.afif.binarchallengech4.database.NoteDatabase
import id.afif.binarchallengech4.database.User
import id.afif.binarchallengech4.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var mDb : NoteDatabase?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = NoteDatabase.getInstance(requireContext())
        addData()
    }


    private fun addData(){
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(isPasswordTheSame()){
                saveToDb(username,email,password)
            }else{
                Toast.makeText(requireContext(),"Password tidak sama!!",Toast.LENGTH_SHORT).show()
                binding.etConfirmPassword.text.clear()
            }
        }
    }

    private fun saveToDb(username : String,email:String,password:String){
        val user = User(null,username,email,password)
        CoroutineScope(Dispatchers.IO).launch{
            val result = mDb?.userDao()?.insertUser(user)
            if(result!=0L){
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Data Berhasil ditambahkan",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Data Berhasil ditambahkan",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun isPasswordTheSame() : Boolean{
        binding.apply {
            return etPassword.text.toString() == etConfirmPassword.text.toString()
        }

    }


}