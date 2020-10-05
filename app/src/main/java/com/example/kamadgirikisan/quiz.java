package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class quiz extends AppCompatActivity {
    String url = "http://148.72.213.116:3000/api/quiz/details/";
    RequestQueue queue;
    JSONArray quizDetails;
    TextView questionText;
    JSONArray options;
    int questionNo = 0;
    Button firstOption, secondOption, thirdOption, fourthOption,nextQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e) {

        }
        setContentView(R.layout.activity_quiz);
        firstOption = findViewById(R.id.first);
        questionText = findViewById(R.id.question);
        nextQuestion = findViewById(R.id.next);
        getQuiz();

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }

   public void getQuiz() {
       String finalUrl = url + "5ebe4a524cc41d1794d36052";
       StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           quizDetails = new JSONArray(response.trim());
                           Toast.makeText(quiz.this, "response" + quizDetails.getJSONObject(0).getString("questionText"), Toast.LENGTH_LONG).show();
                           options = quizDetails.getJSONObject(questionNo).getJSONArray("options");
                           questionText.setText(quizDetails.getJSONObject(questionNo).getString("questionText"));

                           firstOption.setText(options.getJSONObject(questionNo).getString("optionText"));
                       } catch (Exception e) {

                       }
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(quiz.this, "Something went wrong" , Toast.LENGTH_SHORT).show();
                   }
               });
       stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*30 , DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       if (queue == null) {
           queue = Volley.newRequestQueue(getApplicationContext());
       }
       queue.add(stringRequest);
       stringRequest.setTag("TAG");
    }

    public void nextQuestion() {
        questionNo++;
        try {
            questionText.setText(quizDetails.getJSONObject(questionNo).getString("questionText"));

            firstOption.setText(options.getJSONObject(questionNo).getString("optionText"));
        }
        catch (Exception e) { }
    }

    public void previousQuestion() {

    }
}