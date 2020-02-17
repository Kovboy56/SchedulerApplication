package com.example.schoolscheduler.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolscheduler.TestEditorActivity;
import com.example.schoolscheduler.R;
import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.database.TestEntity;
import com.example.schoolscheduler.utilities.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.schoolscheduler.utilities.Constants.NOTE_ID_KEY;
import static com.example.schoolscheduler.utilities.Constants.TERM_ID_KEY;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private final List<TestEntity> mTests;
    private final Context mContext;

    public TestAdapter(List<TestEntity> mTests, Context mContext) {
        this.mTests = mTests;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.test_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TestEntity test = mTests.get(position);
        holder.mTextView.setText(test.getName());

        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestEditorActivity.class);
                intent.putExtra(Constants.TEST_ID_KEY, test.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.test_text) TextView mTextView;
        @BindView(R.id.fab) FloatingActionButton mFab;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
