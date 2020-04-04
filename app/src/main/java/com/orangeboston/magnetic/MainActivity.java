package com.orangeboston.magnetic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.orangeboston.magnetic.FlashUtils;

import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private MagneticView mView;
    private Button mBtn;
    private ImageView mImg;
    private TextView mTvValues1, mTvValues2, mTvValues3;
    float[] mValues;
    FlashUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mView = findViewById(R.id.magnetic_view);
        mTvValues1 = findViewById(R.id.tv_values_1);
        mTvValues2 = findViewById(R.id.tv_values_2);
        mTvValues3 = findViewById(R.id.tv_values_3);
        mBtn = findViewById(R.id.nv_rotate);
        mImg = findViewById(R.id.image_lamp_bulb);
        utils = new FlashUtils(this);
    }

    /**
     * 第一个角度：表示手机顶部朝向正北方的夹角。 当手机绕着Z轴旋转时，该角度值发送改变。
     * 例如当该角度为0时，表明手机顶部朝向正北；该角度为90时，代表手机顶部朝向正东；该角度为180时，代表手机顶部朝向正南；该角度为270时，代表手机顶部朝向正西。
     * 第二个角度：表示手机顶部或尾部翘起的角度。当手机绕着X轴倾斜时，该角度值发送改变。该角度的取值范围是-180~180。
     * 假设将手机屏幕朝上水平放在桌子上，如果桌子是完全水平的，该角度值应该是0。加入从手机顶部开始抬起，直到将手机沿X轴旋转180度（屏幕向下水平放在桌面上），在这个旋转过程中，该角度值会从0变化为-180。也就是说，从手机顶部抬起时，该角度值会逐渐减小，直到等于-180；如果从手机底部开始抬起，知道将手机沿X轴旋转180度（屏幕向下水平放在桌面上），该角度值会从0变化为180。也就是说，从手机顶部抬起时，该角度值会逐渐增大，直到等于180。
     * 第三个角度：表示手机左侧或右侧翘起的角度。当手机绕着Y轴倾斜时，该角度值发送改变。该角度的取值范围是-90~90。
     * 假设将手机屏幕朝上水平放在桌子上，如果桌子是完全水平的，该角度值应该是0。假设将手机左侧逐渐抬起，知道将手机沿Y轴旋转90度(手机与桌面垂直),在这个旋转过程中，该角度值会从0变化为-90。也就是说，从手机左侧抬起时，该角度值会逐渐减小，直到等于-90；如果将手机右侧逐渐抬起，知道将手机沿Y轴旋转90度(手机与桌面垂直),在这个旋转过程中，该角度值会从0变化为90。也就是说，从手机右侧抬起时，该角度值会逐渐增大，直到等于90
     */

    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mValues = event.values;
            if (mView != null) {
                mView.setValues(mValues);
                mView.invalidate();
                mTvValues1.setText("" + (int) mValues[0]);
                mTvValues2.setText("" + (int) mValues[1]);
                mTvValues3.setText("" + (int) mValues[2]);
            }
            if (mBtn != null) {
                int direction = (int) mValues[0];
                String msg = "";
                if (direction > 45 && direction <= 135) {
                    msg = getResources().getString(R.string.east);
                    utils.close();
                    mImg.setImageResource(R.drawable.ic_lamp_bulb);
                } else if (direction > 135 && direction < 225) {
                    msg = getResources().getString(R.string.south);
                    utils.open();
                    mImg.setImageResource(R.drawable.ic_lamp_bulb_light);
                } else if (direction > 225 && direction < 315) {
                    msg = getResources().getString(R.string.west);
                    utils.close();
                    mImg.setImageResource(R.drawable.ic_lamp_bulb);
                } else {
                    msg = getResources().getString(R.string.north);
                    utils.close();
                    mImg.setImageResource(R.drawable.ic_lamp_bulb);
                }
                mBtn.setText(msg + direction + "°");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mListener);
    }


}