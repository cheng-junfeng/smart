package com.audio.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.audio.R;
import com.audio.ui.bean.AudioInfoBean;
import com.base.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioItemViewHolder> {
    //语音动画控制器
    private Timer mTimer = null;
    //语音动画控制任务
    private TimerTask mTimerTask = null;
    //记录语音动画图片
    private int index = 1;
    private AudioAnimationHandler audioAnimationHandler = null;

    private ArrayList<AudioInfoBean> list;
    private Activity context;
    private MediaPlayer audioPlayer;
    private int pos = 0;
    private String id;
    private boolean isChange = false;


    public boolean getChange() {
        return isChange;
    }

    public AudioAdapter(Activity context, MediaPlayer mediaPlayer) {
        this.context = context;
        list = new ArrayList<AudioInfoBean>();
        this.audioPlayer = mediaPlayer;
    }

    @Override
    public AudioItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_play, parent, false);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AudioItemViewHolder holder, final int position) {

        if (TextUtils.isEmpty(list.get(position).getAudioTime())) {
            holder.tvTime.setText("");
            new SecondCountAsycTask(holder.tvTime, position).execute();
        } else {
            holder.tvTime.setText(list.get(position).getAudioTime() + "\"");
        }

        holder.rlAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getAduioPath() == null) {
                    Toast.makeText(context, "未获取到音频文件", Toast.LENGTH_SHORT).show();
                } else {
                    play(position);
                    //播完一首自动播放下一首
                    audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            pos = position + 1 + pos;
                            nextSong(pos);
                            playAudioAnimation(holder.tvSoundWave, audioPlayer);
                        }
                    });
                    playAudioAnimation(holder.tvSoundWave, audioPlayer);
                }
            }
        });

        holder.tvAudioDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确认删除该音频")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                pos = position;
                                deleteMedia();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    //播放音频
    public void play(int position) {
        try {
            if (position <= list.size() - 1) {
                audioPlayer.reset();
                audioPlayer.setDataSource(list.get(position).getAduioPath());
                audioPlayer.prepare();
                audioPlayer.start();
            } else {
                audioPlayer.stop();
                pos = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //播放下一首
    private void nextSong(int position) {
        if (position <= list.size() - 1) {
            play(position);
        } else {
            audioPlayer.stop();
            pos = 0;
        }
    }

    public class SecondCountAsycTask extends AsyncTask<Integer, Integer, String> {
        private TextView textView;
        private int position;
        MediaPlayer mAudioPlayer;

        public SecondCountAsycTask(TextView textView, int position) {
            super();
            this.textView = textView;
            this.position = position;
            mAudioPlayer = new MediaPlayer();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {

                mAudioPlayer.reset();
                mAudioPlayer.setDataSource(list.get(position).getAduioPath());
                mAudioPlayer.prepare();
                LogUtil.d("TAG", "" + mAudioPlayer.getDuration());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return String.valueOf(mAudioPlayer.getDuration() / 1000) + "\"";
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
            mAudioPlayer.release();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public AudioInfoBean getItem(int position) {
        if (position > getItemCount() - 1) {
            return null;
        }
        return list.get(position);
    }

    public void addData(AudioInfoBean bean) {
        if (bean == null) {
            return;
        }
        list.add(bean);
        notifyDataSetChanged();

    }

    public void addData(ArrayList<AudioInfoBean> beans) {
        list.clear();
        if (beans != null && beans.size() > 0) {
            list.addAll(beans);
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        list.remove(position);
        notifyDataSetChanged();

    }

    class AudioItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llFirstAudio;
        private RelativeLayout rlAudio;
        private ImageView tvSoundWave;
        private TextView tvTime;
        private TextView tvAudioDelete;

        public AudioItemViewHolder(View itemVeiew) {
            super(itemVeiew);
            tvTime = (TextView) itemView.findViewById(R.id.tvAudioTime);
            tvSoundWave = (ImageView) itemVeiew.findViewById(R.id.tvSoundWave);
            llFirstAudio = (LinearLayout) itemView.findViewById(R.id.llFirstAudio);
            rlAudio = (RelativeLayout) itemView.findViewById(R.id.rlAudio);
            tvAudioDelete = (TextView) itemView.findViewById(R.id.tvAudioDelete);
        }
    }

    /**
     * 播放语音图标动画
     */
    private void playAudioAnimation(final ImageView imageView, final MediaPlayer mMediaPlayer) {
        //定时器检查播放状态
        stopTimer();
        mTimer = new Timer();
        //将要关闭的语音图片归位
        if (audioAnimationHandler != null) {
            Message msg = new Message();
            msg.what = 3;
            audioAnimationHandler.sendMessage(msg);
        }

        audioAnimationHandler = new AudioAnimationHandler(imageView);
        mTimerTask = new TimerTask() {
            public boolean hasPlayed = false;

            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    hasPlayed = true;
                    index = (index + 1) % 3;
                    Message msg = new Message();
                    msg.what = index;
                    audioAnimationHandler.sendMessage(msg);
                } else {
                    //当播放完时
                    Message msg = new Message();
                    msg.what = 3;
                    audioAnimationHandler.sendMessage(msg);
//                    播放完毕时需要关闭Timer等
                    if (hasPlayed) {
                        stopTimer();
                    }
                }
            }
        };

        //调用频率为500毫秒一次
        mTimer.schedule(mTimerTask, 0, 300);
    }

    class AudioAnimationHandler extends Handler {
        ImageView imageView;

        public AudioAnimationHandler(ImageView imageView) {
            this.imageView = imageView;

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //根据msg.what来替换图片，达到动画效果
            switch (msg.what) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_sound_wave1);
                    break;

                case 1:
                    imageView.setImageResource(R.drawable.ic_sound_wave2);
                    break;

                case 2:
                    imageView.setImageResource(R.drawable.ic_sound_wave3);
                    break;

                default:
                    imageView.setImageResource(R.drawable.ic_sound_wave);
                    break;
            }
        }

    }

    public ArrayList<AudioInfoBean> getDatas() {
        return list;
    }

    /**
     * 停止
     */
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

    public void deleteMedia() {
        isChange = true;
        removeData(pos);
    }
}
