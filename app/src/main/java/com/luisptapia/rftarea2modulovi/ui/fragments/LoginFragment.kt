package com.luisptapia.rftarea2modulovi.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.luisptapia.rftarea2modulovi.R
import com.luisptapia.rftarea2modulovi.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var email =  ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // instancia objecto firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null ){
            actionLoginSuccessful()
        }


        binding.btLogin.setOnClickListener {
            if(!validateFields()) return@setOnClickListener

            authenticateUser(email,password)
        }

        binding.tvSingup.setOnClickListener {

            if(!validateFields()) return@setOnClickListener


            createUser(email,password)

        }

        binding.tvForgotPassword.setOnClickListener {
            resetPassword()
        }
    }


    private fun actionLoginSuccessful(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                TroopsListFragment()
            )
            .commit()
    }

    private fun authenticateUser(usr:String , psw:String){
        firebaseAuth.signInWithEmailAndPassword(usr,psw).addOnCompleteListener { authResult ->

            if(authResult.isSuccessful){

                Toast.makeText(requireContext(), getString(R.string.success_auth),Toast.LENGTH_SHORT).show()
                actionLoginSuccessful()

            }else{

                handleErrors(authResult)

            }

        }
    }

    private fun validateFields(): Boolean{
        email = binding.tietUser.text.toString().trim()  //Elimina los espacios en blanco
        password = binding.tietPassword.text.toString().trim()

        //Verifica que el campo de correo no esté vacío
        if(email.isEmpty()){
            binding.tietUser.error = getString(R.string.email_required)
            binding.tietUser.requestFocus()
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tietUser.error = getString(R.string.email_no_valid)
            binding.tietUser.requestFocus()
            return false
        }

        //Verifica que el campo de la contraseña no esté vacía y tenga al menos 6 caracteres
        if(password.isEmpty()){
            binding.tietPassword.error = getString(R.string.password_required)
            binding.tietPassword.requestFocus()
            return false
        }else if(password.length < 6){
            binding.tietPassword.error = getString(R.string.weak_password)
            binding.tietPassword.requestFocus()
            return false
        }
        return true
    }

    private fun resetPassword(){
        val resetMail = EditText(requireContext())
        resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.reset_password))
            .setMessage(getString(R.string.message_reset_password))
            .setView(resetMail)
            .setPositiveButton(getString(R.string.email)){_,_->
                val mail = resetMail.text.toString()

                if(mail.isNotEmpty() &&  Patterns.EMAIL_ADDRESS.matcher(mail).matches() ){
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener {
                        Toast.makeText(requireContext(),
                            getString(R.string.reset_password_success), Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(),
                            getString(R.string.reset_password_error),Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(requireContext(),
                        getString(R.string.no_valid_email), Toast.LENGTH_LONG).show()
                }

            }
            .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun handleErrors(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(requireContext(),getString(R.string.wrong_email_message),Toast.LENGTH_LONG).show()
                binding.tietUser.error = getString(R.string.wrong_email)
                binding.tietUser.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireContext(),getString(R.string.wrong_password_message),Toast.LENGTH_LONG).show()
                binding.tietPassword.error = getString(R.string.wrong_password)
                binding.tietPassword.requestFocus()
                binding.tietPassword.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(requireContext(),
                    getString(R.string.wrong_credentials_message),Toast.LENGTH_LONG).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.email_already_in_use_message),Toast.LENGTH_LONG).show()
                binding.tietUser.error = (getString(R.string.email_aready_in_use))
                binding.tietUser.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.expired_session_message),Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.not_found_user_message),Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.weak_password_message),Toast.LENGTH_SHORT).show()
                binding.tietPassword.error = getString(R.string.weak_password)
                binding.tietPassword.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(requireContext(), getString(R.string.no_network),Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), getString(R.string.auth_error),Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun createUser(usr:String,psw:String){
        firebaseAuth.createUserWithEmailAndPassword(usr,psw).addOnCompleteListener { authResult ->
            if(authResult.isSuccessful){

                firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(requireContext(),
                        getString(R.string.verification_email_send),Toast.LENGTH_SHORT).show()

                    Toast.makeText(requireContext(), getString(R.string.user_created),Toast.LENGTH_SHORT).show()

                    actionLoginSuccessful()

                }?.addOnFailureListener {
                    Toast.makeText(requireContext(),
                        getString(R.string.verification_email_error),Toast.LENGTH_SHORT).show()
                }

            }else{
                handleErrors(authResult)
            }
        }
    }


}