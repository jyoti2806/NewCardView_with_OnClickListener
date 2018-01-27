package com.example.user.mycourse;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.mycourse.databinding.ActivityCourseDetailBinding;

public class CourseDetail extends AppCompatActivity {
    Button button;
    TextView toc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        toc=findViewById(R.id.table_of_contents);
        button=findViewById(R.id.button);
        MyData myData = getIntent().getParcelableExtra("Card");
        ActivityCourseDetailBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_course_detail);
        binding.setMyData(myData);
    }
}
