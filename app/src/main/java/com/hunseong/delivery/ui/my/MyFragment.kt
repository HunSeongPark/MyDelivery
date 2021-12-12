package com.hunseong.delivery.ui.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hunseong.delivery.R
import com.hunseong.delivery.databinding.FragmentMyBinding
import com.hunseong.delivery.extension.visible
import com.hunseong.delivery.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        initViews()
        return binding.root
    }

    private fun initViews() = with(binding) {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_client_id))
            .requestEmail()
            .build()

        val googleSignInClient =
            GoogleSignIn.getClient((this@MyFragment.activity as MainActivity), gso)

        if (auth.currentUser != null) {

            profileTv.text = auth.currentUser!!.displayName

            logInOutBtn.apply {
                setText(R.string.logout)
                setOnClickListener {
                    auth.signOut()
                    Toast.makeText(this@MyFragment.context, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT)
                        .show()

                    val direction = MyFragmentDirections.myFragmentToHomeFragment()
                    findNavController().navigate(direction)
                }
            }
        } else {
            profileTv.setText(R.string.require_login)

            logInOutBtn.apply {
                setText(R.string.login)
                setOnClickListener {
                    progressBar.visible()
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, 100)
                }
            }

            profileTv.setOnClickListener {
                progressBar.visible()
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, 100)
            }
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
                    Toast.makeText(this.context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()

                    val direction = MyFragmentDirections.myFragmentToHomeFragment()
                    findNavController().navigate(direction)
                } else {
                    Toast.makeText(this.context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}