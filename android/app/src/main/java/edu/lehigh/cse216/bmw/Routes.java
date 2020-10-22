package edu.lehigh.cse216.bmw;
/*
    contains all the routes for the convenience
 */

public class Routes {
    private final static String base = "https://bmw-dba.herokuapp.com/buzz";
    private final static  String showUrl = base + "/%d"; //insert message id
    private final static  String messageUpvoteUrl = base + "/%d/upvote"; //insert message id
    private final static  String messageDownvoteUrl = base + "/%d/downvote"; //insert message id
    private final static String authorizeUserUrl = "https://bmw-dba.herokuapp.com/auth"; //authorize user, uses token
    private final static String commentUrl = showUrl + "/comment";
    //private final static String uploadURL = base + "/%d/upvote";
    //private final static String downloadURL = base + "/%d/download";

    public static String getBuzz()
    {
        return base;
    }
    public static String getShow()
    {
        return showUrl;
    }
    public static String getMessageUpvoteUrl()
    {
        return messageUpvoteUrl;
    }
    public static String getMessageDownvoteUrl()
    {
        return messageDownvoteUrl;
    }
    public static String getAuthorizeUserUrl() { return authorizeUserUrl; }
    public static String getCommentUrl() { return commentUrl; }
    //public static String getUpload(){ return uploadURL; }
    //public static String downloadURL(){ return downloadURL; }

}
