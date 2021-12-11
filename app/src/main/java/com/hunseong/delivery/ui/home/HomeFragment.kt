package com.hunseong.delivery.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.*
import com.hunseong.delivery.databinding.FragmentHomeBinding
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible
import com.hunseong.delivery.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var trackingAdapter: TrackingInfoAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        auth = Firebase.auth
        initViews()
        updateUI(auth.currentUser)
        return binding.root
    }

    private fun initViews() = with(binding) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient((this@HomeFragment.activity as MainActivity), gso)

        googleBtn.setOnClickListener {
            progressBar.visible()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        addBtn.setOnClickListener {
            val directions = HomeFragmentDirections.homeFragmentToAddFragment()
            findNavController().navigate(directions)
        }

        refreshBtn.setOnClickListener {
            (recyclerView.adapter as TrackingInfoAdapter).modifyMode = false
            viewModel.updateInfo(auth.currentUser!!.uid)
        }

        modifyBtn.setOnClickListener {
            it.gone()
            modifyCancelDeleteLayout.visible()

            (recyclerView.adapter as TrackingInfoAdapter).apply {
                modifyMode = true
                notifyDataSetChanged()
            }
        }

        cancelBtn.setOnClickListener {
            modifyBtn.visible()
            modifyCancelDeleteLayout.gone()

            (recyclerView.adapter as TrackingInfoAdapter).apply {
                modifyMode = false
                currentList.forEach { it.isChecked = false }
                notifyDataSetChanged()
            }
        }

        deleteBtn.setOnClickListener {
            val deleteInfoList =
                (recyclerView.adapter as TrackingInfoAdapter).currentList.filter { it.isChecked }

            if (deleteInfoList.isNotEmpty()) {
                viewModel.deleteInfo(auth.currentUser!!.uid, deleteInfoList)
            }
        }

        trackingAdapter = TrackingInfoAdapter()
        recyclerView.adapter = trackingAdapter

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

    private fun updateUI(user: FirebaseUser?) = with(binding) {
        progressBar.gone()
        if (user == null) {
            addBtn.gone()
            nicknameTv.text = getString(R.string.require_login)
            emptyListTv.visible()
            googleBtn.visible()
        } else {
            addBtn.visible()
            nicknameTv.text = getString(R.string.nickname_title, user.displayName)
            emptyListTv.visible()
            googleBtn.gone()
            viewModel.updateInfo(auth.currentUser!!.uid)
        }
    }
}