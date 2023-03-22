package easy.tuto.myquizapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;

    int totalQuestion = QuestionAnswer.question.length;
    int currentQuestionIndex = 0;
    String[] userAnswers = new String[totalQuestion];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalQuestionsTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions : "+totalQuestion);

        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            userAnswers[currentQuestionIndex] = getSelectedAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex == totalQuestion) {
                finishQuiz();
            } else {
                loadNewQuestion();
            }
        } else {
            //choices button clicked
            clearSelection();
            clickedButton.setBackgroundColor(Color.MAGENTA);
        }
    }

    void loadNewQuestion(){
        questionTextView.setText(QuestionAnswer.question[currentQuestionIndex]);
        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);
        clearSelection();
    }

    void finishQuiz(){
        String answers = "Your answers:\n\n";
        for (int i = 0; i < totalQuestion; i++) {
            answers += "Question " + (i + 1) + ": " + userAnswers[i] + "\n";
        }
        new AlertDialog.Builder(this)
                .setTitle("Quiz finished")
                .setMessage(answers)
                .setPositiveButton("Restart",(dialogInterface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();
    }

    void restartQuiz(){
        currentQuestionIndex = 0;
        Arrays.fill(userAnswers, null);
        loadNewQuestion();
    }

    private void clearSelection() {
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);
    }

    private String getSelectedAnswer() {
        if (ansA.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).getConstantState()) {
            return ansA.getText().toString();
        }
        if (ansB.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).getConstantState()) {
            return ansB.getText().toString();
        }
        if (ansC.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).getConstantState()) {
            return ansC.getText().toString();
        }
        if (ansD.getBackground().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).getConstantState()) {
            return ansD.getText().toString();
        }
        return null;
    }
}