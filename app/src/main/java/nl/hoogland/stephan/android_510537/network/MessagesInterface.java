package nl.hoogland.stephan.android_510537.network;

import nl.hoogland.stephan.android_510537.pojo.MessageList;

import nl.hoogland.stephan.android_510537.pojo.MessagePostDataReturn;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Path;

/**
 * Created by stephan on 01/10/14.
 */
public interface MessagesInterface {

    @GET("/Messages")
    void getMessages(Callback<MessageList> cb);

    @GET("/Messages/{id}")
    void getMessagesById(@Path("id") int lastMessageId, Callback<MessageList> cb);

    @FormUrlEncoded
    @POST("/Messages")
    void postMessage(@Field("title") String title, @Field("text") String text, Callback<MessagePostDataReturn> cb);
}
