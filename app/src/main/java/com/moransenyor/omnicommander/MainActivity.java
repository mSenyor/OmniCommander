package com.moransenyor.omnicommander;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {




    // server stuff
    public static boolean isConnected=false;
    private Socket socket;
    public static PrintWriter out;

    // tab buttons
    Button go_tab, object_tab, assign_tab, store_tab, num_tab, next_tab;
    private final String GO_TAB = "com.moransenyor.omnicommander.MainActivity.GO";
    private final String OBJECT_TAB = "com.moransenyor.omnicommander.MainActivity.OBJECT_TAB";
    private final String ASSIGN_TAB = "com.moransenyor.omnicommander.MainActivity.ASSIGN_TAB";
    private final String STORE_TAB = "com.moransenyor.omnicommander.MainActivity.STORE_TAB";
    private final String NUM_TAB = "com.moransenyor.omnicommander.MainActivity.NUM_TAB";
    private final String NEXT_TAB = "com.moransenyor.omnicommander.MainActivity.NEXT_TAB";

    LinearLayout go_layout, object_layout, assign_layout, store_layout, num_layout, next_layout;


    // go pad buttons
    Button fix_btn, select_btn, off_btn, temp_btn, top_btn, on_btn, stepback_btn, learn_btn,
            stepforward_btn, gominus_btn, pause_btn, goplus_btn;

    // object pad buttons
    Button view_btn, effect_btn, goto_btn, page_btn, macro_btn, preset_btn, sequ_btn, cue_btn,
            exec_btn, channel_btn, fixture_btn, group_btn;

    // assign pad buttons
    Button del_btn, blind_btn, copy_btn, freeze_btn, prvw_btn, move_btn, assign_btn, align_btn;

    // store pad buttons
    Button time_btn, esc_btn, edit_btn, oops_btn, update_btn, clear_btn, store_btn;

    // num pad buttons
    Button num7_btn, num8_btn, num9_btn, plus_btn, num4_btn, num5_btn, num6_btn, thru_btn,
            num1_btn, num2_btn, num3_btn, minus_btn, num0_btn, dot_btn, if_btn, at_btn, ma_btn,
            please_btn;

    // next pad buttons
    Button full_btn, highlight_btn, solo_btn, up_btn, prev_btn, set_btn, next_btn, down_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // settings stuff
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Constants.setPassword(preferences.getString("password_text", "key"));
        Constants.setServerPort(Integer.valueOf(preferences.getString("port_text", "2102")));
        Constants.setServerIp(preferences.getString("ip_text", "10.0.0.1"));

        // setup the tabs
        go_tab = (Button) findViewById(R.id.go_pad_button);
        object_tab = (Button) findViewById(R.id.object_pad_button);
        assign_tab = (Button) findViewById(R.id.assign_pad_button);
        store_tab = (Button) findViewById(R.id.store_pad_button);
        num_tab = (Button) findViewById(R.id.num_pad_button);
        next_tab = (Button) findViewById(R.id.next_pad_button);
        selectedTab(go_tab);

        go_layout = (LinearLayout) findViewById(R.id.go_pad_toggle);
        object_layout = (LinearLayout) findViewById(R.id.object_pad_toggle);
        assign_layout = (LinearLayout) findViewById(R.id.assign_pad_toggle);
        store_layout = (LinearLayout) findViewById(R.id.store_pad_toggle);
        num_layout = (LinearLayout) findViewById(R.id.num_pad_toggle);
        next_layout = (LinearLayout) findViewById(R.id.next_pad_toggle);

        go_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(GO_TAB);
            }
        });
        object_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(OBJECT_TAB);
            }
        });
        assign_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(ASSIGN_TAB);
            }
        });
        store_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(STORE_TAB);
            }
        });
        num_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(NUM_TAB);
            }
        });
        next_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabNav(NEXT_TAB);
            }
        });

        // setup the buttons
        // go pad buttons
        fix_btn = (Button) findViewById(R.id.grand_ma2_fix_button);
        select_btn = (Button) findViewById(R.id.grand_ma2_select_button);
        off_btn = (Button) findViewById(R.id.grand_ma2_off_button);
        temp_btn = (Button) findViewById(R.id.grand_ma2_temp_button);
        top_btn = (Button) findViewById(R.id.grand_ma2_top_button);
        on_btn = (Button) findViewById(R.id.grand_ma2_on_button);
        stepback_btn = (Button) findViewById(R.id.grand_ma2_stepback_button);
        learn_btn = (Button) findViewById(R.id.grand_ma2_learn_button);
        stepforward_btn = (Button) findViewById(R.id.grand_ma2_stepforward_button);
        gominus_btn = (Button) findViewById(R.id.grand_ma2_gominus_button);
        pause_btn = (Button) findViewById(R.id.grand_ma2_pause_button);
        goplus_btn = (Button) findViewById(R.id.grand_ma2_goplus_button);

