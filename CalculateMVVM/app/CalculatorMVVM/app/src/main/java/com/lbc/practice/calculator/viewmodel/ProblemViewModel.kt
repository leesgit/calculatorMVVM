package com.lbc.practice.calculator.viewmodel

import android.app.Application
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.lbc.practice.calculator.R
import com.lbc.practice.calculator.data.LessonData
import com.lbc.practice.calculator.data.LessonType
import com.lbc.practice.calculator.data.Problem
import com.lbc.practice.calculator.data.Result
import com.lbc.practice.calculator.data.resource.DataSource
import com.lbc.practice.calculator.data.resource.Repository
import com.lbc.practice.calculator.view.claculator.CalculatorActivity
import java.util.ArrayList

class ProblemViewModel : ViewModel {

    val title = MutableLiveData<String>()
    val problem = MutableLiveData<String>()
    val answer = MutableLiveData<String>()
    val next = MutableLiveData<String>().apply { value ="" }
    val numCount = MutableLiveData<Int>()
    val num = MutableLiveData<Int>()
    val calNum = MutableLiveData<String>()
    val calVisibility = MutableLiveData<Boolean>().apply { value =true }
    val checkState = MutableLiveData<Boolean>()
    val answerResult = MutableLiveData<Boolean>()
    val end = MutableLiveData<Boolean>().apply { value =false }
    val nextState = MutableLiveData<Boolean>()
    val inputSet = MutableLiveData<Boolean>()
    val checkInput = MutableLiveData<Boolean>().apply { value = false }
    val calstate = MutableLiveData<Boolean>().apply { value = true }
    val calBackground = MutableLiveData<Int>().apply { value =  R.color.basic }
    val results = MediatorLiveData<MutableList<Result>>()
    var list: MutableList<Result> = ArrayList<Result>()

    var cnt = 0
    var calCount = 2
    var problemCount = 0
    var calString: String? = null

    var symbolState = false

    var dataSource: DataSource
    var mcontext: Application

    constructor(repository: Repository, application: Application) {
        dataSource = repository
        mcontext = application
        setStart(LessonType.plus)

    }



    fun setStart(lessonType: Int) {
        dataSource.getProblemCount(object : DataSource.LoadDataCallBack3 {
            override fun onLoadData(count: Integer) {
                calString = mcontext.getString(R.string.calculator_chance_format)
                problemCount = count.toInt()
                numCount.value = problemCount
                calNum.postValue(calString + calCount)

                setProblem(LessonType.plus)
            }

            override fun onFailData(errorMsg: String) {
                Toast.makeText(mcontext, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setProblem(lessonType: Int) {
        cnt++
        if (cnt > problemCount) {
            end.postValue(true)
        } else {
            dataSource.getProblem(cnt, object : DataSource.LoadDataCallBack {
                override fun onLoadData(info: Problem) {
                    problem.postValue(String.format(mcontext.getString(R.string.question_form), info.problemContent))
                }

                override fun onFailData(errorMsg: String) {
                    Toast.makeText(mcontext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            })
            dataSource.getLessonData(lessonType, cnt, object : DataSource.LoadDataCallBack2 {
                override fun onLoadData(info: LessonData) {
                    title.postValue(info.lessonName +" 기본학습 중입니다.")

                }

                override fun onFailData(errorMsg: String) {
                    Toast.makeText(mcontext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            })

            num.postValue(cnt)
            answer.postValue("")
            nextState.postValue(false)
            inputSet.postValue(true)
            checkState.postValue(true)
        }
    }

    fun calOnclick(view: View) {
        if (calCount > 0) {
            calCount--
            calNum.postValue(calString + calCount)
            val intent = Intent(view.context, CalculatorActivity::class.java)
            view.context.startActivity(intent)
            if (calCount == 0) {
                calstate.postValue(false)
                calBackground.postValue(R.color.unable_state)
            }
        }
    }

    fun checkAnswer() {
        dataSource.getProblem(cnt, object : DataSource.LoadDataCallBack {
            override fun onLoadData(info: Problem) {
                if (info.correctAnswer.equals(answer.value)) {
                    list.add(Result(cnt,true))
                    results.postValue(list)
                    answerResult.postValue(true)
                    symbolState = true
                } else {
                    list.add(Result(cnt,false))
                    results.postValue(list)
                    answerResult.postValue(false)
                    symbolState = true
                }
            }

            override fun onFailData(errorMsg: String) {
                Toast.makeText(mcontext, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
        checkState.postValue(false)
        nextState.postValue(true)
        inputSet.postValue(false)

        if (cnt == problemCount) {
            next.postValue(mcontext.getString(R.string.finish_lesson))
        } else {
            next.postValue(mcontext.getString(R.string.next_problem))
        }
    }

    fun clickNext() {
        setProblem(LessonType.plus)
    }

    fun rxChange() = answer

    fun checkEnd() = end

    fun getResultItems() = results


}