package com.btellez.solidandroid.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.btellez.solidandroid.R;
import com.btellez.solidandroid.model.Icon;
import com.btellez.solidandroid.utility.Strings;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SingleIconResultItem extends FrameLayout {

    @InjectView(R.id.link) protected ImageView link;
    @InjectView(R.id.icon) protected ImageView icon;
    @InjectView(R.id.term) protected TextView term;
    @InjectView(R.id.attribution) protected TextView attribution;
    protected Listener listener = new Listener.SimpleListener();

    /**
     * Listener is used to keep non-ui logic out of the view.
     */
    public interface Listener {
        void onLinkClicked(String url);
        void requestDownloadInto(String url, ImageView imageView);

        class SimpleListener implements Listener {
            @Override public void onLinkClicked(String url) {/* no-op */}
            @Override public void requestDownloadInto(String url, ImageView imageView) {/* no-op */}
        }
    }

    public SingleIconResultItem(Context context) {
        super(context);
        init(context);
    }

    public SingleIconResultItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleIconResultItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleIconResultItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.single_icon_result_item, this);
        ButterKnife.inject(this);
        listener = new Listener.SimpleListener();
    }

    public void setListener(Listener listener) {
        if (listener == null)
            listener = new Listener.SimpleListener();
        this.listener = listener;
    }

    public void setIconData(Icon data) {
        term.setText(data.getTerm().toUpperCase());
        attribution.setText(getAttributionString(data));
        link.setTag(data.getPermalink());
        listener.requestDownloadInto(data.getPreviewUrl(), icon);
    }

    protected String getAttributionString(Icon data) {
        if (Strings.isEmpty(data.getUploader().getName())) {
            return "The Noun Project";
        }
        return data.getUploader().getName() +" from The Noun Project";
    }

    @OnClick(R.id.link)
    protected void onLinkClicked() {
        listener.onLinkClicked((String) link.getTag());
    }
}
