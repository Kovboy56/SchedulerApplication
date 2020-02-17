package com.example.schoolscheduler;

import android.content.Intent;
import android.os.Bundle;

import com.example.schoolscheduler.database.CourseEntity;
import com.example.schoolscheduler.database.TestDao;
import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.ui.TestAdapter;
import com.example.schoolscheduler.utilities.Constants;
import com.example.schoolscheduler.viewmodel.MainViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.schoolscheduler.utilities.Constants.COURSE_ID_KEY;
import static com.example.schoolscheduler.utilities.Constants.cTerm;
import static com.example.schoolscheduler.utilities.Constants.currentTerm;

public class TestViewer extends AppCompatActivity {

    @BindView(R.id.recycler_view_test)
    RecyclerView mRecyclerView;

    @OnClick(R.id.fab_test)
    void fabClickHandler() {
        Intent intent = new Intent(this, TestEditorActivity.class);
        startActivity(intent);
    }

    private List<TestEntity> testData = new ArrayList<>();
    private TestAdapter mAdapter;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initViewModel() {

        final Observer<List<TestEntity>> testObserver =
                new Observer<List<TestEntity>>() {
                    @Override
                    public void onChanged(List<TestEntity> testEntities) {
                        testData.clear();
                        //testData.addAll(testEntities);
                        for(TestEntity test: testEntities){
                            if(test.getCourseId()== Constants.currentCourse){
                                testData.add(test);
                            }
                        }

                        if(mAdapter == null){
                            mAdapter=new TestAdapter(testData,
                                    TestViewer.this);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                };

        mViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mViewModel.mTests.observe(this, testObserver);

        Bundle extras = getIntent().getExtras();
        setTitle("Tests for " + Constants.cCourse.getName());
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }
}
