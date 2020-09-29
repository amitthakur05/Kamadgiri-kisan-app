package com.example.kamadgirikisan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kamadgirikisan.ui.login.quizLoginActivity;

import org.json.JSONArray;

public class quizHome extends AppCompatActivity {
    TextView marq,announcement,error,conditions;
    String userId;
    Button previouWinner,playQuiz;
    RequestQueue queue;
    CheckBox checkBox;
    String getQuizUrl = "http://148.72.213.116:3000/api/quiz/";
    String getAnnouncementUrl = "http://148.72.213.116:3000/api/admin/announce/get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_quiz_home);
        marq= findViewById(R.id.marq);
        marq.setSelected(true);
        previouWinner = findViewById(R.id.prevoiuswinner);
        announcement = findViewById(R.id.announcement);
        checkBox = findViewById(R.id.terms);
        playQuiz = findViewById(R.id.playquiz);
        error = findViewById(R.id.error);
        conditions = findViewById(R.id.conditions);
        SharedPreferences quizsharedprefrence = getApplicationContext().getSharedPreferences("quizuser",MODE_PRIVATE);
        userId = quizsharedprefrence.getString("userId","0");
        getActiveQuiz();
        getAnnouncement();

        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(quizHome.this);
                builder1.setMessage("सभी किसान भाइयो का कामदगिरी सीड्स की ओर से हार्दिक स्वागत है| आप सभी को जिस प्रतियोगिता का बेसब्री से इंतजार था \n" +
                        "वह आज से शुरू होने जा रही है |\n" +
                        "प्नतियोगिता में भाग लेने के नियम व शर्ते इस प्रकार है | \n" +
                        "1.एक किसान भाई दिन में एक ही बार इस प्रतियोगिता में भाग ले सकता है| \n" +
                        "2. प्रतियोगिता शाम 7 बजे से 8 बजे के बीच खेली जाएगी | \n" +
                        "3.विजेता का नाम लक्की ड्रा के माध्यम से अगले दिन घोषित किया जायेगा | \n" +
                        "4. जीती गयी धनराशि आपके अपने कामदगिरी किसान सहायक से अगले दिन प्राप्त की जा सकती है |\n" +
                        "5.प्रतियोगिता के नियमो में परिवर्तन का अधिकार कंपनी के पास सुरक्षित रहेगा | \n" +
                        "6. किसी भी विवाद की स्थिति में कंपनी के जी.एम.मार्केटिंग का निर्णयअंतिम व सर्वमान्य होगा | \n" +
                        "यदि आप सभी भाई सहमत हो तो नीचे दिए गए लिंक पर क्लिक करके इसका लाभ उठा सकते है | ");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkBox.setChecked(true);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    error.setVisibility(TextView.GONE);
//                    Toast.makeText(quizHome.this, ""+true, Toast.LENGTH_SHORT).show();
                }
            }
        });

        previouWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quizHome.this, previousWinners.class);
                startActivity(intent);
            }
        });

        playQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked()) {
                    error.setVisibility(TextView.VISIBLE);
//                    Toast.makeText(quizHome.this, ""+true, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(quizHome.this, quiz.class);
                startActivity(intent);
            }
        });

    }

    public void getActiveQuiz() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getQuizUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray quiz = new JSONArray(response.trim());
                            if(quiz.length()>0) {
                                Toast.makeText(quizHome.this, ""+quiz.length(),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(quizHome.this, "Something went wrong..", Toast.LENGTH_SHORT);
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");
    }

    public void getAnnouncement() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getAnnouncementUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray announcementArray = new JSONArray(response);
                            for(int i=0; i<announcementArray.length(); i++) {
                                if(announcementArray.getJSONObject(i).getString("key").equals("NEXT_QUIZE")) {
                                    announcement.setText(announcementArray.getJSONObject(i).getString("msg"));
                                    announcement.setVisibility(TextView.VISIBLE);
//                                    Toast.makeText(quizHome.this, "meesage"+announcementArray.getJSONObject(i).getString("msg"), Toast.LENGTH_LONG).show();
                                }
                            }
//                            Toast.makeText(quizHome.this, "ressss"+response, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     Toast.makeText(quizHome.this, "Something went wrong..",Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 30, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }
        queue.add(stringRequest);
        stringRequest.setTag("TAG");
    }

}