package pocholo.coffeejournal;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<ImagesModel.Image> images;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    RecyclerAdapter(Context context, List<ImagesModel.Image> images) {
        this.images = images;
        mRequestQueue = Volley.newRequestQueue(context);

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();

        View view = LayoutInflater.from(parentContext).inflate(R.layout.recyclerviewlayout, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ImagesModel.Image image = images.get(position);

        holder.titleView.setText(image.title);
        holder.networkImageView.setImageUrl(image.displaySizes.get(0).uri, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        NetworkImageView networkImageView;

        RecyclerViewHolder(View view) {
            super(view);

            titleView = (TextView) view.findViewById(R.id.uxTitle);
            networkImageView = (NetworkImageView) view.findViewById(R.id.uxNetworkImageView);
        }
    }

    public ImagesModel.Image getItem(int position) {
        return images.get(position);
    }
}