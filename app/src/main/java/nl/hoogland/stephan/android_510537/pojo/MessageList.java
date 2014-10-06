package nl.hoogland.stephan.android_510537.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class MessageList {

    /*
    GSON has support for serialized names. @SerializedName is the name as it comes from the backend.
    The member name is the name as you wish to have it name in your code.
    In the example below the backend gives us "Messages" but in the code we want to use items
     */

    @SerializedName("Messages")
    public ArrayList<MessageListItem> items;

    public MessageList() {
        items = new ArrayList<MessageListItem>();
    }

}