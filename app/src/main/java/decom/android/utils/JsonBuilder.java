package decom.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

import decom.android.core.App;
import decom.android.enums.ChatRespond;
import decom.android.enums.ExternalRequest;
import decom.android.enums.GroupRespond;
import decom.android.enums.MeetRespond;
import decom.android.models.Chat;

public class JsonBuilder {

    /**
     * This request is used to meet the other node.
     */
    public static JSONObject buildMeetRequest(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_node_alias", App.user.getName());
            jsonObject.put("client_node_id", App.user.getId());
            jsonObject.put("request_type", ExternalRequest.MEET);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * This respond is used for accepting the Meet Request.
     */
    public static JSONObject buildMeetOkRespond(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("host_node_alias", App.user.getName());
            jsonObject.put("host_node_id", App.user.getId());
            jsonObject.put("respond_type", MeetRespond.MEET_OK);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject buildGroupRequest(Chat chat, String hostNodeId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_node_id", App.user.getId());
            jsonObject.put("group_alias", chat.getChatName());
            jsonObject.put("group_id", chat.getId());
            jsonObject.put("host_node_id", hostNodeId);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(App.user.getId());
            for (String id : chat.getContactIds()){
                jsonArray.put(id);
            }
            jsonObject.put("peers", jsonArray);
            jsonObject.put("request_type", ExternalRequest.GROUP);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject buildGroupOkRespond(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("respond_type", GroupRespond.GROUP_OK);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject buildChatRequest(int length, String groupId, String hostId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_node_id", App.user.getId());
            jsonObject.put("content_length", length);
            jsonObject.put("group_id", groupId);
            jsonObject.put("host_node_id", hostId);
            jsonObject.put("request_type", ExternalRequest.CHAT);
            jsonObject.put("timestamp",new Timestamp(System.currentTimeMillis()).getTime() / 1000  );

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject buildChatOkRespond(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("respond_type", ChatRespond.CHAT_OK);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