/*
        fix_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_FIX_BUTTON);
                }
            }
        });*/

        buttonListener(fix_btn, Constants.GRANDMA2_FIX_BUTTON);
/*
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_SELECT_BUTTON);
                }
            }
        });*/
        buttonListener(select_btn, Constants.GRANDMA2_SELECT_BUTTON);
/*
        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_OFF_BUTTON);
                }
            }
        });*/

        buttonListener(off_btn, Constants.GRANDMA2_OFF_BUTTON);
/*
        temp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_TEMP_BUTTON);
                }
            }
        });*/

        buttonListener(temp_btn, Constants.GRANDMA2_TEMP_BUTTON);
/*
        top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_TOP_BUTTON);
                }
            }
        });*/

        buttonListener(top_btn, Constants.GRANDMA2_TOP_BUTTON);
/*
        on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_ON_BUTTON);
                }
            }
        });*/

        buttonListener(on_btn, Constants.GRANDMA2_ON_BUTTON);
/*
        stepback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_STEPBACK_BUTTON);
                }
            }
        });*/

        buttonListener(stepback_btn, Constants.GRANDMA2_STEPBACK_BUTTON);
/*
        learn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_LEARN_BUTTON);
                }
            }
        });*/

        buttonListener(learn_btn, Constants.GRANDMA2_LEARN_BUTTON);
/*
        stepforward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_STEPFORWARD_BUTTON);
                }
            }
        });*/

        buttonListener(stepforward_btn, Constants.GRANDMA2_STEPFORWARD_BUTTON);
/*
        gominus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_GOMINUS_BUTTON);
                }
            }
        });*/

        buttonListener(gominus_btn, Constants.GRANDMA2_GOMINUS_BUTTON);
/*
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PAUSE_BUTTON);
                }
            }
        });*/

        buttonListener(pause_btn, Constants.GRANDMA2_PAUSE_BUTTON);
/*
        goplus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_GOPLUS_BUTTON);
                }
            }
        });*/

        buttonListener(goplus_btn, Constants.GRANDMA2_GOPLUS_BUTTON);



/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        // item pad buttons
        view_btn = (Button) findViewById(R.id.grand_ma2_view_button);
        effect_btn = (Button) findViewById(R.id.grand_ma2_effect_button);
        goto_btn = (Button) findViewById(R.id.grand_ma2_goto_button);
        page_btn = (Button) findViewById(R.id.grand_ma2_page_button);
        macro_btn = (Button) findViewById(R.id.grand_ma2_macro_button);
        preset_btn = (Button) findViewById(R.id.grand_ma2_preset_button);
        sequ_btn = (Button) findViewById(R.id.grand_ma2_sequ_button);
        cue_btn = (Button) findViewById(R.id.grand_ma2_cue_button);
        exec_btn = (Button) findViewById(R.id.grand_ma2_exec_button);
        channel_btn = (Button) findViewById(R.id.grand_ma2_channel_button);
        fixture_btn = (Button) findViewById(R.id.grand_ma2_fixture_button);
        group_btn = (Button) findViewById(R.id.grand_ma2_group_button);
