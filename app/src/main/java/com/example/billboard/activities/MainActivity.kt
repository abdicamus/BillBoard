package com.example.billboard.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.billboard.R
import com.example.billboard.databinding.ActivityMainBinding
import com.example.billboard.consts.DialogConsts
import com.example.billboard.dialogs.SignDialogHelper
import com.example.billboard.fragments.NewBillFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var headerTvTitle: TextView
    val firebaseAuth = FirebaseAuth.getInstance()
    private val signDialogHelper = SignDialogHelper(this)
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        toolbar = findViewById(R.id.toolbar)
        init()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        signDialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                        headerTvTitle.text = account.displayName.toString()
                    } else {
                        Toast.makeText(
                            this,
                            "Something got wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: ApiException) {

                }
            }
        }
    }

    private fun init() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        headerTvTitle = binding.navView.getHeaderView(0).findViewById(R.id.header_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new_ad -> {
                val intent = Intent(this, EditNewBillActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ads_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.my_ads -> {
                Toast.makeText(this, "Pressed my_ads", Toast.LENGTH_SHORT).show()
            }
            R.id.electronics -> {
                Toast.makeText(this, "Pressed electronics", Toast.LENGTH_SHORT).show()
            }
            R.id.transport -> {
                Toast.makeText(this, "Pressed transport", Toast.LENGTH_SHORT).show()
            }
            R.id.services -> {
                Toast.makeText(this, "Pressed services", Toast.LENGTH_SHORT).show()
            }
            R.id.animals -> {
                Toast.makeText(this, "Pressed animals", Toast.LENGTH_SHORT).show()
            }
            R.id.sign_up -> {
                signDialogHelper.createSignDialog(DialogConsts.SIGN_UP_STATE)
            }
            R.id.sign_in -> {
                signDialogHelper.createSignDialog(DialogConsts.SIGN_IN_STATE)
                updateUI(firebaseAuth.currentUser)
            }
            R.id.sign_out -> {
                firebaseAuth.signOut()
                signDialogHelper.accHelper.singOutFromGoogleAcc()
                updateUI(null)
            }
            R.id.fragment -> {
                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.drawerLayout, NewBillFragment())
                    .commit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateUI(user: FirebaseUser?) {
        headerTvTitle.text = if (user == null) {
            resources.getString(R.string.not_auth)
        } else {
            user.email
        }
    }
}