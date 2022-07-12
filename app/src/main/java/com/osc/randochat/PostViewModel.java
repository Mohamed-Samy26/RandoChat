package com.osc.randochat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PostViewModel extends androidx.lifecycle.ViewModel {

    LiveData<String> postlivedata;
    MutableLiveData<String> postmutablelivedata = new MutableLiveData<>();
    MutableLiveData m=new MutableLiveData();

  /*  public void getPostTitle()
    {
        String post_title = getpostfromdatabase().getTitle();
        postmutablelivedata.setValue(post_title);
    }*/
 /*   private post getpostfromdatabase() //inint for post class
    {

        return new post("mariam", "bretty",R.drawable.thor);
    }*/


}