/*
        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_VIEW_BUTTON);
                }
            }
        });*/

        buttonListener(view_btn, Constants.GRANDMA2_VIEW_BUTTON);
/*
        effect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_EFFECT_BUTTON);
                }
            }
        });*/

        buttonListener(effect_btn, Constants.GRANDMA2_EFFECT_BUTTON);
/*
        goto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_GOTO_BUTTON);
                }
            }
        });*/

        buttonListener(goto_btn, Constants.GRANDMA2_GOTO_BUTTON);
/*
        page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PAGE_BUTTON);
                }
            }
        });*/

        buttonListener(page_btn, Constants.GRANDMA2_PAGE_BUTTON);
/*
        macro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_MACRO_BUTTON);
                }
            }
        });*/

        buttonListener(macro_btn, Constants.GRANDMA2_MACRO_BUTTON);
/*
        preset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PRESET_BUTTON);
                }
            }
        });*/

        buttonListener(preset_btn, Constants.GRANDMA2_PRESET_BUTTON);
/*
        sequ_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_SEQU_BUTTON);
                }
            }
        });*/

        buttonListener(sequ_btn, Constants.GRANDMA2_SEQU_BUTTON);
/*
        cue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_CUE_BUTTON);
                }
            }
        });*/

        buttonListener(cue_btn, Constants.GRANDMA2_CUE_BUTTON);
/*
        exec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_EXEC_BUTTON);
                }
            }
        });*/

        buttonListener(exec_btn, Constants.GRANDMA2_EXEC_BUTTON);
/*
        channel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_CHANNEL_BUTTON);
                }
            }
        });*/

        buttonListener(channel_btn, Constants.GRANDMA2_CHANNEL_BUTTON);
/*
        fixture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_FIXTURE_BUTTON);
                }
            }
        });*/

        buttonListener(fixture_btn, Constants.GRANDMA2_FIXTURE_BUTTON);
/*
        group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_GROUP_BUTTON);
                }
            }
        });*/

        buttonListener(group_btn, Constants.GRANDMA2_GROUP_BUTTON);



        // assign pad buttons
        del_btn = (Button) findViewById(R.id.grand_ma2_del_button);
        blind_btn = (Button) findViewById(R.id.grand_ma2_blind_button);
        copy_btn = (Button) findViewById(R.id.grand_ma2_copy_button);
        freeze_btn = (Button) findViewById(R.id.grand_ma2_freeze_button);
        prvw_btn = (Button) findViewById(R.id.grand_ma2_prvw_button);
        move_btn = (Button) findViewById(R.id.grand_ma2_move_button);
        assign_btn = (Button) findViewById(R.id.grand_ma2_assign_button);
        align_btn = (Button) findViewById(R.id.grand_ma2_align_button);
/*
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_DEL_BUTTON);
                }
            }
        });*/

        buttonListener(del_btn, Constants.GRANDMA2_DEL_BUTTON);
/*
        blind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_BLIND_BUTTON);
                }
            }
        });*/

        buttonListener(blind_btn, Constants.GRANDMA2_BLIND_BUTTON);
/*
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_COPY_BUTTON);
                }
            }
        });*/

        buttonListener(copy_btn, Constants.GRANDMA2_COPY_BUTTON);
/*
        freeze_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_FREEZE_BUTTON);
                }
            }
        });*/

        buttonListener(freeze_btn, Constants.GRANDMA2_FREEZE_BUTTON);
/*
        prvw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PRVW_BUTTON);
                }
            }
        });*/

        buttonListener(prvw_btn, Constants.GRANDMA2_PRVW_BUTTON);
/*
        move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_MOVE_BUTTON);
                }
            }
        });*/

        buttonListener(move_btn, Constants.GRANDMA2_MOVE_BUTTON);
