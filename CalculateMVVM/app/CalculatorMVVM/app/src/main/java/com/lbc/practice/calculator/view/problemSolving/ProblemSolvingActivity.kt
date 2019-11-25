package com.lbc.practice.calculator.view.problemSolving

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.adapter.ResultAdapter
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.view.claculator.CalculatorActivity
import com.lbc.practice.calculator.viewmodel.ProblemViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class ProblemSolvingActivity : DaggerAppCompatActivity() {

    lateinit var binding: ActivityProblemSolvingBinding

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ProblemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_problem_solving)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProblemViewModel::class.java)

        binding.let {
            it.viewmodel = viewModel
            it.adapter = ResultAdapter()
            it.adapter!!.setItem(viewModel.list)
        }

        binding.rvProblemAnswerResultTag.let {
            it.adapter = binding.adapter!!
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.answer.observe(this, Observer {
            viewModel.checkChange()
        })

        viewModel.checkEnd().observe(this, Observer { judge ->
            if (judge!!) {
                finish()
            }
        })

        viewModel.toastMessage.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getResultItems().observe(this, Observer { results ->
            if (results != null) {
                binding.adapter!!.notifyDataSetChanged()

                binding.rvProblemAnswerResultTag.smoothScrollToPosition(binding.rvProblemAnswerResultTag.adapter!!.itemCount)

                viewModel.answerSound()
            }
        })

        viewModel.correctSound.observe(this, Observer {
            if (it) {
                music.answerSound(this, viewModel.resouceCorrect, true)
                viewModel.correctSound.value = false
            }
        })

        viewModel.inCorrectSound.observe(this, Observer {
            if (it) {
                music.answerSound(this, viewModel.resouceInCorrect, false)
                viewModel.inCorrectSound.value = false
            }
        })

        viewModel.toCalculatorActivity.observe(this, Observer {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        })

        init()
    }

    private fun init() {
        viewModel.resouceMain = resources.getIdentifier("main", "raw", packageName)
        viewModel.resouceCorrect = resources.getIdentifier("correct", "raw", packageName)
        viewModel.resouceInCorrect = resources.getIdentifier("in_correct", "raw", packageName)
    }

    override fun onDestroy() {
        super.onDestroy()
        music.mainsongStop()
    }

    override fun onResume() {
        super.onResume()
        music.mainsongStart(this, viewModel.resouceMain)
    }

    override fun onStop() {
        super.onStop()
        music.mainsongStop()
    }
}

