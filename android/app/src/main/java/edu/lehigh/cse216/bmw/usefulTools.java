package edu.lehigh.cse216.bmw;

/*
I've included some useful tools here.
at the time of Phase1(ji) there is only jsonCreate method. I thought you might need this later when you make edit.
feel free to add anything that might be useful in other phases
 */
public class usefulTools {
    public static String jsonCreate(String title, String message ){
        String result = "{" + "\"" + "mTitle" + "\"";
        String result2 = ":"+"\""+title+"\"";
        String result3 = ","+"\""+"mMessage"+"\""+":"+"\""+message+"\"";
        String result4 = "}";
        return (result+result2+result3+result4);
    } //creates a json with mTitle and mMessage

    public static String jsonCreateImage(String mFname, byte[] bytearray ){
        String result = "{" + "\"" + "mFname" + "\"";
        String result2 = ":"+"\""+mFname+"\"";
        String result3 = ","+"\""+"mAttach"+"\""+":"+"\""+bytearray+"\"";
        String result4 = "}";
        return (result+result2+result3+result4);
    }

    public static String tokenSend(String token){
        return "{" + "\"" + "token" + "\":\"" + token + "\"" + "}";
    }

    public static String commentSend(String message){
        String result = "{" + "\"" + "cMessage" + "\"";
        String result2 = ":"+"\""+message+"\""+"}";
        return result + result2;
    }

}
