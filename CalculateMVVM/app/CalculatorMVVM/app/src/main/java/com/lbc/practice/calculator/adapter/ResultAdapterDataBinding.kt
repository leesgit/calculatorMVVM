package com.lbc.practice.calculator.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.data.Result
import com.lbc.practice.calculator.databinding.ItemProblemSolvingResultTagBinding
import com.lbc.practice.calculator.viewmodel.ResultListViewModel
import java.util.ArrayList

class ResultAdapterDataBinding : RecyclerView.Adapter<ResultAdapterDataBinding.InnerViewHolder>() {

    var list: MutableList<Result> = ArrayList<Result>()

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val result = list.get(position)
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifychanged() {
        notifyDataSetChanged()
    }


    fun addItems(lists: MutableList<Result>) {
        if(list.size>0) {
            list.add(lists.get(lists.size-1))
        } else {
            list.addAll(lists)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemProblemSolvingResultTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }


    class InnerViewHolder(private val binding: ItemProblemSolvingResultTagBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(itemResult: Result) {
            with(binding) {
                viewmodel = ResultListViewModel(itemResult)
                executePendingBindings()
            }
        }
        //여기서 viewmodel을 할당하지 않고 setVariable(BR.result, imtemReult)과 xml에 variable로 result를 설정 해둔 다음에 데이터를 연결해도 되지만 다양한 경험을 위해 viewmodel연결을 하였습니다.
    }
}