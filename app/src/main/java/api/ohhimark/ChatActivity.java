package api.ohhimark;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String usernameChat;
    private Button sendButton;
    private ListView messageList;
    private ArrayList<String> messagesList;
    private ArrayList<String> usernameSender;
    private ArrayAdapter arrayAdapter;
    private SimpleAdapter simpleAdapter;

    private List<Map<String, String>> msgList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sub_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.userItem:
                Intent i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);

                break;
            case R.id.receiveItem:

                break;
            case R.id.logoutItem:

                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        usernameChat = intent.getStringExtra("username");
        msgList = new ArrayList<>();

        setTitle(usernameChat);
        sendButton = (Button) findViewById(R.id.sendButton);
        messageList = (ListView) findViewById(R.id.chatListView);
        messagesList = new ArrayList<>();
        usernameSender = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, msgList, android.R.layout.simple_list_item_2, new String[] {"content", "username"}, new int[] {android.R.id.text1, android.R.id.text2});
        messageList.setAdapter(simpleAdapter);
        getMessages();

    }

    public void sendMessage (View view )
    {

        final EditText msg = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Send a message")
                .setView(msg)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject object = new ParseObject("Messages");
                        object.put("usernameSender", ParseUser.getCurrentUser().getUsername());
                        object.put("message", msg.getText().toString());
                        object.put("usernameReceiver", usernameChat);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null)
                                    Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Errrror" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                getMessages();
                                Log.i("info", "sent");

                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }

    public void refresh(View view)
    {
        getMessages ();

    }

    private void getMessages ()
    {
        msgList.clear();

        ParseQuery<ParseObject> query = new ParseQuery("Messages");
        ArrayList<String> al = new ArrayList<String>();
        al.add(usernameChat);
        al.add(ParseUser.getCurrentUser().getUsername());
        query.whereContainedIn("usernameSender",al);
        query.whereContainedIn("usernameReceiver", al);

        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null)
                    {
                        for (ParseObject o : objects)
                        {
                            Map<String, String> msgMap = new HashMap<>();
                            msgMap.put("content", o.getString("message"));
                            msgMap.put("username", "Sent by: " +o.getString("usernameSender"));
                            msgList.add(msgMap);
                        }


                    } else {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                simpleAdapter.notifyDataSetChanged();

            }
        });

    }

}

