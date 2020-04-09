package clic.zoomapp.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import clic.zoomapp.R;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ScaleGestureDetector.OnScaleGestureListener {
    private Context mContext;
    private ArrayList<Model> ArrayList;
    private float min_height;
    private float max_height;

    // ScaleGestureDetector

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;


            // setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * scale);
            Log.e(TAG, String.valueOf(scale));


        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
    }

    private float mScaleFactor = 1.f;
    private float defaultSize;

    private float zoomLimit = 3.0f;



    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private static final String TAG = "ZoomLayout";
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;


    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
        ImageView thumbnail;
        TextView title, newsTitle, newsInfo;
        public ScaleGestureDetector gestureDetector;

        public ViewHolder(View view) {
            super(view);
            mview = view;

            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.title);
            newsTitle = view.findViewById(R.id.newsTitle);
            newsInfo = view.findViewById(R.id.newsInfo);


        }
    }


    public CardAdapter(Context mContext, ArrayList<Model> ArrayList) {

        this.mContext = mContext;
        this.ArrayList = ArrayList;
    }

    View itemView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        // ZoomLayout myZoomView = new ZoomLayout(mContext);
        //myZoomView.addView(itemView);
        Init(itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Model Main = ArrayList.get(position);

        ((ViewHolder) holder).newsInfo.setText(Main.getDescription());
        ((ViewHolder) holder).title.setText(Main.getTitle());
        ((ViewHolder) holder).newsTitle.setText(Main.getTitle());
        Glide.with(mContext)
                .load(Main.getImage())
                .into(((ViewHolder) holder).thumbnail);


    }


    @Override
    public int getItemCount() {
        return ArrayList.size();
    }

    float defaultImageWidth = 0;
    float defaultImageHeight = 0;

    void Init(final View view) {

        final ViewHolder dView = new ViewHolder(view);
        final ImageView thumbnail = dView.thumbnail;
        ViewTreeObserver vto = thumbnail.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // Remove after the first run so it doesn't fire forever
                thumbnail.getViewTreeObserver().removeOnPreDrawListener(this);
                defaultImageHeight = thumbnail.getMeasuredHeight();
                defaultImageWidth = thumbnail.getMeasuredWidth();
                Log.e("Height: ", defaultImageHeight + " Width: " + defaultImageWidth);
                InitData(view, defaultImageWidth, defaultImageHeight);
                return true;
            }
        });

    }

    void InitData(final View view, final float defaultImageWidth, final float defaultImageHeight) {
        final ViewHolder dView = new ViewHolder(view);
        final TextView newsTitle = dView.newsTitle;
        final TextView newsInfo = dView.newsInfo;
        final TextView title = dView.title;
        final ImageView thumbnail = dView.thumbnail;


        defaultSize = newsTitle.getTextSize();
        final float defaultSizenewsInfo = newsInfo.getTextSize();
        final float defaultSizetitle = newsTitle.getTextSize();


        Log.e("trtr", defaultImageWidth + "");

        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(mContext, this);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {


                Log.e("action", motionEvent + "");

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "DOWN");
                        if (scale > MIN_ZOOM) {
                            mode = Mode.DRAG;
                            startX = motionEvent.getX() - prevDx;
                            startY = motionEvent.getY() - prevDy;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.DRAG;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "UP");
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        break;
                }


                scaleDetector.onTouchEvent(motionEvent);
                Log.e("prevDy", startX + "-----" + startY);
                if (mode == Mode.ZOOM) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (view.getWidth() - (view.getWidth() / scale)) / 2 * scale;
                    float maxDy = (view.getHeight() - (view.getHeight() / scale)) / 2 * scale;
                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
                    dy = Math.min(Math.max(dy, -maxDy), maxDy);
                    Log.i(TAG, "Width: " + view.getWidth() + ", scale " + scale + ", dx " + dx
                            + ", max " + maxDx);
                    //applyScaleAndTranslation(view);

                    newsTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * scale);
                    newsInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSizenewsInfo * scale);
                    title.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSizetitle * scale);


                    android.view.ViewGroup.LayoutParams parms = thumbnail.getLayoutParams();
                    parms.width = (int) (defaultImageWidth * scale);
                    parms.height = (int) (defaultImageHeight * scale);
                    thumbnail.setLayoutParams(parms);


                }

                return true;
            }
        });
    }


}
