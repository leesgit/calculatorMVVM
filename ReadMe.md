# ���� ������Ʈ

- ### ����

  - Kotlin, Dagger2, livedata, MVVM ���� �̿��� ���� ������Ʈ�� �Դϴ�.

  - ����Ǯ�� ȭ���� �̹����� ���� �̹��� �̰� ���⿡ ���� �̹����� xxhdpi�Դϴ�. �̹����� ���� ��� ���� ���� ��������ϴ�.

  - viewmodel���� �����͸� ���� �ϱ� ������ ������ ȸ�� �Ǿ� onCreate �� ��쿡�� �����͸� �����մϴ�.(��Ʈ�������� ��ũ�� ������ 1��8�ʸ� ���ø� �˴ϴ�.)  

  - ���� ��Ű�÷��� ������ �۵� ��Ű�� RetrofitManager�� ���� �ּҸ� �Է� �ϼž� �ϸ� ������ �ּҿ� ���� /cal�� �ٿ� �ֽðų� ���� �ֽø�˴ϴ�.

  - Db��  `idx` INT(11) NOT NULL AUTOINCREAMENT,
  `lessonNum` INT(11) NOT NULL, `lessonName` VARCHAR(255)  NOT NULL,
 `problemNumber` INT(11) NOT NULL, `problemContent` VARCHAR(255)  NOT NULL, `problemAnswer` VARCHAR(255)  NOT NULL,
 PRIMARY KEY (`idx`)); �� ���� �Ǿ��ֽ��ϴ�.

  - Dagger2�� �ƴ� InjectorUtil�� ���� �����Ͽ�(google samples sunflower) repository�� context������ �Ҵ��ϴ� ����� ������ Dagger2�� �̿��Ͽ� ���¸� �� �޸� �Ͽ����ϴ�. 

- ### �ٽ� �ڵ� ����

```
- ProblemViewModel

results.value?.let {
            if (it[it.size - 1].isResult && symbolState) { //���� �ֱ��� ������ true�� ��� ���� ���� false�� ��� ���� ������ ����մϴ�.
                correctSound.postValue(true)
                symbolState = false // ȭ���� �������� �ٽ� ���� �Ǹ鼭 ���尡 ��� �Ǵ°��� �����ϱ� ���� ����
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
        //���⼭ viewmodel�� ������� �ʰ� databinding�� �ٸ� ������� ó�� ���������� viewmodel�� ����� ����; �̷��� �Ͽ����ϴ�.
    }
```


```
- CalculatorViewModel

fun btnSymbol(view: View) { //(0~9���� ���ڹ�ư���, +-���� ��ȣ��ư�� , clear, result���� ��� ��ư���� ��ɿ� ���� ����
			// ���� �޼ҵ� �ϳ��� ǥ���Ͽ����ϴ�.(���⼱ +-���� �ɺ�)

        val num = StringTokenizer(statement.toString(), "+-/X")
        val oper = StringTokenizer(statement.toString(), "1234567890.")
        if (symbol == true && num.countTokens() >= oper.countTokens()) { // 1++2�� ���� �������� �����ϱ� ���� 
				 // boolean���� StringTonkenizer�� ����(���ڵ��� ������ ��ȣ ������) ������ ���� �մϴ�.
            when (view.tag as String) {  //�±� ���� �̿��Ͽ� �ڵ��� �ߺ��� ���Դϴ�.
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
//���� ��𵨿� �����ǰ� �ִ� text��(����ڰ� �����Է��ϴ� �ؽ�Ʈ)�� �ǽð����� observe�Ͽ� ������ ���ڰ� ��ȣ�� ��� �ǽð� ��� �ؽ�Ʈ�� �����ϸ�
//������ ��ȣ�� 123456789�� ��� �ش� ��ȣ�� �´� ������ ����մϴ�.
//0�� ��� clear�ϰų� ó�� �����Ǿ��� ��� ���� 0�̶� ������ ����ɼ� �����Ƿ� text�� ���̰� 2�̻��϶� ��� ���ֱ� ���� ���� ó�� ������ϴ�.
```



