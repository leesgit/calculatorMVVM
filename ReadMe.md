# 토이 프로젝트

- ### 설명

  - Kotlin, Dagger2, livedata, MVVM 등을 이용한 토이 프로젝트를 입니다.

  - 문제풀이 화면의 이미지는 백터 이미지 이고 계산기에 사용된 이미지는 xxhdpi입니다. 이미지와 음성 모두 제가 직접 만들었습니다.

  - 실행 시키시려면 서버를 작동 시키고 RetrofitManager에 서버 주소를 입력 하셔야 하며 서버의 주소에 따라 /cal을 붙여 주시거 떼어 주시면됩니다.

  - Db는  `idx` INT(11) NOT NULL AUTOINCREAMENT,
  `lessonNum` INT(11) NOT NULL, `lessonName` VARCHAR(255)  NOT NULL,
 `problemNumber` INT(11) NOT NULL, `problemContent` VARCHAR(255)  NOT NULL, `problemAnswer` VARCHAR(255)  NOT NULL,
 PRIMARY KEY (`idx`)); 로 구성 되어있습니다.


- ### 핵심 코드 설명

  -문제 화면

<code>
```
ProblemSolvingActivity

binding.viewmodel?.getResultItems()?.observe(this, Observer { results ->
            if (results != null) {
                adapter?.addItems(results)  //뷰모델에 저장된 정답 결과들을 adpater에 보냅니다.
                adapter?.notifychanged() 
                rv_problem_answer_result_tag.smoothScrollToPosition(rv_problem_answer_result_tag.getAdapter().getItemCount());
                if (results.get(results.size - 1).isResult) {       //가장 최근의 정답이 true일 경우 맞는 음악 false일 경우 틀린음악을 재생합니다.
                    music.answerSound(this, resouceCorrect, true)
                } else {
                    music.answerSound(this, resouceInCorrect, false)
                }
            }
        })


ResultAdapterDataBinding

fun addItems(lists: MutableList<Result>) {
        if(list.size>0) {
            list.add(lists.get(lists.size-1))
        } else {
            list.addAll(lists)
        }
    }

//뷰모델에서 리스트가 아닌 하나의 아이템을 저장할 경우 회전되어도 마지막 정답만 살아 남기 때문에 모든 데이터들의 보존을 위해 viewmodel에서
//단일 아이템이 아닌 list로 정보를 관리 합니다.


```
</code>


  -계산기 화면

<code>
```
CalculatorViewModel

fun btnSymbol(view: View) { //(0~9같은 숫자버튼들과, +-등의 기호버튼들 , clear, result등의 기능 버튼들을 기능에 따라 묶어 각각 메소드 하나로 표현하였습니다.(여기선 +-등의 심볼)

        val num = StringTokenizer(statement.toString(), "+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")
        if (symbol == true && num.countTokens() >= oper.countTokens()) { // 1++2와 같은 오류들을 방지하기 위해 boolean값과 StringTonkenizer를 
							          // 통해(숫자들의 갯수와 부호 갯수비교) 오류를 방지 합니다.
            when (view.tag as String) {  //태그 값을 이용하여 코드의 중복을 줄입니다.
			                           "plus" -> statement.append("+")
                "minus" -> statement.append("-")
                "division" -> statement.append("/")
                "multiply" -> statement.append("X")
            }
            text.postValue(statement.toString())
            symbol = false
        }
    }


CalculatorActivity

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

//현재 뷰모델에 관리되고 있는 text값(사용자가 숫자입력하는 텍스트)을 실시간으로 observe하여 마지막 문자가 부호일경우 실시간 계산 텍스트를 수정하며
//마지막 부호가 123456789일 경우 해당 번호에 맞는 음성을 재생합니다.
//0의 경우 clear하거나 처음 생성됬을 경우 값이 0이라 음성이 재생될수 있으므로 text의 길이가 2이상일때 재생 해주기 위해 따로 처리 해줬습니다.
```
</code>


