package com.lbc.practice.calculator.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbc.practice.calculator.data.Result
import com.lbc.practice.calculator.databinding.ItemProblemSolvingResultTagBinding
import com.lbc.practice.calculator.viewmodel.ResultListViewModel
import java.util.ArrayList

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.InnerViewHolder>() {

    private var list: MutableList<Result> = ArrayList()

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val result = list[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItem(list: MutableList<Result>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val binding = ItemProblemSolvingResultTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerViewHolder(binding)
    }


    class InnerViewHolder(private val binding: ItemProblemSolvingResultTagBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(itemResult: Result) {
            with(binding) {
                viewmodel = ResultListViewModel(itemResult)
            }
        }
        //여기서 viewmodel을 할당하지 않고 바로 itemResult를 넘겨 databinding 해도 되지만 다양한 경험을 위해 viewmodel이용 하였습니다.
    }
}