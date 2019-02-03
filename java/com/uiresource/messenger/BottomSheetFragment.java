package com.uiresource.messenger;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uiresource.messenger.recylcerchat.ChatData;
import com.uiresource.messenger.recylcerchat.ConversationRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.uiresource.messenger.R.layout.content_conversation;


public class BottomSheetFragment extends BottomSheetDialogFragment {
    private RecyclerView mRecyclerView;
    private ConversationRecyclerView mAdapter;

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(contentView);

        Button bookatest = (Button) contentView.findViewById(R.id.bookatestbutton);
        Button enquireaboutorder = (Button) contentView.findViewById(R.id.enquireaboutorder);
        final List<ChatData> data = new ArrayList<ChatData>();
        final List<ChatData> data2 = new ArrayList<ChatData>();
        final ChatData item = new ChatData();
        View layoutInflater=View.inflate(getContext(),R.layout.activity_conversation,null);
        //View contentView1 = LayoutInflater.from(R.layout.content_conversation);
        final EditText editText=(EditText)layoutInflater.findViewById(R.id.et_message);
        mRecyclerView = (RecyclerView)layoutInflater. findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mAdapter = new ConversationRecyclerView(getContext(),data);

  //      mRecyclerView.setAdapter(mAdapter);
//
//
//        bookatest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("cl", "clicked kjsdksjdksdmsmdn");
//                dialog.dismiss();
//                editText.setText("Book me Medical Test");
//                Toast.makeText(getContext(), "clicked", Toast.LENGTH_LONG).show();
//                item.setTime("6:00pm");
//                item.setType("2");
//                //String message = text.getText().toString();
//                item.setText("Book a Medical Test");
//                data.add(item);
//                mAdapter=new ConversationRecyclerView(getContext(),data);
//      //          mAdapter.addItem(data);
//                mRecyclerView.setAdapter(mAdapter);
//    //            mAdapter.notifyDataSetChanged();
//                //mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
//
//
//
//            }
//        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

}