/*
        assign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_ASSIGN_BUTTON);
                }
            }
        });*/

        buttonListener(assign_btn, Constants.GRANDMA2_ASSIGN_BUTTON);
/*
        align_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_ALIGN_BUTTON);
                }
            }
        });*/

        buttonListener(align_btn, Constants.GRANDMA2_ALIGN_BUTTON);



        // store pad buttons
        time_btn = (Button) findViewById(R.id.grand_ma2_time_button);
        esc_btn = (Button) findViewById(R.id.grand_ma2_esc_button);
        edit_btn = (Button) findViewById(R.id.grand_ma2_edit_button);
        oops_btn = (Button) findViewById(R.id.grand_ma2_oops_button);
        update_btn = (Button) findViewById(R.id.grand_ma2_update_button);
        clear_btn = (Button) findViewById(R.id.grand_ma2_clear_button);
        store_btn = (Button) findViewById(R.id.grand_ma2_store_button);
/*
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_TIME_BUTTON);
                }
            }
        });*/

        buttonListener(time_btn, Constants.GRANDMA2_TIME_BUTTON);
/*
        esc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_ESC_BUTTON);
                }
            }
        });*/

        buttonListener(esc_btn, Constants.GRANDMA2_ESC_BUTTON);
/*
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_EDIT_BUTTON);
                }
            }
        });*/

        buttonListener(edit_btn, Constants.GRANDMA2_EDIT_BUTTON);
/*
        oops_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_OOPS_BUTTON);
                }
            }
        });*/

        buttonListener(oops_btn, Constants.GRANDMA2_OOPS_BUTTON);
/*
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_UPDATE_BUTTON);
                }
            }
        });*/

        buttonListener(update_btn, Constants.GRANDMA2_UPDATE_BUTTON);
/*
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_CLEAR_BUTTON);
                }
            }
        });*/

        buttonListener(clear_btn, Constants.GRANDMA2_CLEAR_BUTTON);
/*
        store_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_STORE_BUTTON);
                }
            }
        });*/

        buttonListener(store_btn, Constants.GRANDMA2_STORE_BUTTON);



        // num pad buttons
        num7_btn = (Button) findViewById(R.id.grand_ma2_7_button);
        num8_btn = (Button) findViewById(R.id.grand_ma2_8_button);
        num9_btn = (Button) findViewById(R.id.grand_ma2_9_button);
        plus_btn = (Button) findViewById(R.id.grand_ma2_plus_button);
        num4_btn = (Button) findViewById(R.id.grand_ma2_4_button);
        num5_btn = (Button) findViewById(R.id.grand_ma2_5_button);
        num6_btn = (Button) findViewById(R.id.grand_ma2_6_button);
        thru_btn = (Button) findViewById(R.id.grand_ma2_thru_button);
        num1_btn = (Button) findViewById(R.id.grand_ma2_1_button);
        num2_btn = (Button) findViewById(R.id.grand_ma2_2_button);
        num3_btn = (Button) findViewById(R.id.grand_ma2_3_button);
        minus_btn = (Button) findViewById(R.id.grand_ma2_minus_button);
        num0_btn = (Button) findViewById(R.id.grand_ma2_0_button);
        dot_btn = (Button) findViewById(R.id.grand_ma2_dot_button);
        if_btn = (Button) findViewById(R.id.grand_ma2_if_button);
        at_btn = (Button) findViewById(R.id.grand_ma2_at_button);
        ma_btn = (Button) findViewById(R.id.grand_ma2_ma_button);
        please_btn = (Button) findViewById(R.id.grand_ma2_please_button);
/*
        num7_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_7_BUTTON);
                }
            }
        });*/

        buttonListener(num7_btn, Constants.GRANDMA2_7_BUTTON);
/*
        num8_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_8_BUTTON);
                }
            }
        });*/

        buttonListener(num8_btn, Constants.GRANDMA2_8_BUTTON);
