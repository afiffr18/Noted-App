package id.afif.binarchallengech4.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import id.afif.binarchallengech4.R
import id.afif.binarchallengech4.database.NoteDatabase
import id.afif.binarchallengech4.database.User
import id.afif.binarchallengech4.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private var mDb : NoteDatabase?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = NoteDatabase.getInstance(requireContext())
        registerClicked()
        loginClicked()
    }

    private fun loginClicked(){
        binding.btnLogin.setOnClickListener {
            isCheckAccountReady()
        }
    }

    private fun registerClicked(){
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun isCheckAccountReady(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            var result = mDb?.userDao()?.getUser(username, password)
            val dataUser = result?.component1()
            if(result.isNullOrEmpty()){
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(),"Username atau password salah!!",Toast.LENGTH_LONG).show()
                    binding.etPassword.text.clear()
                }
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(dataUser!!)
                    findNavController().navigate(action)
                }
            }
        }
    }


}