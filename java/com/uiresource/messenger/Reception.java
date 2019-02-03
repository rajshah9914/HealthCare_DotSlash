package com.uiresource.messenger;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Reception extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    ImageButton send,speak;
    String msg;
    String url;
    String selhospital;
    LinearLayout l1,l2,l3;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ChatView chatView;
    EditText text;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Reception.this));
        chatView=(ChatView)findViewById(R.id.chatView1);
        send=(ImageButton)findViewById(R.id.bt_send1);
        tts = new TextToSpeech(this, this);


        text=(EditText)findViewById(R.id.et_message1);

        speak=(ImageButton)findViewById(R.id.bt_speak1);
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

                Message messagefirst = new Message();
                messagefirst.setBody("Hey, Welcome to Dr. Bot!! Please select particular hospital with whom you want to talk !!");
                messagefirst.setType(Message.LeftSimpleMessage);
                //message1.setTime(getTime());
                messagefirst.setUserName("Dr. Bot");
                //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                chatView.addMessage(messagefirst);
                msg="Hey, Welcome to Dr. Bot!! Please select particular hospital with whom you want to talk !!";
                speakOut(msg);

            }
        }, 1000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                showDiag();

            }
        }, 3000);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selhospital.equals("apollo")){

                    String msg1=text.getText().toString();

                    url="https://health-reception-one.herokuapp.com/check?query="+msg1;
                    Log.d("log",url);
                    final Message message = new Message();
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
                                      if(cat.equals("call")){

                                          Intent callIntent = new Intent(Intent.ACTION_CALL);
                                          callIntent.setData(Uri.parse("tel:"+res));

                                          if (ActivityCompat.checkSelfPermission(Reception.this,
                                                  Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                              return;
                                          }
                                          startActivity(callIntent);
                                          Message messagew = new Message();

                                          messagew.setType(Message.LeftSimpleMessage);
                                          messagew.setBody("Here is the contact no:"+res);
                                          //messagew.setTime(getTime());
                                          messagew.setUserName("Dr. bot");
                                          //messagew.setVideoUri(Uri.parse(res));
                                          //messagew.setVideoUri(Uri.parse(""));
                                            //messagew.setBody(Message.LeftSimpleMessage);
                                          messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                          chatView.addMessage(messagew);
                                          speakOut("Wait a minute, We are making call for you");

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
                                            speakOut("Hey, You might want to Look at this video");
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

                    MyApplication.getInstance().addToRequestQueue(jsonObjReq, "getRequest");

                }




                if(selhospital.equals("fortis")){
                    String msg1=text.getText().toString();

                    url="https://health-reception-two.herokuapp.com/check?query="+msg1;
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
                                        if(cat.equals("call")){

                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:"+res));

                                            if (ActivityCompat.checkSelfPermission(Reception.this,
                                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            startActivity(callIntent);
                                            Message messagew = new Message();

                                            messagew.setType(Message.LeftSimpleMessage);
                                            messagew.setBody("Here is the contact no:"+res);
                                            //messagew.setTime(getTime());
                                            messagew.setUserName("Dr. bot");
                                            //messagew.setVideoUri(Uri.parse(res));
                                            //messagew.setVideoUri(Uri.parse(""));
                                            //messagew.setBody(Message.LeftSimpleMessage);
                                            messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                            chatView.addMessage(messagew);
                                            speakOut("Wait a minute, We are making call for you");

                                        }
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
                                            speakOut("Hey, You might want to Look at this video");
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

                    MyApplication.getInstance().addToRequestQueue(jsonObjReq, "getRequest");












                }

                if(selhospital.equals("max")){

                    String msg1=text.getText().toString();

                    url="https://health-reception-three.herokuapp.com/check?query="+msg1;
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
                                        if(cat.equals("call")){

                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:"+res));

                                            if (ActivityCompat.checkSelfPermission(Reception.this,
                                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            startActivity(callIntent);
                                            Message messagew = new Message();

                                            messagew.setType(Message.LeftSimpleMessage);
                                            messagew.setBody("Here is the contact no:"+res);
                                            //messagew.setTime(getTime());
                                            messagew.setUserName("Dr. bot");
                                            //messagew.setVideoUri(Uri.parse(res));
                                            //messagew.setVideoUri(Uri.parse(""));
                                            //messagew.setBody(Message.LeftSimpleMessage);
                                            messagew.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                                            chatView.addMessage(messagew);
                                            speakOut("Wait a minute, We are making call for you");

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
                                            speakOut("Hey, You might want to Look at this video");
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

                    MyApplication.getInstance().addToRequestQueue(jsonObjReq, "getRequest");

                }






            }
        });



    }

    private void showDiag() {

        //final View dialogView = View.inflate(this,R.layout.dialog,null);

        final Dialog dialog = new Dialog(Reception.this);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.receptiondialog);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
dialog.show();
        l1=(LinearLayout)dialog.findViewById(R.id.hos1);
        l2=(LinearLayout)dialog.findViewById(R.id.hos2);
        l3=(LinearLayout)dialog.findViewById(R.id.hos3);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selhospital="apollo";
                Message messagefirst1 = new Message();
                messagefirst1.setBody("Hey, Welcome to Apollo Hospital.");
                messagefirst1.setType(Message.LeftSimpleMessage);
                //message1.setTime(getTime());
                messagefirst1.setUserName("Dr. Bot");
                //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                chatView.addMessage(messagefirst1);
                msg = "Hey, Welcome to Apollo Hospital.";
                speakOut(msg);
                dialog.dismiss();
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selhospital="fortis";
                Message messagefirst1 = new Message();
                messagefirst1.setBody("Hey, Welcome to Fortis Hospital");
                messagefirst1.setType(Message.LeftSimpleMessage);
                //message1.setTime(getTime());
                messagefirst1.setUserName("Dr. Bot");
                //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                chatView.addMessage(messagefirst1);
                msg = "Hey, Welcome to Fortis Hospital";
                speakOut(msg);
                dialog.dismiss();
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selhospital="max";
                Message messagefirst1 = new Message();
                messagefirst1.setBody("Hey, Welcome to Max Insurance Hospitals");
                messagefirst1.setType(Message.LeftSimpleMessage);
                //message1.setTime(getTime());
                messagefirst1.setUserName("Dr. Bot");
                //message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                chatView.addMessage(messagefirst1);
                msg = "Hey, Welcome to Max Insurance Hospitals";
                speakOut(msg);
                dialog.dismiss();
            }
        });
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

}
