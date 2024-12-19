package com.example.componentslearning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.componentslearning.databinding.RecyclerViewRowBinding

class ContactAdapter(
    private val contactList: List<ContactModel>
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerViewRowBinding.bind(view)

        fun setupUI(contactName: String, contactNumber: String) {
            binding.contactName.text = contactName
            binding.contactNumber.text = contactNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.setupUI(
            contactList.get(position).name,
            contactList.get(position).number)
    }
}