package com.lbc.practice.calculator.view.claculator;

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.databinding.ActivityCalculatorBinding
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.CalculatorViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CalculatorActivity : DaggerAppCompatActivity() {

    lateinit var binding: ActivityCalculatorBinding

    lateinit var viewModel: CalculatorViewModel

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.text.observe(this, Observer {
            viewModel.textChange()
        })

        viewModel.numberSound.observe(this, Observer {
            if (it) {
                music.numberSound(application, viewModel.resourceids!![Integer.parseInt(it.toString()[viewModel.text.value!!.length - 1].toString())], Integer.parseInt(it.toString()[viewModel.text.value!!.length - 1].toString()))
                viewModel.numberSound.value = false
            }
        })

        viewModel.numberSoundZero.observe(this, Observer {
            if (it) {
                music.numberSound(application, viewModel.resourceids!![0], 0)
                viewModel.numberSoundZero.value = false
            }
        })

        init()
    }


    private fun init() {
        var resourceId: Int
        for (i in 0..9) {
            resourceId = resources.getIdentifier("num$i", "raw", packageName)
            viewModel.resourceids!![i] = resourceId
        }
        viewModel.resouceCal = resources.getIdentifier("cal", "raw", packageName)
    }

    override fun onStop() {
        super.onStop()
        music.calSongStop()
    }

    override fun onResume() {
        super.onResume()
        music.calSongStart(application, viewModel.resouceCal)
    }

}