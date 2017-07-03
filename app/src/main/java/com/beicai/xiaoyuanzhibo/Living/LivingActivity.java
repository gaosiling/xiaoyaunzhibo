package com.beicai.xiaoyuanzhibo.Living;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beicai.xiaoyuanzhibo.R;
import com.beicai.xiaoyuanzhibo.activity.BaseActivity;
import com.beicai.xiaoyuanzhibo.api.HomeApi;
import com.beicai.xiaoyuanzhibo.api.UrlConfig;
import com.beicai.xiaoyuanzhibo.bean.CreatLiveBean;
import com.beicai.xiaoyuanzhibo.bean.UpdateLiveBean;
import com.beicai.xiaoyuanzhibo.network.RetrofitManager;
import com.beicai.xiaoyuanzhibo.utils.PreferenceUtils;
import com.beicai.xiaoyuanzhibo.utils.ToastUtils;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.OnAudioRawDataListener;
import com.ksyun.media.streamer.kit.OnPreviewFrameListener;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivingActivity extends BaseActivity {

    private static final String TAG = "CameraActivity";

    private GLSurfaceView mCameraPreviewView;
    //private TextureView mCameraPreviewView;
    private CameraHintView mCameraHintView;
    private Chronometer mChronometer;
    private View mDeleteView;
    private View mSwitchCameraView;
    private View mFlashView;
    private TextView mShootingText;
    private int mLastRotation;
    private OrientationEventListener mOrientationEventListener;
    private ButtonObserver mObserverButton;
    private KSYStreamer mStreamer;
    private Handler mMainHandler;
    private Timer mTimer;
    private boolean mAutoStart;
    private boolean mIsLandscape;
    private boolean mPrintDebugInfo = false;
    private boolean mRecording = false;
    private boolean mIsFileRecording = false;
    private boolean isFlashOpened = false;
    private String mBgmPath = "/sdcard/test.mp3";
    private String mLogoPath = "file:///sdcard/test.png";
    private String mRecordUrl = "/sdcard/test.mp4";
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    private static final String START_STRING = "开始直播";
    private static final String STOP_STRING = "停止直播";
    private String[] pic = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489589097749&di=44c8dce130d718071bcefc508c2ffb6f&imgtype=0&src=http%3A%2F%2Fq.115.com%2Fimgload%3Fr%3DADFCE0A0CBCDD585454FECD908C3215E7BDBEC9B%26u%3DwNnOPm%26s%3DsibMXXEQjUHME4hUTToiMQ%26e%3D5%26st%3D0", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489589097749&di=91358f4b4f0b5d31b9b0f4563e7d9f55&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3De0256a9a740e0cf3a0f74ef33a47f23d%2Fc63cfaedab64034f0887058ea9c379310b551d33.jpg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489589097748&di=727e483a3ad03951b22b6b4e51ea2039&imgtype=0&src=http%3A%2F%2Fattimg.dospy.com%2Fimg%2Fday_110719%2F20110719_7f2ed5138ca5ea16b17eKJMcApTUtDJN.jpg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489589097746&di=4b17a1b6b9da3a6818177f33fabebf31&imgtype=0&src=http%3A%2F%2Fimg1.gamersky.com%2Fimage2015%2F04%2F20150408zwc_7%2Fimage021_S.jpg", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=763856467,4231999936&fm=23&gp=0.jpg"};
    private EditText liveName;
    private ImageView close;
    private String live_id;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LivingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.living_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        mObserverButton = new ButtonObserver();
        mShootingText = (TextView) findViewById(R.id.click_to_shoot);
        liveName = (EditText) findViewById(R.id.living_name);
        mShootingText.setOnClickListener(mObserverButton);
        mSwitchCameraView = findViewById(R.id.switch_cam);
        close = (ImageView) findViewById(R.id.close);
        mSwitchCameraView.setOnClickListener(mObserverButton);
        close.setOnClickListener(mObserverButton);
        mFlashView = findViewById(R.id.flash);
        mFlashView.setOnClickListener(mObserverButton);
        mMainHandler = new Handler();
        mStreamer = new KSYStreamer(this);
        int frameRate = 15;
        if (frameRate > 0) {
            mStreamer.setPreviewFps(frameRate);
            mStreamer.setTargetFps(frameRate);
        }

        int videoBitrate = 800;
        if (videoBitrate > 0) {
            mStreamer.setVideoKBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        }

        int audioBitrate = 48;
        if (audioBitrate > 0) {
            mStreamer.setAudioKBitrate(audioBitrate);
        }

        int videoResolution = StreamerConstants.VIDEO_RESOLUTION_360P;
        mStreamer.setPreviewResolution(videoResolution);
        mStreamer.setTargetResolution(videoResolution);

        int encode_type = AVConst.CODEC_ID_AVC;
        mStreamer.setVideoCodecId(encode_type);

        int encode_method = StreamerConstants.ENCODE_METHOD_SOFTWARE;
        mStreamer.setEncodeMethod(encode_method);

        int encodeScene = VideoEncodeFormat.ENCODE_SCENE_SHOWSELF;
        mStreamer.setVideoEncodeScene(encodeScene);

        int encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER;
        mStreamer.setVideoEncodeProfile(encodeProfile);

        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            int rotation = getDisplayRotation();
            mIsLandscape = (rotation % 180) != 0;
            mStreamer.setRotateDegrees(rotation);
            mLastRotation = rotation;
            mOrientationEventListener = new OrientationEventListener(this,
                    SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {
                    int rotation = getDisplayRotation();
                    if (rotation != mLastRotation) {
                        Log.d(TAG, "Rotation changed " + mLastRotation + "->" + rotation);
                        mIsLandscape = (rotation % 180) != 0;
                        mStreamer.setRotateDegrees(rotation);
                        hideWaterMark();
                        showWaterMark();
                        mLastRotation = rotation;
                    }
                }
            };
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mIsLandscape = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mStreamer.setRotateDegrees(90);
        } else {
            mIsLandscape = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mStreamer.setRotateDegrees(0);
        }

        mAutoStart = false;
        mPrintDebugInfo = false;


        mStreamer.setDisplayPreview(mCameraPreviewView);
        //if (mIsLandscape) {
        //    mStreamer.setOffscreenPreview(1280, 720);
        //} else {
        //    mStreamer.setOffscreenPreview(720, 1280);
        //}
        //断网等异常case触发自动重练
        mStreamer.setEnableAutoRestart(true, 3000);
        mStreamer.setFrontCameraMirror(true);//前置镜像
        mStreamer.setMuteAudio(false);//静音
        mStreamer.setEnableAudioPreview(false);
        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);
        mStreamer.setOnLogEventListener(mOnLogEventListener);
        //mStreamer.setOnAudioRawDataListener(mOnAudioRawDataListener);
        //mStreamer.setOnPreviewFrameListener(mOnPreviewFrameListener);

        // set beauty filter
        initBeautyUI();

        mStreamer.getImgTexFilterMgt().

                setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
                                       @Override
                                       public void onError(ImgTexFilterBase filter, int errno) {
                                           Toast.makeText(LivingActivity.this, "当前机型不支持该滤镜",
                                                   Toast.LENGTH_SHORT).show();
                                           mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                                                   ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
                                       }
                                   }

                );

        // add RGBA buffer filter to ImgTexFilterMgt, this would cause performance drop,
        // only valid after Android 4.4
        //RGBABufDemoFilter demoFilter = new RGBABufDemoFilter(mStreamer.getGLRender());
        //mStreamer.getImgTexFilterMgt().setExtraFilter(demoFilter);

        // touch focus and zoom support
        CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        // set CameraHintView to show focus rect and zoom ratio
        cameraTouchHelper.setCameraHintView(mCameraHintView);
        mStreamer.startCameraPreview();

        onBeautyChecked(true);

        onWaterMarkChecked(true);

        onFrontMirrorChecked(true);
    }

    private void initBeautyUI() {
        mStreamer.getImgTexFilterMgt().setFilter(
                mStreamer.getGLRender(), 1 + 15);
        List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
        final ImgFilterBase filter = filters.get(0);
        filter.setGrindRatio(1f);
        filter.setWhitenRatio(1f);
        filter.setRuddyRatio(1f);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOrientationEventListener != null &&
                mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
        startCameraPreviewWithPermCheck();
        mStreamer.onResume();
        mStreamer.setUseDummyAudioCapture(false);
        showWaterMark();
        mCameraHintView.hideAll();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
        mStreamer.onPause();
        mStreamer.setUseDummyAudioCapture(true);
        mStreamer.stopCameraPreview();
        hideWaterMark();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mStreamer.setOnLogEventListener(null);
        mStreamer.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackoffClick();
                break;
            default:
                break;
        }
        return true;
    }

    private int getDisplayRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    //start streaming
    private void startStream() {
        String sLiveName = liveName.getText().toString();
        if (TextUtils.isEmpty(sLiveName)) {
            ToastUtils.showLong("直播名字不能为空");
            return;
        }

        createLive(sLiveName);


    }

    //start recording to a local file
    private void startRecord() {
        mStreamer.startRecord(mRecordUrl);
        mIsFileRecording = true;
    }

    private void stopRecord() {
        mStreamer.stopRecord();
        mIsFileRecording = false;
        stopChronometer();
    }

    private void stopChronometer() {
        if (mRecording || mIsFileRecording) {
            return;
        }
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
    }

    private void stopStream() {
        mStreamer.stopStream();
        mShootingText.setText(START_STRING);
        mShootingText.postInvalidate();
        mRecording = false;
        stopChronometer();
    }

    private void beginInfoUploadTimer() {
        if (mPrintDebugInfo && mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }, 100, 1000);
        }
    }


    //show watermark in specific location
    private void showWaterMark() {
        if (!mIsLandscape) {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
            mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.WHITE, 1.0f);
        } else {
            mStreamer.showWaterMarkLogo(mLogoPath, 0.05f, 0.09f, 0, 0.20f, 0.8f);
            mStreamer.showWaterMarkTime(0.01f, 0.03f, 0.22f, Color.WHITE, 1.0f);
        }
    }

    private void hideWaterMark() {
        mStreamer.hideWaterMarkLogo();
        mStreamer.hideWaterMarkTime();
    }

    // Example to handle camera related operation
    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mStreamer.getCameraCapture().setCameraParameters(parameters);
        }
    }

    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    if (mAutoStart) {
                        startStream();
                    }
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                    mShootingText.setText(STOP_STRING);
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    beginInfoUploadTimer();
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_FILE_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_FILE_SUCCESS");
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                    Toast.makeText(LivingActivity.this, "Network not good!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };

    private void handleEncodeError() {
        int encodeMethod = mStreamer.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mStreamer.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            if (mHWEncoderUnsupported) {
                mStreamer.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                onBeautyChecked(true);
                Log.e(TAG, "Got SW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
                Log.e(TAG, "Got SW encoder error, switch to HARDWARE mode");
            }
        }
    }

    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                //Camera was disconnected due to use by higher priority user.
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_EVICTED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();
                        }
                    }, 5000);
                    break;
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_FORMAT_NOT_SUPPORTED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                    stopRecord();
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
                    handleEncodeError();
                    stopStream();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startStream();
                        }
                    }, 3000);
                }
                break;
                default:
                    if (mStreamer.getEnableAutoRestart()) {
                        mShootingText.setText(START_STRING);
                        mShootingText.postInvalidate();
                        mRecording = false;
                        stopChronometer();
                    } else {
                        stopStream();
                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startStream();
                            }
                        }, 3000);
                    }
                    break;
            }
        }
    };

    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };

    private OnAudioRawDataListener mOnAudioRawDataListener = new OnAudioRawDataListener() {
        @Override
        public short[] OnAudioRawData(short[] data, int count) {
            Log.d(TAG, "OnAudioRawData data.length=" + data.length + " count=" + count);
            //audio pcm data
            return data;
        }
    };

    private OnPreviewFrameListener mOnPreviewFrameListener = new OnPreviewFrameListener() {
        @Override
        public void onPreviewFrame(byte[] data, int width, int height, boolean isRecording) {
            Log.d(TAG, "onPreviewFrame data.length=" + data.length + " " +
                    width + "x" + height + " mRecording=" + isRecording);
        }
    };

    private void onSwitchCamera() {
        mStreamer.switchCamera();
        mCameraHintView.hideAll();
    }

    private void onFlashClick() {
        if (isFlashOpened) {
            mStreamer.toggleTorch(false);
            isFlashOpened = false;
        } else {
            mStreamer.toggleTorch(true);
            isFlashOpened = true;
        }
    }

    private void onBackoffClick() {
        new AlertDialog.Builder(LivingActivity.this).setCancelable(true)
                .setTitle("结束直播?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mChronometer.stop();
                        mRecording = false;
                        LivingActivity.this.finish();
                    }
                }).show();
    }

    private void onShootClick() {
        if (mRecording) {
            stopStream();
        } else {
            startStream();
        }
    }

    private void onRecordClick() {
        if (mIsFileRecording) {
            stopRecord();
        } else {
            startRecord();
        }
    }

    private boolean[] mChooseFilter = {false, false};


    private void onBeautyChecked(boolean isChecked) {
        if (mStreamer.getVideoEncodeMethod() == StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT) {
            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(), isChecked ?
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE :
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            mStreamer.setEnableImgBufBeauty(isChecked);
        } else {

        }
    }


    private void onBgmChecked(boolean isChecked) {
        if (isChecked) {
            // use KSYMediaPlayer instead of KSYBgmPlayer
            mStreamer.getAudioPlayerCapture().setEnableMediaPlayer(true);
            mStreamer.getAudioPlayerCapture().getMediaPlayer()
                    .setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(IMediaPlayer iMediaPlayer) {
                            Log.d(TAG, "End of the currently playing music");
                        }
                    });
            mStreamer.getAudioPlayerCapture().getMediaPlayer()
                    .setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                            Log.e(TAG, "OnErrorListener, Error:" + what + ", extra:" + extra);
                            return false;
                        }
                    });
            mStreamer.setEnableAudioMix(true);
            mStreamer.startBgm(mBgmPath, true);
            mStreamer.getAudioPlayerCapture().getMediaPlayer().setVolume(0.4f, 0.4f);
        } else {
            mStreamer.stopBgm();
        }
    }

    private void onAudioPreviewChecked(boolean isChecked) {
        mStreamer.setEnableAudioPreview(isChecked);
    }

    private void onMuteChecked(boolean isChecked) {
        mStreamer.setMuteAudio(isChecked);
    }

    private void onWaterMarkChecked(boolean isChecked) {
        if (isChecked)
            showWaterMark();
        else
            hideWaterMark();
    }

    private void onFrontMirrorChecked(boolean isChecked) {
        mStreamer.setFrontCameraMirror(isChecked);
    }

    private void onAudioOnlyChecked(boolean isChecked) {
        mStreamer.setAudioOnly(isChecked);
    }


    private class ButtonObserver implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.switch_cam:
                    onSwitchCamera();
                    break;