/*
        num9_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_9_BUTTON);
                }
            }
        });*/

        buttonListener(num9_btn, Constants.GRANDMA2_9_BUTTON);
/*
        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PLUS_BUTTON);
                }
            }
        });*/

        buttonListener(plus_btn, Constants.GRANDMA2_PLUS_BUTTON);
/*
        num4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_4_BUTTON);
                }
            }
        });*/

        buttonListener(num4_btn, Constants.GRANDMA2_4_BUTTON);
/*
        num5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_5_BUTTON);
                }
            }
        });*/

        buttonListener(num5_btn, Constants.GRANDMA2_5_BUTTON);
/*
        num6_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_6_BUTTON);
                }
            }
        });*/

        buttonListener(num6_btn, Constants.GRANDMA2_6_BUTTON);
/*
        thru_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_THRU_BUTTON);
                }
            }
        });*/

        buttonListener(thru_btn, Constants.GRANDMA2_THRU_BUTTON);
/*
        num1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_1_BUTTON);
                }
            }
        });*/

        buttonListener(num1_btn, Constants.GRANDMA2_1_BUTTON);
/*
        num2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_2_BUTTON);
                }
            }
        });*/

        buttonListener(num2_btn, Constants.GRANDMA2_2_BUTTON);
/*
        num3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_3_BUTTON);
                }
            }
        });
        */

        buttonListener(num3_btn, Constants.GRANDMA2_3_BUTTON);
/*
        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_MINUS_BUTTON);
                }
            }
        });*/

        buttonListener(minus_btn, Constants.GRANDMA2_MINUS_BUTTON);
/*
        num0_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_0_BUTTON);
                }
            }
        });*/

        buttonListener(num0_btn, Constants.GRANDMA2_0_BUTTON);
/*
        dot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_DOT_BUTTON);
                }
            }
        });*/

        buttonListener(dot_btn, Constants.GRANDMA2_DOT_BUTTON);
/*
        if_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_IF_BUTTON);
                }
            }
        });*/

        buttonListener(if_btn, Constants.GRANDMA2_IF_BUTTON);
/*
        at_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_AT_BUTTON);
                }
            }
        });*/

        buttonListener(at_btn, Constants.GRANDMA2_AT_BUTTON);
/*
        ma_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_MA_BUTTON);
                }
            }
        });*/

        buttonListener(ma_btn, Constants.GRANDMA2_MA_BUTTON);
/*
        please_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PLEASE_BUTTON);
                }
            }
        });*/

        buttonListener(please_btn, Constants.GRANDMA2_PLEASE_BUTTON);



        // next pad buttons
        full_btn = (Button) findViewById(R.id.grand_ma2_full_button);
        highlight_btn = (Button) findViewById(R.id.grand_ma2_highlight_button);
        solo_btn = (Button) findViewById(R.id.grand_ma2_solo_button);
        up_btn = (Button) findViewById(R.id.grand_ma2_up_button);
        prev_btn = (Button) findViewById(R.id.grand_ma2_prev_button);
        set_btn = (Button) findViewById(R.id.grand_ma2_set_button);
        next_btn = (Button) findViewById(R.id.grand_ma2_next_button);
        down_btn = (Button) findViewById(R.id.grand_ma2_down_button);
/*
        full_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_FULL_BUTTON);
                }
            }
        });*/

        buttonListener(full_btn, Constants.GRANDMA2_FULL_BUTTON);
/*
        highlight_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_HIGHLIGHT_BUTTON);
                }
            }
        });*/

        buttonListener(highlight_btn, Constants.GRANDMA2_HIGHLIGHT_BUTTON);
/*
        solo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_SOLO_BUTTON);
                }
            }
        });*/

        buttonListener(solo_btn, Constants.GRANDMA2_SOLO_BUTTON);
/*
        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_UP_BUTTON);
                }
            }
        });*/

        buttonListener(up_btn, Constants.GRANDMA2_UP_BUTTON);
