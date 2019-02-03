package com.uiresource.messenger;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uiresource.messenger.recyclerview.Chat;
import com.uiresource.messenger.recylcerchat.ChatData;
import com.uiresource.messenger.recylcerchat.ConversationRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;


public class Conversation extends BaseActivity implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;
    ImageButton speak;
    String testsnames, date;
    String[] booktestnames;
    int totaltests;
    Button second_state_button1, second_state_button2, second_state_button3, confirmbutton, ignorebutton;
    NestedScrollView secondstate_scrollview, confirm;
    private RecyclerView mRecyclerView;
    private ConversationRecyclerView mAdapter;
    private EditText text;
    private ListView lv;
    int mYear, mMonth, mDay;
    // Listview Adapter
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> productList;

    private Button send;
    int value = 0;
    NestedScrollView nestedScrollView;
    CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior mBottomSheetBehavior;
    Button bookatest, enquireaboutorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        setupToolbarWithUpNav(R.id.toolbar, "Ask Me Your Query", R.drawable.ic_action_back);
        secondstate_scrollview = (NestedScrollView) findViewById(R.id.state_second_nestedscrolview);
        confirm = (NestedScrollView) findViewById(R.id.confirmdialog);
        confirm.setVisibility(View.GONE);
        confirmbutton = (Button) findViewById(R.id.confirmbutton);
        ignorebutton = (Button) findViewById(R.id.ignorebutton);
        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                "iPhone 4S", "Samsung Galaxy Note 800",
                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};

        lv = (ListView) findViewById(R.id.listviewid);
        lv.setVisibility(View.GONE);
        adapter = new ArrayAdapter<String>(this, R.layout.testlist, R.id.product_name, products);
        lv.setAdapter(adapter);
        secondstate_scrollview.setVisibility(View.GONE);
        second_state_button1 = (Button) findViewById(R.id.state_second_buttonone);
        second_state_button2 = (Button) findViewById(R.id.state_second_buttontwo);
        second_state_button3 = (Button) findViewById(R.id.state_second_buttonthree);
        text = (EditText) findViewById(R.id.et_message);
        speak = (ImageButton) findViewById(R.id.bt_speak);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    text.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        speak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        text.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        text.setText("");
                        text.setHint("Listening...");
                        break;
                }
                return false;
            }
        });
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatelayout);
        ///
        ///
        /// //////
        nestedScrollView = (NestedScrollView) findViewById(R.id.bottom_sheet_new);
        nestedScrollView.setVisibility(View.GONE);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        // View v=bottomSheet.findViewById(R.id.ineerlayout);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        ////////
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
        final String TAG = "abcd";
        //If you want to handle callback of Sheet Behavior you can use below code
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "State Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "State Dragging");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d(TAG, "State Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d(TAG, "State Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.d(TAG, "State Settling");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        //Implement click listeners
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ConversationRecyclerView(this, setData());

        mRecyclerView.setAdapter(mAdapter);
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 0);

        //  textToSpeech=new TextToSpeech(Conversation.this,Conversation.this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                speakWords("Hello Welcome to LabStreet !! Get your medical tests done Super easy !! How can i help you today !!");

            }
        }, 1000);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                nestedScrollView.setVisibility(View.VISIBLE);
//                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetFragment();
//                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

            }
        }, 7000);


        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }, 1000);


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 500);
            }
        });
        send = (Button) findViewById(R.id.bt_send);
