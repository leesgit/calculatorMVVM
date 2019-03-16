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

    var resourceids: IntArray? = IntArray(10)
    var resouceCal: Int = 0
    var start = false

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var calc: CalculateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator)
        val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)
        binding.let {
            it.viewmodel = viewmodel
            it.setLifecycleOwner(this)
        }

        viewmodel.text.observe(this, Observer {
            viewmodel.textChange()
        })

        viewmodel.numberSound.observe(this, Observer {
            if(it) {
                music.numberSound(application, resourceids!![Integer.parseInt(it.toString()[viewmodel.text.value!!.length - 1].toString())], Integer.parseInt(it.toString()[viewmodel.text.value!!.length - 1].toString()))
                viewmodel.numberSound.value = false
            }
        })

        viewmodel.numberSoundZero.observe(this, Observer {
            if(it) {
                music.numberSound(application, resourceids!![0], 0)
                viewmodel.numberSoundZero.value = false
            }
        })

        init()
    }


    fun init() {
        var resourceId: Int
        start =true
        for (i in 0..9) {
            resourceId = resources.getIdentifier("num$i", "raw", packageName)
            resourceids!![i] = resourceId
        }
        resouceCal = resources.getIdentifier("cal", "raw", packageName)
        music.calSongStart(application, resouceCal)
    }

    override fun onStop() {
        super.onStop()
        music.calSongStop()
    }

    override fun onResume() {
        super.onResume()
        if(start) {
            music.calSongStart(application, resouceCal)
        }
    }

}