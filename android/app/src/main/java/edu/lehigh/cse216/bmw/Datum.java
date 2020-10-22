package edu.lehigh.cse216.bmw;

public class Datum {

    private String mTitle;

    private String mMessage;

    private int mVote;

    private String mId; //TODO fix to be an int
    //private int mId;

    private String  uId;

    private String uName;

    private String cId;

    private String cMessage;

    private String date_created;


    public Datum(String msg_id, String usr_id, String cmt_id, String cmt_msg){
        mId = msg_id;
        uId = usr_id;
        cId = cmt_id;
        cMessage = cmt_msg;
    }

    public Datum(String title, String message, int vote, String m_id, String u_id, String date){
        mTitle = title;
        mMessage = message;
        mVote = vote;
        mId = m_id;
        uId = u_id;
        date_created = date;
    }


    public String getmTitle()
    {
        return mTitle;
    }

    public String getmId()
    {
        return mId;
    }

    public int getmVote(){
        return mVote;
    }

    public String getmMessage(){
        return mMessage;
    }

    public String getcMessage(){
        return cMessage;
    }

    public String getDate(){ return date_created; }

    public String getuId() { return uId; }

    public boolean contains(String filterdString){
        if(mTitle.contains(filterdString)){
            return true;
        }
        if(mMessage.contains(filterdString)) {
            return true;
        }
        return false;
    }
}
