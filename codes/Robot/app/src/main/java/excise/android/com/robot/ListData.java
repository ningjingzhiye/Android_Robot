package excise.android.com.robot;

/**
 * Created by a123 on 17/4/16.
 */

public class ListData {
    private String date;
    private String content;
    public static final int SEND = 1;
    public static final int RECEIVE = 2;
    private int flag;

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public int getFlag() {
        return flag;
    }


    public void setFlag(int flag) {
        this.flag = flag;
    }


    ListData(String content,int flag,String date)
    {
        setContent(content);
        setFlag(flag);
        setDate(date);
    }
}
