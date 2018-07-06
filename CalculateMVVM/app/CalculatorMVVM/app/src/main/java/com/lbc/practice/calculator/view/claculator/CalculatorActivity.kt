package com.lbc.practice.calculator.view.claculator;

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle;
import android.view.WindowManager
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.R.id.tv_rx_text
import com.lbc.practice.calculator.databinding.ActivityCalculatorBinding
import com.lbc.practice.calculator.databinding.ActivityProblemSolvingBinding
import com.lbc.practice.calculator.di.ActivityBindingModule_CalculatoActivity
import com.lbc.practice.calculator.util.CalculateManager
import com.lbc.practice.calculator.util.MusicManager
import com.lbc.practice.calculator.viewmodel.CalculatorViewModel
import com.lbc.practice.calculator.viewmodel.ProblemViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_calculator.*
import javax.inject.Inject

class CalculatorActivity : DaggerAppCompatActivity() {


    private var resourceids: IntArray? = IntArray(10)
    private var resouceCal: Int = 0
    var event: Observable<CharSequence>? = null
    lateinit var binding: ActivityCalculatorBinding

    @Inject
    lateinit var music: MusicManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var calc : CalculateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator)
        val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)
        binding.let {
            it.viewmodel = viewmodel
            it.setLifecycleOwner(this)
        }

        init()

        binding.viewmodel?.text?.observe(this, Observer { text ->
            if(text!!.length>0) {
                when ( text.toString()[text.length-1]) {
                    '+','-','X','/' -> tv_rx_text.text = calc.calculate(text.toString().substring(0,text.length-1))

                    '1','2','3','4','5','6','7','8','9' ->
                        music.numberSound(application, resourceids!![Integer.parseInt(text.toString()[text.length-1].toString())], Integer.parseInt(text.toString()[text.length-1].toString()))
                    '0' -> if(text.length>1) {
                        music.numberSound(application, resourceids!![0], 0)
                    }
                }
            }
        })
    }

    fun init() {
        var resourceId: Int
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

    override fun onRestart() {
        super.onRestart()
        music.calSongStart(application, resouceCal)
    }


}