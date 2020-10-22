package edu.lehigh.cse216.bmw.backend;

import java.io.File;

/**
 * SimpleRequest provides a format for clients to present title and message
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The title being provided by the client.
     */
    public String mTitle;

    /**
     * The message being provided by the client.
     */
    public String mMessage;

    /** 
     * The upvote/downvote being provided by the client. Default is 0.
     */
    public int mVote;

    /** 
    * comment content
    */
    public String cMessage;

    public String mLink;
    public String mFName;

    public File mFile;

    public byte[] mAttach;

    /**
     * Encrypted String
     */
    public String encryptedString;   
}