/*
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_PREV_BUTTON);
                }
            }
        });*/

        buttonListener(prev_btn, Constants.GRANDMA2_PREV_BUTTON);
/*
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_SET_BUTTON);
                }
            }
        });*/

        buttonListener(set_btn, Constants.GRANDMA2_SET_BUTTON);
/*
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_NEXT_BUTTON);
                }
            }
        });*/

        buttonListener(next_btn, Constants.GRANDMA2_NEXT_BUTTON);
/*
        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected && out!=null){
                    out.println(Constants.GRANDMA2_DOWN_BUTTON);
                }
            }
        });*/

        buttonListener(down_btn, Constants.GRANDMA2_DOWN_BUTTON);

    }

    private void selectedTab(Button btn){
        btn.setBackground(super.getResources().getDrawable(R.drawable.grand_ma2_current_tab));
        btn.setTextColor(super.getResources().getColor(R.color.grandMA2yellow));
    }

    private void normalizedTab(Button btn){
        btn.setBackground(super.getResources().getDrawable(R.drawable.grand_ma2_button));
        btn.setTextColor(Color.WHITE);
    }

    private void tabNav(String tab){
        List<Button> allTabs = new ArrayList<>();
        allTabs.add(go_tab);
        allTabs.add(object_tab);
        allTabs.add(assign_tab);
        allTabs.add(store_tab);
        allTabs.add(num_tab);
        allTabs.add(next_tab);

        for (int i = 0; i < allTabs.size(); i++) {
            normalizedTab(allTabs.get(i));
        }

        // tab visibility
        go_layout.setVisibility(LinearLayout.GONE);
        object_layout.setVisibility(LinearLayout.GONE);
        assign_layout.setVisibility(LinearLayout.GONE);
        store_layout.setVisibility(LinearLayout.GONE);
        num_layout.setVisibility(LinearLayout.GONE);
        next_layout.setVisibility(LinearLayout.GONE);

        switch (tab){
            case GO_TAB:
                // TODO: 16/02/17
                go_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(go_tab);
                break;
            case OBJECT_TAB:
                // TODO: 16/02/17
                object_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(object_tab);
                break;
            case ASSIGN_TAB:
                // TODO: 16/02/17
                assign_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(assign_tab);
                break;
            case STORE_TAB:
                // TODO: 16/02/17
                store_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(store_tab);
                break;
            case NUM_TAB:
                // TODO: 16/02/17
                num_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(num_tab);
                break;
            case NEXT_TAB:
                // TODO: 16/02/17
                next_layout.setVisibility(LinearLayout.VISIBLE);
                selectedTab(next_tab);
                break;
        }
    }

    private void buttonListener(final Button btn, final String str){

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.grand_ma2_pressed_button));
                    if(isConnected && out!=null){
                        out.println(str);
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.grand_ma2_button));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_connect) {
            ConnectNow();
        }
        if (id == R.id.action_disconnect) {
            if(isConnected && out!=null){
                out.println("exit");
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void ConnectNow(){
        ConnectPhoneTask connectPhoneTask = new ConnectPhoneTask();
        connectPhoneTask.execute(Constants.SERVER_IP); //try to connect to server in another thread
    }

    public class ConnectPhoneTask extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            try {
                InetAddress serverAddr = InetAddress.getByName(params[0]);
                socket = new Socket(serverAddr, Constants.SERVER_PORT);//Open socket on server IP and port
            } catch (IOException e) {
                Log.e("remotedroid", "Error while connecting", e);
                result = false;
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean result)
        {
            isConnected = result;
            Toast.makeText(getApplicationContext(),isConnected?"Connected to server!":"Error while connecting",Toast.LENGTH_LONG).show();
            try {
                if(isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true); //create output stream to send data to server
                }
            }catch (IOException e){
                Log.e("remotedroid", "Error while creating OutWriter", e);
                Toast.makeText(getApplicationContext(),"Error while connecting",Toast.LENGTH_LONG).show();
            }
        }
    }
}
