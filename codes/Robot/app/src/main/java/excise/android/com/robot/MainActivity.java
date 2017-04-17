package excise.android.com.robot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv;
    private EditText sendText;
    private Button sendBtn;

    private List<ListData> lists;
    private TextAdapter adapter;

    void init(){
        lv= (ListView) findViewById(R.id.listView1);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendText = (EditText) findViewById(R.id.sendText);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contend_text = sendText.getText().toString();
                sendText.setText("");
                String dropk = contend_text.replace(" ", "");
                dropk = dropk.replace("\n", "");

                ListData listData = new ListData(dropk,ListData.SEND,getTime());
                lists.add(listData);
                adapter.notifyDataSetChanged();


                MyTask mTask = new MyTask();
                mTask.execute("http://www.tuling123.com/openapi/api","key=3f3ca7cc2966b9fee772a43322552450&info="+dropk);
            }
        });

        lists = new ArrayList<ListData>();
        ListData listdata = new ListData(getRandomTips(), ListData.RECEIVE,getTime());
        lists.add(listdata);

        adapter = new TextAdapter(this);

        lv.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private String getRandomTips() {
        String[] welcomes = this.getResources().getStringArray(R.array.welcome_tips);
        int index = (int)(Math.random()*(welcomes.length-1));
        return welcomes[index];
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str ;
    }


    public class TextAdapter extends BaseAdapter {

        private Context context;
        private RelativeLayout layout ;

        public TextAdapter(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View arg1, ViewGroup arg2) {
            LayoutInflater inflater = LayoutInflater.from(context);
            if(lists.get(position).getFlag() == ListData.RECEIVE)
            {
                layout = (RelativeLayout) inflater.inflate(R.layout.leftitem2, null);
            }
            if(lists.get(position).getFlag() == ListData.SEND)
            {
                layout = (RelativeLayout) inflater.inflate(R.layout.rightitem2, null);
            }
            TextView tv = (TextView) layout.findViewById(R.id.tv);
            TextView tvTime = (TextView) layout.findViewById(R.id.tvTime);
            tv.setText(lists.get(position).getContent());
            tvTime.setText(lists.get(position).getDate());

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    //Toast.makeText(context, "文字点击", 3000).show();
                    new AlertDialog.Builder(context).setTitle("确认删除此条消息？")//设置对话框标题
                            //.setMessage("")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // 删除一个item
                                    lists.remove(position);
                                    TextAdapter.this.notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮

                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件

                        }
                    }).show();//在按键响应事件中显示此对话框
                }
            });

            return layout;
        }

    }


    private class MyTask extends AsyncTask<String, Integer, String> {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String pars = params[1];
            String res = HttpUtil.sendGet(url, pars);
            return res;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {

            //tv.setText("loading..." + progresses[0] + "%");
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            if(result == null || result.length() <= 0){
                return;
            }
            // parse String reult to Json Object , then get the result
            // TODO
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                //e.printStackTrace();
            }
            //String code = "";
            String text = "server is wrong";
            if(jsonObject != null) {
                try {
                    //code = jsonObject.getString("code");
                    text = jsonObject.getString("text");
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }
            //
            ListData listData = new ListData(text,ListData.RECEIVE,getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {

        }
    }
}
