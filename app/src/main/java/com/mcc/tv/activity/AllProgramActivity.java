package com.mcc.tv.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;
import com.mcc.tv.R;
import com.mcc.tv.adapter.ProgramListAdapter;
import com.mcc.tv.data.constant.AppConstants;
import com.mcc.tv.listeners.ItemClickListener;
import com.mcc.tv.model.Program;
import com.mcc.tv.utility.AdUtils;
import com.mcc.tv.utility.DataUtilities;
import com.mcc.tv.utility.ListItemDivider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class AllProgramActivity extends BaseActivity {

    private Context mContext;
    private ArrayList<Program> programList;
    private RecyclerView rvProgramList;
    private ProgramListAdapter programListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariable();
        initView();
        loadProgramList();
        initListeners();
    }

    private void initVariable() {
        mContext = getApplicationContext();
        programList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_program_list);

        // initialize toolbar
        initToolbar();
        enableBackButton();
        // set toolbar title
        getToolbar().setTitle(getString(R.string.programs));

        // list utilities
        rvProgramList = findViewById(R.id.rv_all_programs);
        programListAdapter = new ProgramListAdapter(programList, mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvProgramList.setLayoutManager(mLayoutManager);
        rvProgramList.setItemAnimator(new DefaultItemAnimator());

        // add divider
        rvProgramList.addItemDecoration(new ListItemDivider(this, LinearLayoutManager.VERTICAL, 16));
        rvProgramList.setAdapter(programListAdapter);
    }

    private void loadProgramList() {

        if (!programList.isEmpty()) {
            programList.clear();
        }

        programList.addAll(DataUtilities.getProgramList(mContext));

        Collections.sort(programList, new Comparator<Program>() {
            SimpleDateFormat format = new SimpleDateFormat(AppConstants.DATE_TIME_FORMAT, Locale.US);

            Date date1;
            Date date2;

            @Override
            public int compare(Program program1, Program program2) {
                try {
                    date1 = format.parse(program1.getTime());
                    date2 = format.parse(program2.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date1.compareTo(date2);
            }
        });

        programListAdapter.notifyDataSetChanged();
    }

    private void initListeners() {

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // // Show Banner Ads
        AdUtils.getInstance(this).showBannerAd((AdView) findViewById(R.id.adView));
    }
}
