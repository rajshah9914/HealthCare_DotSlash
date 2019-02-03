package com.uiresource.messenger;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shrikanthravi.chatview.widget.ChatView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.zagum.expandicon.ExpandIconView;
import com.shrikanthravi.chatview.widget.ChatView;
import com.shrikanthravi.chatview.data.Message;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.filter.Filter;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MessageListActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener,RatingDialogListener {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    List messageList = new ArrayList();
    ChatView chatView;
    String star="posi";
    RadioGroup radioGroup;
    RadioButton r1,r2,r3,r4,r5,r6,r7;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String url;
    EditText text;
    boolean switchbool=true;
    ImageButton send,speak;
    TextToSpeech tts;
    String msg;
    String finalcatergory;
TextView selectcategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MessageListActivity.this));
        chatView = (ChatView) findViewById(R.id.chatView);
        send=(ImageButton)findViewById(R.id.bt_send);
        tts = new TextToSpeech(this, this);


        text=(EditText)findViewById(R.id.et_message);

        speak=(ImageButton)findViewById(R.id.bt_speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
//                Message messagew = new Message();
//                messagew.setType(Message.LeftVideo);
//                //messagew.setTime(getTime());
//                messagew.setUserName("Dr. bot");
//                messagew.setVideoUri(Uri.parse("http://chahalacademyexpenditures.hostingerapp.com/video.mp4"));
//                //messagew.setVideoUri(Uri.parse(""));

                //messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));

                //chatView.addMessage(messagew);


                Message messagefirst = new Message();
                messagefirst.setBody("Hey, Welcome to Dr Bot!! Please select particular department in which you have queries.");
                messagefirst.setType(Message.LeftSimpleMessage);
                //message1.setTime(getTime());
                messagefirst.setUserName("Dr. Bot");
                //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                chatView.addMessage(messagefirst);
                msg="Hey, Welcome to Dr. Bot!! Please select particular department in which you have queries.";
                speakOut(msg);

            }
        }, 1000);

//        Message messagew = new Message();
//        messagew.setType(Message.LeftVideo);
//        //messagew.setTime(getTime());
//        messagew.setUserName("Dr. bot");
//        messagew.setVideoUri(Uri.parse("https://www.youtube.com/watch?v=WSWPgFkUUeU"));
//        messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
//        chatView.addMessage(messagew);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(finalcatergory.equals("Nutrition and Diabietes")){

                    String msg1=text.getText().toString();

                    url="https://nutrients-api.herokuapp.com/check?query="+msg1;
                    Log.d("log",url);
                    Message message = new Message();
                    message.setBody(text.getText().toString());
                    message.setType(Message.RightSimpleMessage);
                    //message.setTime(getTime());
                    message.setUserName("Me");

                    chatView.addMessage(message);
                    text.setText("");
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String res=response.getString("returenedans");
                                        String cat=response.getString("cate");
                                        if(cat.equals("image_url")){
                                            Message message11 = new Message();
                                            List<Uri> mSelected  = new ArrayList<>();
                                            mSelected.add(Uri.parse(res));
                                            //message1.setTime(getTime());
                                            message11.setType(Message.LeftSingleImage);
                                            message11.setImageList(mSelected);
                                            chatView.addMessage(message11);
                                            speakOut("Hey, here is the image for you!!");

                                        }
                                        if(cat.equals("video_url")){

                                            Message messagew = new Message();
                                            messagew.setType(Message.LeftVideo);
                                            //messagew.setTime(getTime());
                                            messagew.setUserName("Dr. bot");
                                            messagew.setVideoUri(Uri.parse(res));
                                            //messagew.setVideoUri(Uri.parse(""));

                                            messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                            chatView.addMessage(messagew);

                                        }

                                        if(cat.equals("text")){

                                            Message message1 = new Message();
                                            message1.setBody(res);
                                            message1.setType(Message.LeftSimpleMessage);
                                            //message1.setTime(getTime());

                                            //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                            chatView.addMessage(message1);
                                            speakOut(res);

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Failure Callback
                                }
                            });
