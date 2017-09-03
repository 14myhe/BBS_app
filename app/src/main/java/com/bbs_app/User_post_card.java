package com.bbs_app;

/**
 * Created by Nicole on 2016/12/15.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class User_post_card extends Activity {

    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;
    public static final int CHOOSE_PHOTO=3;

    private Button takePhoto;
    private Button chooseFromAlbum;
    private ImageView picture;

    private Uri imageUri;
    public static final int SHOW_success=1;
    public static final int NOT_Login=2;
    public static final int SHOW_fail=3;

  /*  private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case SHOW_success:
                    Toast.makeText(User_post_card.this, "发帖成功", Toast.LENGTH_SHORT).show();
                    break;
                case NOT_Login:
                    Toast.makeText(User_post_card.this, "尚未登录，不能发帖", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_fail:
                    Toast.makeText(User_post_card.this, "发帖失败", Toast.LENGTH_SHORT).show();
                    break;
                default:break;




            }
        }

    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_message);

        //获取各个组件
        takePhoto=(Button)findViewById(R.id.post_message).findViewById(R.id.take_photo);
        chooseFromAlbum=(Button)findViewById(R.id.post_message).findViewById(R.id.select_photo);
        picture=(ImageView)findViewById(R.id.post_message).findViewById(R.id.photo);


        //上传图片
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建File对象，用于存储图片
                File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image_jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);//启动相机程序
            }
        });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType ("image/*");
                startActivityForResult(intent,CHOOSE_PHOTO);
            }
        });


        //点击提交按钮
        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //获取layout
                View post_msg=findViewById(R.id.post_message);
                //获得用户的输入
                EditText cardtitle_edittext=(EditText)post_msg.findViewById(R.id.title);
                EditText cardcontent_edittext=(EditText)post_msg.findViewById(R.id.content);
                EditText price_edittext=(EditText)post_msg.findViewById(R.id.price);
                EditText newState_edittext=(EditText)post_msg.findViewById(R.id.newstatus);
                //获取商品名
                final String shop_name=cardtitle_edittext.getText().toString();
                //获取商品价格
                final String shop_price=price_edittext.getText().toString();
                //获取商品的描述
                final String shop_des=cardcontent_edittext.getText().toString();
                //获取新旧程度
                final String shop_newsState=newState_edittext.getText().toString();

            // sendRequestWithHttpURLConnection(shop_name,shop_price,shop_des,shop_newsState);
               isOK(shop_name, shop_price, shop_des, shop_newsState);
               // getTaskPicTags(shop_name,shop_price,shop_des,shop_newsState);

            }
        });
    }
    private void isOK(final String name,final String price,final String des,final String newstate)
    {
        int isprice,isnewstate;
        if(name.length()>0) {
            if (des.length() > 0) {
                if (newstate.length() > 0) {
                    isnewstate = Integer.parseInt(newstate);
                    if (isnewstate >= 1 && isnewstate <= 10) {
                        if (price.length() > 0) {
                            isprice = Integer.parseInt(price);
                            if (isprice > 0) {

                                getTaskPicTags(name, isprice, des, isnewstate);

                            } else {
                                Toast.makeText(getApplicationContext(), "价格不能小于0", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "必须输入价格", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "新旧程度必须输入1-10的其中一个数字", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "必须输入新旧程度", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "商品描述不能为空", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(getApplicationContext(),"必须填写商品名称",Toast.LENGTH_SHORT).show();

        }
    }
    private void getTaskPicTags(final String name,final int price,final String des,final int newstate) {
        String url = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/shoppingpub.do?" + "name=" + name + "&price=" + price + "&des=" + des + "&newState=" + newstate;
        final AsyncHttpClient client = new AsyncHttpClient();
        if (Util.getCookies() != null) {
            Log.d("User_post_card", "Util.getCookies() 不是空的 ");
            BasicCookieStore bcs = new BasicCookieStore();
            bcs.addCookies(Util.getCookies().toArray(
                    new Cookie[Util.getCookies().size()]));
            client.setCookieStore(bcs);
        }

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] errorResponse, Throwable e) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Log.d("User_post_card", "onSuccess json = " + json);
                if (json.equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), "成功发帖", Toast.LENGTH_SHORT).show();
                    User_post_card.this.finish();
                } else if (json.equalsIgnoreCase("2")) {
                    Toast.makeText(getApplicationContext(), "尚未登录，没有权限发帖子", Toast.LENGTH_SHORT).show();
                    //弹出窗口看要不要登录或注册
                    login_or_register();

                } else if (json.equalsIgnoreCase("3")) {
                    Toast.makeText(getApplicationContext(), "发帖出错嘎嘎", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
   /* private void sendRequestWithHttpURLConnection(final String name,final int price,final String des,final int newstate)
    {
        String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/shoppingpub.do?"+"name="+name+"&price="+price+"&des="+des+"&newState="+newstate;
        HttpUtil.postHttpRequest(str, new HttpCallbackListener() {
            public void onFinish(String response) {
                Message message = new Message();
                message.obj = response;
                String tip = (String) message.obj;
                if (tip.equalsIgnoreCase("1")) {
                    message.what = SHOW_success;
                } else if (tip.equalsIgnoreCase("2")) {
                    message.what = NOT_Login;
                } else if (tip.equalsIgnoreCase("3")) {
                    message.what = SHOW_fail;
                } else {
                    ;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }*/

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch(requestCode)
        {
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);//启动裁剪程序
                }
                break;
            case  CROP_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);//把裁剪后的图片显示出来

                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

           case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT>=18)
                    {
                        //4.4以上系统使用者这个方法处理图片
                       // handleImageOnKitKat(data);
                        handleImageBeforeKitKat(data);
                    }
                    else{
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:break;
        }

    }

    //两个处理图片的方法
  /* private void handleImageOnKitKat(Intent data)
    {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri,则通过document 的id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id=docId.split(":")[1];//解析出数字的id
                String selection=MediaStore.Images.Media._ID + "="+ id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId);
                imagePath=getImagePath(contentUri,null);

            }else if("content".equalsIgnoreCase(uri.getScheme()))
            {
                //如果不是DOCUMENT类型的uri，用普通方式处理
                imagePath=getImagePath(uri,null);

            }
            displayImage(imagePath);
        }
    }*/
   private  void handleImageBeforeKitKat(Intent data)
    {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri, null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection)
    {
        String path=null;
        //通过URIH和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri, null, selection, null, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath)
    {

        if(imagePath!=null)
        {
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void login_or_register()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(User_post_card.this);
        //注册成功后询问是否现在登录
        alertDialog.setTitle("注册/登录");
        alertDialog.setMessage("是否现在登录/注册?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(User_post_card.this, User_Login.class);
                startActivity(intent1);
                User_post_card.this.finish();

            }
        });
        alertDialog.setNegativeButton("去注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(User_post_card.this,User_Register.class);
                startActivity(intent);
                User_post_card.this.finish();

            }
        });
        alertDialog.show();
    }

    }

