package by.ziziko.fitboard.ui.home;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.ziziko.fitboard.Activities.MainActivity;
import by.ziziko.fitboard.Activities.NewPostActivity;
import by.ziziko.fitboard.CustomDialogFragment;
import by.ziziko.fitboard.Entities.Post;
import by.ziziko.fitboard.Entities.User;
import by.ziziko.fitboard.PostAdapter;
import by.ziziko.fitboard.R;
import by.ziziko.fitboard.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final String CHANNEL_ID = "1";
    private static final int NOTIFY_ID = 100;
    private FragmentHomeBinding binding;
    private String login;
    private List<User> users;
    private List<Post> posts;

    RecyclerView recyclerView;

    View view;

    private NotificationManager notificationManager;
    private boolean isAdmin;
    private boolean isInBlacklist;
    private DatabaseReference mDbUser;
    private DatabaseReference mDbPost;
    private PostAdapter adapter;
    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        String userKey = "User";
        mDbUser = FirebaseDatabase.getInstance().getReference(userKey);
        String postKey = "Post";
        mDbPost = FirebaseDatabase.getInstance().getReference(postKey);
        auth = FirebaseAuth.getInstance();

        users = new ArrayList<>();
        mDbUser.get().addOnCompleteListener(task -> {
            for (DataSnapshot ds: task.getResult().getChildren())
            {
                users.add(ds.getValue(by.ziziko.fitboard.Entities.User.class));
            }
        });
        posts = new ArrayList<>();
        mDbPost.get().addOnCompleteListener(task -> {
            for (DataSnapshot ds: task.getResult().getChildren())
            {
                posts.add(ds.getValue(by.ziziko.fitboard.Entities.Post.class));
                posts.size();
            }
        });


        getDataFromDb();


        if (requireActivity().getIntent().getExtras() != null) {
            login = requireActivity().getIntent().getExtras().getString("login");
            isAdmin = requireActivity().getIntent().getExtras().getBoolean("isAdmin");
            isInBlacklist = requireActivity().getIntent().getExtras().getBoolean("isInBlackList");
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isInBlacklist) {
            binding.fab.setVisibility(View.INVISIBLE);
            sendNotification("Пользователь " + login + " был добавлен в чёрный список", "Теперь он не может добавлять новости");
        }

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity().getApplicationContext(), NewPostActivity.class);
            intent.putExtra("login", login);
            startActivity(intent);
        });

        notificationManager = (NotificationManager) requireActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        view = root;
        recyclerView = root.findViewById(R.id.posts);
        registerForContextMenu(recyclerView);
        PostAdapter.OnPostClickListener postClickListener = (post, position) -> {

            CustomDialogFragment dialog = new CustomDialogFragment((dialogInterface, i) -> {
                Post Post = posts.get(position);

                for (User user : users) {
                    if (user.getLogin().equals(Post.getAuthor())) {

                        mDbUser.get().addOnCompleteListener(task -> {
                            for (DataSnapshot ds: task.getResult().getChildren())
                            {
                                User User = ds.getValue(by.ziziko.fitboard.Entities.User.class);
                                assert User != null;
                                if (User.getId().equals(user.getId()))
                                {
                                    ds.getRef().child("inBlacklist").setValue(true);
                                }
                            }
                        });
                        Notification(Post.getAuthor());
                    }
                }
            });
            if (isAdmin)
                dialog.show(requireActivity().getSupportFragmentManager(), "custom");
        };


        PostAdapter.OnDeletePostListener deletePostListener = post -> {

                if (isAdmin)
                {
                    Query Query = mDbPost.orderByChild("title").equalTo(post.getTitle());


                    Query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
        };

        adapter = new PostAdapter(requireActivity(), posts, postClickListener, deletePostListener);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void getDataFromDb()
    {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (posts.size()>0)
                    posts.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Post post = ds.getValue(Post.class);
                    assert post!=null;
                    posts.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDbPost.addValueEventListener(listener);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        auth.signOut();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Notification(String login){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(requireActivity(),CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.message)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Пользователь " + login + " был добавлен в чёрный список")
                        .setContentText("Теперь он не может добавлять новости")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                .setChannelId(CHANNEL_ID);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Channel title",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(NOTIFY_ID,notificationBuilder.build());

    }

    private void sendNotification(String messageTitle,String messageBody) {
        NotificationManager notificationManager =
                (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel("my_notification","n_channel",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("description");
            notificationChannel.setName("Channel Name");
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(requireActivity())
                .setSmallIcon(R.drawable.message)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.message))
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setChannelId("my_notification")
                .setColor(Color.parseColor("#3F5996"));

        //.setProgress(100,50,false);
        assert notificationManager != null;
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());
    }
}