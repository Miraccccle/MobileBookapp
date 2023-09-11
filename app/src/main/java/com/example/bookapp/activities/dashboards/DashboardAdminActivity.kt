package com.example.bookapp.activities.dashboards

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import com.example.bookapp.activities.CategoryAddActivity
import com.example.bookapp.activities.MainActivity
import com.example.bookapp.activities.ProfileActivity
import com.example.bookapp.activities.pdf.PdfAddActivity
import com.example.bookapp.adapters.AdapterCategory
import com.example.bookapp.databinding.ActivityDashboardAdminBinding
import com.example.bookapp.models.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardAdminBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    private lateinit var adapterCategory: AdapterCategory
    public companion object {
        val refCategory = FirebaseDatabase.getInstance().getReference("Categories")
        val refBooks = FirebaseDatabase.getInstance().getReference("Books")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()
        binding.searchEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception) {

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.logoutBtn.setOnClickListener {
            //confirm
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Confirm") {a, d->
                    firebaseAuth.signOut()
                    checkUser()
                }
                .setNegativeButton("Cancel") {a, d ->
                    a.dismiss()
                    checkUser()
                }
                .show()

        }

        binding.addCategoryBtn.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }

        binding.addPgfFab.setOnClickListener {
            startActivity(Intent(this, PdfAddActivity::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()

        //val ref = FirebaseDatabase.getInstance().getReference("Categories")
        refCategory.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)

                    categoryArrayList.add(model!!)
                }
                adapterCategory = AdapterCategory(this@DashboardAdminActivity, categoryArrayList)

                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else {
            val email = firebaseUser.email

            binding.subTitleTv.text = email
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && firebaseAuth.currentUser != null) {
            // your code
            val builder = AlertDialog.Builder(this@DashboardAdminActivity)
            builder.setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Confirm") {a, d->
                    firebaseAuth.signOut()
                    checkUser()
                }
                .setNegativeButton("Cancel") {a, d ->
                    a.dismiss()
                    checkUser()
                }
                .show()
            true
        } else super.onKeyDown(keyCode, event)
    }
}