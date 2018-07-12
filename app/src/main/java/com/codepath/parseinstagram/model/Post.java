package com.codepath.parseinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_HANDLE = "handle";

    // getPhotoFileUri() is defined in
// https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media#using-capture-intent

    //File photoFile = getPhotoFileUri(photoFileName);
    //ParseFile parseFile = new ParseFile(photoFile);


    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public void setHandle(String handle) { getUser().put(KEY_HANDLE, handle);}

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public String getHandle() throws ParseException {return getUser().fetchIfNeeded().getString(KEY_HANDLE);}

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getMedia() { return getParseFile("media"); }

    public void setMedia(ParseFile parseFile) { put("media", parseFile); }

    public static class Query extends ParseQuery<Post>{
        public Query(){
            super(Post.class);
        }

        public Query getTop(){
            setLimit(20);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }

    }
}
