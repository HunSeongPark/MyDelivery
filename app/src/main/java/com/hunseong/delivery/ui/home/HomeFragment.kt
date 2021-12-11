package com.hunseong.delivery.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hunseong.delivery.R
import com.hunseong.delivery.databinding.FragmentHomeBinding
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible
import com.hunseong.delivery.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        initViews()
        updateUI(auth.currentUser)
        return binding.root
    }

    private fun initViews() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient((this.activity as MainActivity), gso)

        binding.googleBtn.setOnClickListener {
            binding.progressBar.visible()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        binding.addBtn.setOnClickListener {
            val directions = HomeFragmentDirections.homeFragmentToAddFragment()
            findNavController().navigate(directions)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.result
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                Timber.tag("GoogleAccount").d("error : $e")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        binding.progressBar.gone()
        if (user == null) {
            binding.addBtn.gone()
            binding.nicknameTv.text = getString(R.string.require_login)
            binding.emptyListTv.visible()
            binding.googleBtn.visible()
        } else {
            binding.addBtn.visible()
            binding.nicknameTv.text = getString(R.string.nickname_title, user.displayName)
            binding.emptyListTv.visible()
            binding.googleBtn.gone()
        }
    }
}