//        final List<ChatData> data= new ArrayList<ChatData>();
//        final List<ChatData> data2 = new ArrayList<ChatData>();
//        final List<ChatData> data3 = new ArrayList<ChatData>();
//        final List<ChatData> data4 = new ArrayList<ChatData>();
        final ChatData item = new ChatData();
        final ChatData item2 = new ChatData();
        final ChatData item3 = new ChatData();
        final ChatData item4 = new ChatData();
        final ChatData item5 = new ChatData();
        final ChatData item6 = new ChatData();
        final ChatData item7 = new ChatData();
        final ChatData item8 = new ChatData();
        final ChatData item9 = new ChatData();
        final ChatData item10 = new ChatData();
        final ChatData item11 = new ChatData();
        final ChatData item12 = new ChatData();
        final ChatData item13 = new ChatData();
        //dialog.setContentView(contentView);
        View view = View.inflate(getApplicationContext(), R.layout.bottom_sheet_content, null);

        Button bookatest1 = (Button) findViewById(R.id.bookatestbutton);
        Button enquireaboutorder = (Button) view.findViewById(R.id.enquireaboutorder);

        bookatest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cl", "clicked kjsdksjdksdmsmdn");
                nestedScrollView.setVisibility(View.GONE);
//                Toast.makeText(Conversation.this, "clickedghhg", Toast.LENGTH_LONG).show();
                item.setTime("6:00pm");
                item.setType("2");
                //String message = text.getText().toString();
                item.setText("Book a Medical Test");
                //data.add(item);

                mAdapter.addItem(item);

                item2.setTime("6:00pm");
                item2.setType("1");
                //String message = text.getText().toString();
                item2.setText("Do you know the test name you need ?\nIf you don't you can upload doctor's prescription");
                //data2.add(item2);
                mAdapter.addItem(item2);
                speakWords("Do you know the test name that you need !! If you don't you can upload doctor's prescription");
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        secondstate_scrollview.setVisibility(View.VISIBLE);
                    }
                }, 5000);
                second_state_button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        secondstate_scrollview.setVisibility(View.GONE);
                        item3.setTime("6:00pm");
                        item3.setType("2");
                        //String message = text.getText().toString();
                        item3.setText("Yes i know the test name");
                        //      data3.add(item3);

                        mAdapter.addItem(item3);

                        item4.setTime("6:00pm");
                        item4.setType("1");
                        //String message = text.getText().toS
                        // tring();
                        item4.setText("Please enter the test's name that you need ");
                        //    data4.add(item4);
                        mAdapter.addItem(item4);
                        speakWords("Please enter the test's name that you need");
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        text.setHint("Select the test name that you need");
                        lv.setVisibility(View.GONE);
                        text.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                // When user changed the Text

                                if (adapter.isEmpty()) {
                                    lv.setVisibility(View.GONE);
                                } else {
                                    lv.setVisibility(View.VISIBLE);
                                }
                                if (text.getText().toString().equals("")) {
                                    lv.setVisibility(View.GONE);
                                }
                                Conversation.this.adapter.getFilter().filter(cs);
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                          int arg3) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }
                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item5.setTime("6:00pm");
                        item5.setType("2");
                        testsnames = text.getText().toString();
                        booktestnames = text.getText().toString().split(",");
                        totaltests = booktestnames.length;
                        //String message = text.getText().toString();
                        item5.setText(text.getText().toString());
                        //      data3.add(item3);

                        mAdapter.addItem(item5);
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        item6.setTime("6:00pm");
                        item6.setType("1");
                        //String message = text.getText().toS
                        // tring();
                        item6.setText("Can you please tell the patient's name ");
                        //    data4.add(item4);
                        mAdapter.addItem(item6);
                        speakWords("Can you please tell the patient's name !!");
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                item7.setTime("6:00pm");
                                item7.setType("2");
                                //String message = text.getText().toS
                                // tring();
                                final String name = text.getText().toString();
                                item7.setText(text.getText().toString());
                                //    data4.add(item4);
                                mAdapter.addItem(item7);
                                //speakWords("Can you please tell the patient's name !!");
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                item8.setTime("6:00pm");
                                item8.setType("1");
                                //String message = text.getText().toS
                                // tring();
                                item8.setText("At which date would you like to collect the blood sample ? ");
                                //    data4.add(item4);
                                mAdapter.addItem(item8);
                                speakWords("At which date would you like to collect the blood sample !!!");
                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Calendar mcurrentDate = Calendar.getInstance();
                                        mYear = mcurrentDate.get(Calendar.YEAR);
                                        mMonth = mcurrentDate.get(Calendar.MONTH);
                                        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                                        DatePickerDialog mDatePicker = new DatePickerDialog(Conversation.this, new DatePickerDialog.OnDateSetListener() {
                                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                                Calendar myCalendar = Calendar.getInstance();
                                                myCalendar.set(Calendar.YEAR, selectedyear);
                                                myCalendar.set(Calendar.MONTH, selectedmonth);
                                                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                                                String myFormat = "dd/MM/yy"; //Change as you need
                                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                                                date = sdf.format(myCalendar.getTime());
                                                text.setText("I want to do test's on " + sdf.format(myCalendar.getTime()));

                                                mDay = selectedday;
                                                mMonth = selectedmonth;
                                                mYear = selectedyear;
                                            }
                                        }, mYear, mMonth, mDay);
                                        //mDatePicker.setTitle("Select date");
                                        mDatePicker.show();
                                        send.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                item9.setTime("6:00pm");
                                                item9.setType("2");
                                                //String message = text.getText().toS
                                                // tring();
                                                item9.setText(text.getText().toString());
                                                //    data4.add(item4);
                                                mAdapter.addItem(item9);
                                                //speakWords("Can you please tell the patient's name !!");
                                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                                item10.setTime("6:00pm");
                                                item10.setType("1");
                                                //String message = text.getText().toS
                                                // tring();
                                                item10.setText("Finally , please tell your contact no ");
                                                //    data4.add(item4);
                                                mAdapter.addItem(item10);
                                                speakWords("Finally , please tell your contact no !!");
                                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                                send.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        item11.setTime("6:00pm");
                                                        item11.setType("2");
                                                        //String message = text.getText().toS
                                                        // tring();
                                                        String phoneno = text.getText().toString();
                                                        item11.setText(text.getText().toString());
                                                        //    data4.add(item4);
                                                        mAdapter.addItem(item11);
                                                        //speakWords("Can you please tell the patient's name !!");
                                                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                                        item12.setTime("6:00pm");
                                                        item12.setType("1");
                                                        //String message = text.getText().toS
                                                        // tring();
                                                        item12.setText("Thank you for providing all the details .\nPlease check all the details and confirm the booking \nName :" + name + "\nPhone No :" + phoneno + "\nTest Name :" + testsnames + "\nToatal Tests :" + totaltests + "\nDate Of test :" + date);
                                                        final String mesaagefinal = "Thank you for providing all the details .\nPlease check all the details and confirm the booking \nName :" + name + "\nPhone No :" + phoneno + "\nTest Name :" + testsnames + "\nToatal Tests :" + totaltests + "\nDate Of test :" + date;
                                                        //    data4.add(item4);
                                                        mAdapter.addItem(item12);
                                                        speakWords("Thank you for providing all the details . Please check all the details and confirm the booking !! ");
                                                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                confirm.setVisibility(View.VISIBLE);
                                                                confirmbutton.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        confirm.setVisibility(View.GONE);
                                                                        item13.setTime("6:00pm");
                                                                        item13.setType("1");
                                                                        //String message = text.getText().toS
                                                                        // tring();
                                                                        //String phoneno=text.getText().toString();
                                                                        item13.setText("Thank You for your booking, we will get bck to you within short time and send confirmation");
                                                                        //    data4.add(item4);
                                                                        mAdapter.addItem(item13);
                                                                        speakWords("Thank You for your booking, we will get bck to you within short time and send confirmation!! ");
                                                                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);

                                                                        }
                                                                });
                                                            }
                                                        }, 7000);
                                                    }
                                                });

                                            }
                                        });
                                    }
                                }, 4000);

                            }
                        });


                    }
                });


            }
        });

