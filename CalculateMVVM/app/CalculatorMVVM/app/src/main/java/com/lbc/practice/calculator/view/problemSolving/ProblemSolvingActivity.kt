package com.lbc.practice.calculator.view.problemSolving

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.adapter.ResultAdapterDataBinding
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.ProblemViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class ProblemSolvingActivity : DaggerAppCompatActivity() {


    private var resouceMain = 0
    private var resouceCorrect = 0
    private var resouceInCorrect = 0
    private var adapter: ResultAdapterDataBinding? = null
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
            it.lifecycleOwner = this
        }

        viewmodel.answer.observe(this, Observer { data ->
            viewmodel.checkChange()
        })

        viewmodel.checkEnd().observe(this, Observer { judge ->
            if (judge!!) {
                finish()
            }
        })

        viewmodel.toastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewmodel.getResultItems().observe(this, Observer { results ->
            if (results != null) {
                adapter?.addItems(results)
                adapter?.notifyDataSetChanged()
                binding.rvProblemAnswerResultTag.smoothScrollToPosition(binding.rvProblemAnswerResultTag.getAdapter()!!.getItemCount());

                viewmodel.answerSound()
            }
        })

        viewmodel.correctSound.observe(this, Observer {
            if (it) {
                music.answerSound(this, resouceCorrect, true)
                viewmodel.correctSound.value = false
            }
        })

        viewmodel.inCorrectSound.observe(this, Observer {
            if (it) {
                music.answerSound(this, resouceInCorrect, false)
                viewmodel.inCorrectSound.value = false
            }
        })

        init()
    }

    private fun init() {
        adapter = ResultAdapterDataBinding()


        binding.rvProblemAnswerResultTag.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProblemAnswerResultTag.adapter = adapter

        resouceMain = resources.getIdentifier("main", "raw", packageName)
        resouceCorrect = resources.getIdentifier("correct", "raw", packageName)
        resouceInCorrect = resources.getIdentifier("in_correct", "raw", packageName)

//        music.mainsongStart(this, resouceMain)
    }

    override fun onDestroy() {
        super.onDestroy()
        music.mainsongStop()
    }

    override fun onResume() {
        super.onResume()
        music.mainsongStart(this, resouceMain)
    }

    override fun onStop() {
        super.onStop()
        music.mainsongStop()
    }
}

