package com.lbc.practice.calculator.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lbc.practice.calculator.data.LessonData
import com.lbc.practice.calculator.data.LessonType
import com.lbc.practice.calculator.data.Problem
import com.lbc.practice.calculator.data.Result
import com.lbc.practice.calculator.data.resource.DataSource
import com.lbc.practice.calculator.data.resource.Repository
import com.lbc.practice.calculator.view.claculator.CalculatorActivity
import java.util.ArrayList

class ProblemViewModel(repository: Repository) : ViewModel() {

    val title = MutableLiveData<String>()
    val problem = MutableLiveData<String>()
    val answer = MutableLiveData<String>()
    val next = MutableLiveData<String>()
    val numCount = MutableLiveData<Int>()
    val num = MutableLiveData<Int>()
    val calNum = MutableLiveData<String>()
    val checkState = MutableLiveData<Boolean>()
    val answerResult = MutableLiveData<Boolean>()
    val end = MutableLiveData<Boolean>()
    val nextState = MutableLiveData<Boolean>()
    val inputSet = MutableLiveData<Boolean>()
    val checkInput = MutableLiveData<Boolean>()
    val calstate = MutableLiveData<Boolean>()
    val results = MediatorLiveData<MutableList<Result>>()
    var list: MutableList<Result> = ArrayList<Result>()
    val toastMessage = MutableLiveData<String>()
    val correctSound = MutableLiveData<Boolean>()
    val inCorrectSound = MutableLiveData<Boolean>()

    var cnt = 0
    var calCount = 2
    var problemCount = 0
    var calString: String? = null

    var symbolState = false

    var dataSource: DataSource = repository

    init {
        end.value = false
        checkInput.value = false
        calstate.value = true
    }

    fun setStart(lessonType: Int) {
        dataSource.getProblemCount(object : DataSource.LoadDataCallBack3 {
            override fun onLoadData(count: Int) {
                calString = "찬스 : "
                problemCount = count
                numCount.value = problemCount
                calNum.postValue(calString + calCount)

                setProblem(LessonType.plus)
            }

            override fun onFailData(errorMsg: String) {
                toastMessage.postValue(errorMsg)
            }
        })
    }

    fun setProblem(lessonType: Int) {

        if (++cnt > problemCount) {
            end.postValue(true)
        } else {
            dataSource.getProblem(cnt, object : DataSource.LoadDataCallBack {
                override fun onLoadData(info: Problem) {
                    problem.postValue("다음 문제를 계산하시오.\\n\\n " + info.problemContent)
                }

                override fun onFailData(errorMsg: String) {
                    toastMessage.postValue(errorMsg)
                }
            })
            dataSource.getLessonData(lessonType, cnt, object : DataSource.LoadDataCallBack2 {
                override fun onLoadData(info: LessonData) {
                    title.postValue(info.lessonName + " 기본학습 중입니다.")

                }

                override fun onFailData(errorMsg: String) {
                    toastMessage.postValue(errorMsg)
                }
            })

            num.value = cnt
            answer.value = ""
            nextState.value = false
            inputSet.value = true
            checkState.value = true
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
            }
        }
    }

    fun checkAnswer() {
        dataSource.getProblem(cnt, object : DataSource.LoadDataCallBack {
            override fun onLoadData(info: Problem) {
                symbolState = if (info.correctAnswer.equals(answer.value)) {
                    list.add(Result(cnt, true))
                    results.postValue(list)
                    answerResult.postValue(true)
                    true
                } else {
                    list.add(Result(cnt, false))
                    results.postValue(list)
                    answerResult.postValue(false)
                    true
                }
            }

            override fun onFailData(errorMsg: String) {
                toastMessage.postValue(errorMsg)
            }
        })

        if (cnt == problemCount) {
            next.postValue("학습완료")
        } else {
            next.postValue("다음문제")
        }

        checkState.postValue(false)
        nextState.postValue(true)
        inputSet.postValue(false)
    }


    fun clickNext() {
        setProblem(LessonType.plus)
    }

    fun checkChange() {
        checkInput.value = answer.value!!.isNotEmpty()
    }

    fun answerSound() {
        results.value?.let {
            if (it[it.size - 1].isResult && symbolState) {
                correctSound.postValue(true)
                symbolState = false
            } else if (!it[it.size - 1].isResult && symbolState) {
                inCorrectSound.postValue(true)
                symbolState = false
            }
        }
    }

    fun checkEnd() = end

    fun getResultItems() = results
}