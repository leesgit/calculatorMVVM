package com.lbc.practice.calculator.view.problemSolving

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.adapter.ResultAdapterDataBinding
import com.lbc.practice.calculator.data.resource.Repository
import com.lbc.practice.calculator.data.resource.remote.RemoteDataSource
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.CalculatorViewModel
import com.lbc.practice.calculator.viewmodel.ProblemViewModel
import com.lbc.practice.calculator.viewmodel.ResultListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_problem_solving.*
import javax.inject.Inject


class ProblemSolvingActivity : DaggerAppCompatActivity() {


    var resouceMain: Int = 0
    var resouceCorrect: Int = 0
    var resouceInCorrect: Int = 0
    var recycler :RecyclerView?=null
    var adapter: ResultAdapterDataBinding? = null
    var start = false
    lateinit var binding: ActivityProblemSolvingBinding

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_problem_solving)


        val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(ProblemViewModel::class.java)
        binding.let {
            it.viewmodel = viewmodel
            it.setLifecycleOwner(this)
        }

        binding.viewmodel?.answer?.observe(this, Observer { data ->
            viewmodel.checkChange()
        })

        binding.viewmodel?.checkEnd()?.observe(this, Observer { judge ->
            if (judge!!) {
                finish()
            }
        })

        binding.viewmodel?.toastMessage!!.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        binding.viewmodel?.getResultItems()?.observe(this, Observer { results ->
            if (results != null) {
                adapter?.addItems(results)
                adapter?.notifychanged()
                binding.rvProblemAnswerResultTag.smoothScrollToPosition(binding.rvProblemAnswerResultTag.getAdapter()!!.getItemCount());
                if (results.get(results.size - 1).isResult && viewmodel.symbolState) {
                    music.answerSound(this, resouceCorrect, true)
                    viewmodel.symbolState = false
                } else if (results.get(results.size - 1).isResult == false && viewmodel.symbolState) {
                    music.answerSound(this, resouceInCorrect, false)
                    viewmodel.symbolState = false
                }
            }
        })

        init()
    }

    fun init() {
        adapter = ResultAdapterDataBinding()

        start = true

        binding.rvProblemAnswerResultTag.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProblemAnswerResultTag.adapter = adapter

        resouceMain = resources.getIdentifier("main", "raw", packageName)
        resouceCorrect = resources.getIdentifier("correct", "raw", packageName)
        resouceInCorrect = resources.getIdentifier("in_correct", "raw", packageName)

        music.mainsongStart(this, resouceMain)
    }

    override fun onDestroy() {
        super.onDestroy()
        music.mainsongStop()
    }

    override fun onResume() {
        super.onResume()
        if (start) {
            music.mainsongStart(this, resouceMain)
        }
    }

    override fun onStop() {
        super.onStop()
        music.mainsongStop()
    }
}

