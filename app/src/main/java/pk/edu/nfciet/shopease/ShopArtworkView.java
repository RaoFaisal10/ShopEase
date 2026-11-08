package pk.edu.nfciet.shopease;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/** Lightweight, scalable vector artwork: no network images or bitmap assets required. */
public class ShopArtworkView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    public ShopArtworkView(Context c, AttributeSet a) { super(c,a); setLayerType(View.LAYER_TYPE_SOFTWARE,null); }
    private void fill(Canvas c, int color, float x,float y,float w,float h,float r) {
        p.reset();p.setAntiAlias(true);p.setColor(color);p.setStyle(Paint.Style.FILL);
        c.drawRoundRect(new RectF(x,y,x+w,y+h),r,r,p);
    }
    private void oval(Canvas c,int color,float x,float y,float w,float h) {
        p.reset();p.setAntiAlias(true);p.setColor(color);c.drawOval(x,y,x+w,y+h,p);
    }
    private void line(Canvas c,int color,float ax,float ay,float bx,float by,float weight) {
        p.reset();p.setAntiAlias(true);p.setColor(color);p.setStrokeWidth(weight);
        p.setStrokeCap(Paint.Cap.ROUND);c.drawLine(ax,ay,bx,by,p);
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();canvas.scale(getWidth()/300f,getHeight()/230f);
        boolean gift = "gift".equals(String.valueOf(getTag()));
        if(gift) drawGift(canvas); else drawBags(canvas);
        canvas.restore();
    }
    private void drawBags(Canvas c) {
        oval(c,0x40D49F64,65,191,190,18);
        oval(c,0x55FFFFFF,208,27,55,55);
        oval(c,0x55FFFFFF,27,63,32,32);
        c.save();c.rotate(12,163,129);
        fill(c,0xFFFA8C61,96,53,112,139,19);
        fill(c,0xFFFFAC81,99,55,107,40,16);
        line(c,0xFFB56446,130,62,131,35,7);
        line(c,0xFFB56446,175,62,174,35,7);
        c.restore();
        c.save();c.rotate(-13,193,139);
        fill(c,0xFF1C2C4A,145,97,97,112,14);
        fill(c,0xFF263E65,146,98,96,26,12);
        p.setStyle(Paint.Style.STROKE);p.setColor(0xFFFFD692);p.setStrokeWidth(8);
        c.drawArc(new RectF(174,70,210,116),181,178,false,p);p.setStyle(Paint.Style.FILL);
        c.restore();
        oval(c,0xFFFFF7DA,80,140,43,43);
        line(c,0xFFB8753B,91,159,113,159,4);
        line(c,0xFFB8753B,102,148,102,170,4);
        line(c,0xFF1C2C4A,253,52,263,52,4);
        line(c,0xFF1C2C4A,258,47,258,57,4);
        oval(c,0xFFFA8C61,54,115,7,7);
    }
    private void drawGift(Canvas c) {
        oval(c,0x22A77D53,38,185,226,21);
        oval(c,0xFFFFF3DC,43,38,212,155);
        c.save();c.rotate(-10,145,119);
        fill(c,0xFFEA9B5C,78,79,138,111,16);
        fill(c,0xFFF8BF7B,78,72,138,37,12);
        fill(c,0xFF1A2C4B,136,73,21,117,2);
        fill(c,0xFFF4AB3E,133,78,27,36,0);
        line(c,0xFFFFE7B5,145,73,145,189,3);
        c.restore();
        oval(c,0xFF1B2A46,210,34,38,38);
        line(c,0xFFFFDCA0,219,54,237,54,3);
        line(c,0xFFFFDCA0,228,45,228,63,3);
        oval(c,0xFFFFAC78,49,58,13,13);
        oval(c,0xFF2C466D,240,166,9,9);
    }
}
