package com.example.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app1.R
import com.example.app1.modal.EmployeeModel

class EmpAdaptor(private val empList: ArrayList<EmployeeModel>) :
    RecyclerView.Adapter<EmpAdaptor.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val empName: TextView = itemView.findViewById(R.id.tvEmpName)
        val empImage: ImageView = itemView.findViewById(R.id.ivEmpImage)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_list_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val employee = empList[position]
        holder.empName.text = employee.empName

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(employee.imageUrl)
            .into(holder.empImage)
    }

    override fun getItemCount() = empList.size
}
