package easy.tuto.myquizapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    Button backBtn;

    static int totalQuestion = QuestionAnswer.question.length;
    int currentQuestionIndex = 0;
    static String[] userAnswers = new String[totalQuestion];
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
        backBtn = findViewById(R.id.back_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        totalQuestionsTextView.setText("Total questions : "+totalQuestion);
        loadNewQuestion();
    }


    @Override
    public void onClick(View view) {

        Button clickedButton = (Button) view;
        if(clickedButton.getId()==R.id.submit_btn){
            String selectedAnswer = getSelectedAnswer();
            if (selectedAnswer != null) {
                userAnswers[currentQuestionIndex] = selectedAnswer;
            }
            currentQuestionIndex++;
            if (currentQuestionIndex == totalQuestion) {
                finishQuiz();
            } else {
                loadNewQuestion();
            }
        }
        else if(clickedButton.getId()==R.id.back_btn) {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadNewQuestion();
            }
        }
        else {
            //choices button clicked
            clearSelection();
            clickedButton.setBackgroundColor(Color.MAGENTA);
            String selectedAnswer = clickedButton.getText().toString();
            userAnswers[currentQuestionIndex] = selectedAnswer;
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

    DestinationGenerator destinationGenerator = new DestinationGenerator();




    void finishQuiz(){
        String answers = "Your answers:\n\n";
        for (int i = 0; i < totalQuestion; i++) {
            answers += "Question " + (i + 1) + ": " + userAnswers[i] + "\n";
        }

        String bestDestination = DestinationGenerator.getBestDestination(userAnswers);
        String resultMessage = "Your best destination is: " + bestDestination;

        // Start a new activity to show the quiz results
        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("answers", answers);
        intent.putExtra("bestDestination", bestDestination);
        intent.putExtra("userAnswers", userAnswers); // add this line to pass userAnswers to QuizResultActivity
        startActivity(intent);
        finish();
    }




    private void clearSelection() {
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);
    }

    private String getSelectedAnswer() {
        if (ansA.getBackground().mutate().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).mutate().getConstantState()) {
            return ansA.getText().toString();
        }
        if (ansB.getBackground().mutate().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).mutate().getConstantState()) {
            return ansB.getText().toString();
        }
        if (ansC.getBackground().mutate().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).mutate().getConstantState()) {
            return ansC.getText().toString();
        }
        if (ansD.getBackground().mutate().getConstantState() == getResources().getDrawable(R.drawable.button_background_selected).mutate().getConstantState()) {
            return ansD.getText().toString();
        }
        return null;
    }

}