//        bookatest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("cl", "clicked kjsdksjdksdmsmdn");
//                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                mBottomSheetBehavior.setPeekHeight(0);
//
//                Toast.makeText(Conversation.this,"clicked",Toast.LENGTH_LONG).show();
//                item.setTime("6:00pm");
//                item.setType("2");
//                //String message = text.getText().toString();
//                item.setText("Book a Medical Test");
//                data.add(item);
//                mAdapter.addItem(data);
//                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
//
//            }
//        });
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!text.getText().equals("")) {
//                    item.setTime("6:00pm");
//                    item.setType("2");
//                    String message = text.getText().toString();
//                    item.setText(text.getText().toString());
//                    //data.add(item);
//                    //mAdapter.addItem(data);
//                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
//                    text.setText("");
//                    final String JSON_URL = "https://aayushpatel.herokuapp.com/check?text=" + message;
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                Log.d("status", "loading");
//                                JSONObject jsonObject = new JSONObject(response);
//                                String res = jsonObject.getString("input");
//                                ChatData item2 = new ChatData();
//                                item2.setTime("6:01pm");
//                                item2.setType("1");
//                                item2.setText(res);
//                      //          data2.add(item2);
//                      //          mAdapter.addItem(data2);
//                                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
//                                Log.d("status", "loaded");
//                            } catch (JSONException e) {
//                                Log.d("status", "error exception");
//                                e.printStackTrace();
//
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(Conversation.this, "error" + error, Toast.LENGTH_LONG).show();
//                            Log.d("status", String.valueOf(error));
//                        }
//                    });
//                    RequestQueue requestQueue = Volley.newRequestQueue(Conversation.this);
//                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                            10000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                    //adding the string request to request queue
//                    requestQueue.add(stringRequest);
//                    //text.setText("");
//                }
//            }
//        });
//
    }

    public List<ChatData> setData() {
        List<ChatData> data = new ArrayList<>();

//        String text[] = {"15 September","Hi, Julia! How are you?", "Hi, Joe, looks great! :) ", "I'm fine. Wanna go out somewhere?", "Yes! Coffe maybe?", "Great idea! You can come 9:00 pm? :)))", "Ok!", "Ow my good, this Kit is totally awesome", "Can you provide other kit?", "I don't have much time, :`("};
//        String time[] = {"", "5:30pm", "5:35pm", "5:36pm", "5:40pm", "5:41pm", "5:42pm", "5:40pm", "5:41pm", "5:42pm"};
//        String type[] = {"0", "2", "1", "1", "2", "1", "2", "2", "2", "1"};
        String text[] = {"15 September", "Hello Welcome to LabStreet \nGet your medical tests done Super easy \nGet your blood sample collected in comfort of your home \nHow can we help you today ?"};
        String time[] = {"", "5:40pm"};
        String type[] = {"0", "1"};


        for (int i = 0; i < text.length; i++) {
            ChatData item = new ChatData();
            item.setType(type[i]);
            item.setText(text[i]);
            item.setTime(time[i]);
            data.add(item);

        }

        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_userphoto, menu);
        return true;
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                Log.d("new", "no");
                textToSpeech = new TextToSpeech(this, this);
//                String msg=data.getStringExtra("RESULT_STRING");
//                speakWords(msg);
//                Log.d("new",msg);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    private void speakWords(String speech) {

        //speak straight away

        textToSpeech.setSpeechRate(2.0f);
        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {

            if (textToSpeech.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                textToSpeech.setLanguage(Locale.US);
            }
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetFragment();
                            bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");

                        }
                    });
                }

                @Override
                public void onError(String s) {

                }
            });
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

//
//    @Override
//    public void onInit(int status) {
//        if (status == TextToSpeech.SUCCESS) {
//
//            int result = textToSpeech.setLanguage(Locale.US);
//
//            if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "This Language is not supported");
//            } else {
//
//
//
//                textToSpeech.speak("Hello Welcome to LabStreet !! Get your medical tests done Super easy !! Get your blood sample collected in comfort of your home !! How can we help you today ?", TextToSpeech.QUEUE_FLUSH, null);
//            }
//
//        } else {
//            Log.e("TTS", "Initilization Failed!");
//        }
//    }
}
