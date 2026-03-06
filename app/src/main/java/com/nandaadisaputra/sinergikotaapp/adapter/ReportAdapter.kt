package com.nandaadisaputra.sinergikotaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nandaadisaputra.sinergikotaapp.R
import com.nandaadisaputra.sinergikotaapp.model.Report

class ReportAdapter(private val list_report: List<Report>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.findViewById(R.id.tv_item_title)
        val tv_desc: TextView = view.findViewById(R.id.tv_item_desc)
        val tv_date: TextView = view.findViewById(R.id.tv_item_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = list_report[position]
        holder.tv_title.text = report.title
        holder.tv_desc.text = report.description
        holder.tv_date.text = report.createdDate
    }

    override fun getItemCount(): Int = list_report.size
}