//                case R.id.backoff:
//                    onBackoffClick();
//                    break;
                case R.id.flash:
                    onFlashClick();
                    break;
                case R.id.click_to_shoot:
                    onShootClick();
                    break;
                case R.id.close:
                    back();
                    break;
                default:
                    break;
            }
        }

    }


    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mStreamer.startCameraPreview();
        }
    }


    private void    createLive(String name) {
        onShowProgressDlg();
        FormBody formBody = new FormBody.Builder()
                .add("uid", PreferenceUtils.getsessionId(this))
                .add("pic", pic[new Random().nextInt(5)])
                .add("live_name", name)
                .add("live_type", new Random().nextInt(2) + "")
                .build();
        HomeApi homeApi = RetrofitManager.getTestRetrofit().create(HomeApi.class);
        Call<CreatLiveBean> checkUserCall = homeApi.live_create(formBody);
        checkUserCall.enqueue(new Callback<CreatLiveBean>() {
            @Override
            public void onResponse(Call<CreatLiveBean> call, Response<CreatLiveBean> response) {
                cancelProgressDlg();
                if (response.body().getError_code() == 0) {
                    close.setVisibility(View.VISIBLE);
                    live_id = response.body().getResult().getId() + "";
                    mStreamer.setUrl(UrlConfig.UP_LIVE + response.body().getResult().getId());
                    Log.d("TAG", "response.body().getResult().getId()");
                    mStreamer.startStream();
                    mShootingText.setText(STOP_STRING);
                    mShootingText.postInvalidate();
                    mRecording = true;
                    mShootingText.setVisibility(View.GONE);
                    liveName.setVisibility(View.GONE);
                    updateLiveStatus(response.body().getResult().getId() + "", "0");//把直播改成直播中
                } else {
                    ToastUtils.showLong("创建失败");
                }
            }

            @Override
            public void onFailure(Call<CreatLiveBean> call, Throwable t) {
                ToastUtils.showLong(getResources().getString(R.string.net_error));
                cancelProgressDlg();
            }
        });
    }

    private void updateLiveStatus(String id, String status) {
        onShowProgressDlg();
        FormBody formBody = new FormBody.Builder()
                .add("live_id", id)
                .add("status", status)
                .build();
        HomeApi homeApi = RetrofitManager.getTestRetrofit().create(HomeApi.class);
        Call<UpdateLiveBean> checkUserCall = homeApi.live_status_update(formBody);
        checkUserCall.enqueue(new Callback<UpdateLiveBean>() {
            @Override
            public void onResponse(Call<UpdateLiveBean> call, Response<UpdateLiveBean> response) {
                cancelProgressDlg();
                if (response.body().getError_code() == 0) {
                    ToastUtils.showLong("更改状态成功");
                } else {
                    ToastUtils.showLong("创建失败");
                }
            }

            @Override
            public void onFailure(Call<UpdateLiveBean> call, Throwable t) {
                ToastUtils.showLong(getResources().getString(R.string.net_error));
                cancelProgressDlg();
            }
        });
    }


    public void back() {
        new android.support.v7.app.AlertDialog.Builder(this).setTitle("温馨提示").setMessage("确定结束直播？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopStream();
                updateLiveStatus(live_id, "1");//把状态改成非直播
                finish();

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

}
