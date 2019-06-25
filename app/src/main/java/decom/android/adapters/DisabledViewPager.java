package decom.android.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisabledViewPager extends ViewPager {

    private boolean enableSwipe = false;

    public DisabledViewPager(@NonNull Context context) {
        super(context);
    }

    public DisabledViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enableSwipe && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enableSwipe && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enableSwipe = enabled;
    }

    public boolean isPagingEnabled() {
        return enableSwipe;
    }

}