// Adding the request to the queue along with a unique string tag
                    MyApplication.getInstance().addToRequestQueue(jsonObjReq, "getRequest");











                    if(switchbool) {
//                    Message message = new Message();
//                    message.setBody(text.getText().toString());
//                    message.setType(Message.RightSimpleMessage);
//                    //message.setTime(getTime());
//                    message.setUserName("Me");
//
//                    chatView.addMessage(message);
//
//                    switchbool=false;
//                    StringBuffer sb=new StringBuffer("To reduce the pain, use hot fomentation. Place a hot water bottle or electric belt on the affected area for 15-20 minutes to relax the muscles and promote healing.");
//                    String newstr=sb.substring(6);
//                    Message message1 = new Message();
//                    message1.setBody(newstr);
//                    message1.setType(Message.LeftSimpleMessage);
//                    //message1.setTime(getTime());

                        //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                        //chatView.addMessage(message1);

                        switchbool=true;

//                    Message message11 = new Message();
//                    List<Uri> mSelected  = new ArrayList<>();
//                    mSelected.add(Uri.parse("https://chahalacademyexpenditures.000webhostapp.com/msdhoni310119_0.jpeg"));
//                    //message1.setTime(getTime());
//                    message11.setType(Message.LeftSingleImage);
//                    message11.setImageList(mSelected);
//                    chatView.addMessage(message11);

                    }
                    else{

                    }
                }




                if(finalcatergory.equals("Orthopadics/Pysyotherapy Center")){
                String msg1=text.getText().toString();

                url="https://health-api-heroku.herokuapp.com/check?query="+msg1;
                Log.d("log",url);
                Message message = new Message();
                message.setBody(text.getText().toString());
                message.setType(Message.RightSimpleMessage);
                //message.setTime(getTime());
                message.setUserName("Me");

                chatView.addMessage(message);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String res=response.getString("returenedans");
                                    String cat=response.getString("cate");
                                    if(cat.equals("image_url")){
                                        Message message11 = new Message();
                                        List<Uri> mSelected  = new ArrayList<>();
                                        mSelected.add(Uri.parse(res));
                                        //message1.setTime(getTime());
                                        message11.setType(Message.LeftSingleImage);
                                        message11.setImageList(mSelected);
                                        chatView.addMessage(message11);
                                        speakOut("Hey, here is the image for you!!");

                                    }
                                    if(cat.equals("video_url")){

                                        Message messagew = new Message();
                                        messagew.setType(Message.LeftVideo);
                                        //messagew.setTime(getTime());
                                        messagew.setUserName("Dr. bot");
                                        messagew.setVideoUri(Uri.parse(res));
                                        //messagew.setVideoUri(Uri.parse(""));

                                        messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                        chatView.addMessage(messagew);

                                    }

                                    if(cat.equals("text")){

                                        Message message1 = new Message();
                                        message1.setBody(res);
                                        message1.setType(Message.LeftSimpleMessage);
                                        //message1.setTime(getTime());

                                        //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                        chatView.addMessage(message1);
                                        speakOut(res);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Failure Callback
                            }
                        });
// Adding the request to the queue along with a unique string tag
                MyApplication.getInstance().addToRequestQueue(jsonObjReq, "getRequest");











                if(switchbool) {
//                    Message message = new Message();
//                    message.setBody(text.getText().toString());
//                    message.setType(Message.RightSimpleMessage);
//                    //message.setTime(getTime());
//                    message.setUserName("Me");
//
//                    chatView.addMessage(message);
//
//                    switchbool=false;
//                    StringBuffer sb=new StringBuffer("To reduce the pain, use hot fomentation. Place a hot water bottle or electric belt on the affected area for 15-20 minutes to relax the muscles and promote healing.");
//                    String newstr=sb.substring(6);
//                    Message message1 = new Message();
//                    message1.setBody(newstr);
//                    message1.setType(Message.LeftSimpleMessage);
//                    //message1.setTime(getTime());

                    //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    //chatView.addMessage(message1);

                    switchbool=true;

                }
                else{

                }
            }}
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                showDiag();

            }
        }, 6000);


