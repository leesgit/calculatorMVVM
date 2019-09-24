# 토이 프로젝트

- ### 설명

  - Kotlin, Dagger2, livedata, MVVM 등을 이용한 토이 프로젝트를 입니다.

  - 문제풀이 화면의 이미지는 백터 이미지 이고 계산기에 사용된 이미지는 xxhdpi입니다. 이미지와 음성 모두 제가 직접 만들었습니다.

  - viewmodel에서 데이터를 관리 하기 때문에 어플이 회전 되어 onCreate 될 경우에도 데이터를 유지합니다.(포트폴리오에 링크된 동영상 1분8초를 보시면 됩니다.)  

  - 실행 시키시려면 서버를 작동 시키고 RetrofitManager에 서버 주소를 입력 하셔야 하며 서버의 주소에 따라 /cal을 붙여 주시거나 떼어 주시면됩니다.

  - Db는  `idx` INT(11) NOT NULL AUTOINCREAMENT,
  `lessonNum` INT(11) NOT NULL, `lessonName` VARCHAR(255)  NOT NULL,
 `problemNumber` INT(11) NOT NULL, `problemContent` VARCHAR(255)  NOT NULL, `problemAnswer` VARCHAR(255)  NOT NULL,
 PRIMARY KEY (`idx`)); 로 구성 되어있습니다.

  - Dagger2가 아닌 InjectorUtil을 따로 생성하여(google samples sunflower) repository나 context값등을 할당하는 방법도 있지만 Dagger2를 이용하여 형태를 좀 달리 하였습니다. 

- ### 핵심 코드 설명

```
- ProblemViewModel

results.value?.let {
            if (it[it.size - 1].isResult && symbolState) { //가장 최근의 정답이 true일 경우 정답 음악 false일 경우 오답 음악을 재생합니다.
                correctSound.postValue(true)
                symbolState = false // 화면이 뒤집어져 다시 생성 되면서 사운드가 재생 되는것을 방지하기 위한 변수
            } else if (!it[it.size - 1].isResult && symbolState) {
                inCorrectSound.postValue(true)
                symbolState = false
            }
        }
```


```
- InnerViewHolder

class InnerViewHolder(private val binding: ItemProblemSolvingResultTagBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(itemResult: Result) {
            with(binding) {
                viewmodel = ResultListViewModel(itemResult)
                executePendingBindings()
            }
        }
        //여기서 viewmodel을 사용하지 않고 databinding등 다른 방식으로 처리 가능하지만 viewmodel을 사용해 보고싶어서 이렇게 하였습니다.
    }
```


```
- CalculatorViewModel

fun btnSymbol(view: View) { //(0~9같은 숫자버튼들과, +-등의 기호버튼들 , clear, result등의 기능 버튼들을 기능에 따라 묶어
			// 각각 메소드 하나로 표현하였습니다.(여기선 +-등의 심볼)

        val num = StringTokenizer(statement.toString(), "+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")
        if (symbol == true && num.countTokens() >= oper.countTokens()) { // 1++2와 같은 오류들을 방지하기 위해 
				 // boolean값과 StringTonkenizer를 통해(숫자들의 갯수와 부호 갯수비교) 오류를 방지 합니다.
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
```

```
- CalculatorActivity

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
//현재 뷰모델에 관리되고 있는 text값(사용자가 숫자입력하는 텍스트)을 실시간으로 observe하여 마지막 문자가 부호일 경우 실시간 계산 텍스트를 수정하며
//마지막 부호가 123456789일 경우 해당 번호에 맞는 음성을 재생합니다.
//0의 경우 clear하거나 처음 생성되었을 경우 값이 0이라 음성이 재생될수 있으므로 text의 길이가 2이상일때 재생 해주기 위해 따로 처리 해줬습니다.
```



