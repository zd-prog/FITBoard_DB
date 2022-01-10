package by.ziziko.fitboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.ziziko.fitboard.Entities.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public interface OnPostClickListener
    {
        void onPostClick(Post post, int position);
    }
    public interface OnDeletePostListener
    {
        void deletePost(Post post);
    }
    private final LayoutInflater inflater;
    private final List<Post> posts;
    private final OnPostClickListener onPostClickListener;
    private final OnDeletePostListener onDeletePostListener;
    public PostAdapter(Context context, List<Post> posts, OnPostClickListener onPostClickListener, OnDeletePostListener onDeletePostListener)
    {
        this.posts = posts;
        this.inflater = LayoutInflater.from(context);
        this.onPostClickListener = onPostClickListener;
        this.onDeletePostListener = onDeletePostListener;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Post post = posts.get(position);
        holder.postText.setText(post.getText());
        holder.postTitle.setText(post.getTitle());
        holder.postCategory.setText(post.getCategory());
        holder.userLogin.setText(post.getAuthor());
        holder.postDate.setText(post.getDate());

        if (post.getImage()!=null)
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(post.getImage(), Base64.DEFAULT), 0,
                Base64.decode(post.getImage(), Base64.DEFAULT).length));
        else
            holder.imageView.setImageBitmap(null);

        holder.itemView.setOnClickListener(view -> onPostClickListener.onPostClick(post, position));

        holder.deleteButton.setOnClickListener(view -> onDeletePostListener.deletePost(post));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView userLogin, postDate, postCategory, postTitle, postText;
        final ImageView imageView, deleteButton;
        ViewHolder(View view)
        {
            super(view);

            userLogin = view.findViewById(R.id.user_login);
            postDate = view.findViewById(R.id.post_date);
            postCategory = view.findViewById(R.id.post_category);
            postTitle = view.findViewById(R.id.post_title);
            postText = view.findViewById(R.id.post_text);
            imageView = view.findViewById(R.id.post_Image);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }
}