//        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
//            @Override
//            public void onSendButtonClick(String body) {
//                if(switchbool) {
//                    Message message = new Message();
//                    message.setBody(body);
//                    message.setType(Message.RightSimpleMessage);
//                    //message.setTime(getTime());
//                    message.setUserName("Me");
//
//                    chatView.addMessage(message);
//
//                    switchbool=false;
//                    StringBuffer sb=new StringBuffer("To reduce the pain, use hot fomentation. Place a hot water bottle or electric belt on the affected area for 15-20 minutes to relax the muscles and promote healing.");
//                    String newstr=sb.substring(6);
//                    Message message1 = new Message();
//                    message1.setBody(newstr);
//                    message1.setType(Message.LeftSimpleMessage);
//                    //message1.setTime(getTime());
//
//                    //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
//                    chatView.addMessage(message1);
//
//                    switchbool=true;
//                }
//                else{
//
//                }
//
//            }
//        });
//



    }
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);
                speakOut(msg);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut(String message) {

        //String text = .getText().toString();

        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        tts.setSpeechRate(2);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void showDiag() {

        //final View dialogView = View.inflate(this,R.layout.dialog,null);

        final Dialog dialog = new Dialog(MessageListActivity.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        radioGroup=(RadioGroup)dialog.findViewById(R.id.radioGroup);
        //r1=(RadioButton)dialog.findViewById(R.id.radioButton1);
        r2=(RadioButton)dialog.findViewById(R.id.radioButton2);
        r3=(RadioButton)dialog.findViewById(R.id.radioButton3);
        r4=(RadioButton)dialog.findViewById(R.id.radioButton4);
        r5=(RadioButton)dialog.findViewById(R.id.radioButton5);
        r6=(RadioButton)dialog.findViewById(R.id.radioButton6);
        r7=(RadioButton)dialog.findViewById(R.id.radioButton7);

       // selectcategory=(TextView) dialog.findViewById(R.id.selectcategory);
        Button imageView = (Button)dialog.findViewById(R.id.done);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                revealShow(dialogView, false, dialog);
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                r1 = (RadioButton)dialog.findViewById(selectedId);

                //Toast.makeText(MyAndroidAppActivity.this,
                  //      radioButton.getText(), Toast.LENGTH_SHORT).show();
                //text.setText(r1.getText().toString());
                dialog.dismiss();
                finalcatergory=r1.getText().toString();
                if(r1.getText().equals("Orthopadics/Pysyotherapy Center")) {
                    Message messagefirst1 = new Message();
                    messagefirst1.setBody("Hey, welcome to orthopedic and Physio therapy center. We will guide you along with common problems regarding injuries!!");
                    messagefirst1.setType(Message.LeftSimpleMessage);
                    //message1.setTime(getTime());
                    messagefirst1.setUserName("Dr. Bot");
                    //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    chatView.addMessage(messagefirst1);
                    msg = "Hey, welcome to orthopedic and Physio therapy center. We will guide you along with common problems regarding injuries!!";
                    speakOut(msg);

                    Message message11 = new Message();
                    List<Uri> mSelected  = new ArrayList<>();
                    mSelected.add(Uri.parse("http://chahalacademyexpenditures.hostingerapp.com/orthowelcome.jpg"));
                    //message1.setTime(getTime());
                    message11.setType(Message.LeftSingleImage);
                    message11.setImageList(mSelected);
                    chatView.addMessage(message11);

                }
                if(r1.getText().equals("Nutrition and Diabietes")) {
                    Message messagefirst1 = new Message();
                    messagefirst1.setBody("Welcome to Nutition and Diabetes center. We are here to solve your queries and give tips to be healthy");
                    messagefirst1.setType(Message.LeftSimpleMessage);
                    //message1.setTime(getTime());
                    messagefirst1.setUserName("Dr. Bot");
                    //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    chatView.addMessage(messagefirst1);
                    msg = "Welcome to Nutition and Diabetes center. We are here to solve your queries and give tips to be healthy";
                    speakOut(msg);

                    Message message11 = new Message();
                    List<Uri> mSelected  = new ArrayList<>();
                    mSelected.add(Uri.parse("http://chahalacademyexpenditures.hostingerapp.com/nutwelcome.jpg"));
                    //message1.setTime(getTime());
                    message11.setType(Message.LeftSingleImage);
                    message11.setImageList(mSelected);
                    chatView.addMessage(message11);

                }

            }

        });


//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                revealShow(dialogView, true, null);
//            }
//        });
//
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_BACK){
//
//                    revealShow(dialogView, false, dialog);
//                    return true;
//                }
//
//                return false;
//            }
//        });
//
//
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }
    private void revealShow(View dialogView, boolean b, final Dialog dialog) {

        final View view = dialogView.findViewById(R.id.dialog);

        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (text.getX() + (text.getWidth()/2));
        int cy = (int) (text.getY())+ text.getHeight() + 56;


        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(1000);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            anim.setDuration(1000);
            anim.start();
        }

    }
        @Override
        public void onBackPressed() {
            showDialog();
//            if(star.equals("positive")){
//                finish();
//                moveTaskToBack(true);
//
//            }
//            if(star.equals("neutral")){
//
//               finish();
//               moveTaskToBack(true);
//            }
//            if(star.equals("negative")){
//                Intent intent=new Intent(MessageListActivity.this,DoctorConsult.class);
//                startActivity(intent);
//
//            }

        }
    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")

                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("How did you find this conversation")
                .setDescription("Please select some stars and give your feedback. \n We will be glad to help you if you do do not get proper results!")
                .setCommentInputEnabled(true)
                .setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.colorAccent)
                .setNoteDescriptionTextColor(R.color.colorPurpleDark)
                .setTitleTextColor(R.color.colorPurpleDark)
                .setDescriptionTextColor(R.color.colorPurpleDark)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.transparent_white_hex_1)
                //.setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorTextHint)
                //.setWindowAnimation(R.style.MyAlertDialogStyle)
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .create(MessageListActivity.this)
                //.setTargetFragment(this, TAG) // only if listener is implemented by fragment
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

           //star="positive";
      if(i==1||i==2||i==3){
          Intent intent=new Intent(MessageListActivity.this,DoctorConsult.class);
          startActivity(intent);

      }
      else{
          finish();

      }


    }

    @Override
    public void onNegativeButtonClicked() {

                Intent intent=new Intent(MessageListActivity.this,DoctorConsult.class);
                startActivity(intent);

    }

    @Override
    public void onNeutralButtonClicked() {

        finish();
        moveTaskToBack(true);

    }
}

