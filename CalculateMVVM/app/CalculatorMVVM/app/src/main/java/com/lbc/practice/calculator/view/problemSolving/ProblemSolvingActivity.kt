package com.lbc.practice.calculator.view.problemSolving

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.adapter.ResultAdapterDataBinding
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.ProblemViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_problem_solving.*
import javax.inject.Inject


class ProblemSolvingActivity : DaggerAppCompatActivity() {


    private var resouceMain: Int = 0
    private var resouceCorrect: Int = 0
    private var resouceInCorrect: Int = 0
    private var adapter: ResultAdapterDataBinding? = null


    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityProblemSolvingBinding = DataBindingUtil.setContentView(this, R.layout.activity_problem_solving)

        val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(ProblemViewModel::class.java)
        binding.let {
            it.viewmodel = viewmodel
            it.setLifecycleOwner(this)
        }

        init()

        binding.viewmodel?.rxChange()?.observe(this, Observer { data ->
            if (data!!.length > 0) {
                viewmodel.checkInput.postValue(true)
            } else {
                viewmodel.checkInput.postValue(false)
            }
        })

        binding.viewmodel?.checkEnd()?.observe(this, Observer { judge ->
            if (judge!!) {
                finish()
            }
        })

        binding.viewmodel?.getResultItems()?.observe(this, Observer { results ->
            if (results != null) {
                adapter?.addItems(results)
                adapter?.notifychanged()
                rv_problem_answer_result_tag.smoothScrollToPosition(rv_problem_answer_result_tag.getAdapter().getItemCount());
                if (results.get(results.size - 1).isResult&&viewmodel.symbolState) {
                    music.answerSound(this, resouceCorrect, true)
                    viewmodel.symbolState = false
                } else if(results.get(results.size - 1).isResult==false&&viewmodel.symbolState) {
                    music.answerSound(this, resouceInCorrect, false)
                    viewmodel.symbolState = false
                }
            }
        })
    }

    fun init() {
        adapter = ResultAdapterDataBinding()

        rv_problem_answer_result_tag.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_problem_answer_result_tag.adapter = adapter

        resouceMain = resources.getIdentifier("main", "raw", packageName)
        resouceCorrect = resources.getIdentifier("correct", "raw", packageName)
        resouceInCorrect = resources.getIdentifier("in_correct", "raw", packageName)

        music.mainsongStart(this, resouceMain)
    }

    override fun onDestroy() {
        super.onDestroy()
        music.mainsongStop()
    }

    override fun onRestart() {
        super.onRestart()
        music.mainsongStart(this, resouceMain)
    }

    override fun onStop() {
        super.onStop()
        music.mainsongStop()
    }
}

