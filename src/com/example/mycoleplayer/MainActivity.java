package com.example.mycoleplayer;

import java.io.File;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {
	
	  private final String DOWNLOAD_FILE_URL = "http://usi.mods.jp/test.mp4";
	  private ProgressDialog progressDialog;
	  private ProgressHandler progressHandler;
	  private AsyncFileDownload asyncfiledownload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		VideoView videoView = (VideoView)findViewById(R.id.videoView1);
		// インターネット上のファイルを再生
		videoView.setVideoURI(Uri.parse("http://usi.mods.jp/test.mp4"));
		videoView.setMediaController(new MediaController(this));
		videoView.start();
		
		progressHandler = new ProgressHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	public void button1(View view){
	    initFileLoader();
	    showDialog(0);
	    progressHandler.progressDialog = progressDialog;
	    progressHandler.asyncfiledownload = asyncfiledownload;
	     
	    if (progressDialog != null && asyncfiledownload != null){
	      progressDialog.setProgress(0);
	      progressHandler.sendEmptyMessage(0);
	    }else{
	      Toast ts = Toast.makeText(this, "NULLエラー", Toast.LENGTH_LONG);
	      ts.show();
	    }
	  }
	   
	  private void initFileLoader()
	  {
	    File sdCard = Environment.getExternalStorageDirectory();
	    File directory = new File(sdCard.getAbsolutePath() + "/SampleFolder");
	    if(directory.exists() == false){
	      if (directory.mkdir() == true){
	      }else{
	        Toast ts = Toast.makeText(this, "ディレクトリ作成に失敗", Toast.LENGTH_LONG);
	        ts.show(); 
	      }
	    }
	    File outputFile = new File(directory, "test.mp4");
	    asyncfiledownload = new AsyncFileDownload(this,DOWNLOAD_FILE_URL, outputFile);
	    asyncfiledownload.execute();
	 
	    /*
	    //内部メモリの領域を用いる場合
	    File dataDir = this.getFilesDir();
	    File directory = new File(dataDir.getAbsolutePath()+ "/SampleFolder");
	    if(directory.exists() == false){
	      if (directory.mkdir() == true){
	      }else{
	        Toast ts = Toast.makeText(this, "ディレクトリ作成に失敗", Toast.LENGTH_LONG);
	        ts.show(); 
	      }
	    }  
	    File outputFile = new File(directory, "test.jpg");
	    asyncfiledownload = new AsyncFileDownload(this,DOWNLOAD_FILE_URL, outputFile);
	    asyncfiledownload.execute();
	    */
	  }
	   
	  @Override
	  protected void onPause(){
	      super.onPause();
	      cancelLoad();
	  }
	 
	  @Override
	  protected void onStop(){
	      super.onStop();
	      cancelLoad();
	  }
	 
	  private void cancelLoad()
	  {
	    if(asyncfiledownload != null){
	      asyncfiledownload.cancel(true);
	    }
	  }
	   
	  @Override
	  protected Dialog onCreateDialog(int id){
	    switch(id){
	      case 0:
	        progressDialog = new ProgressDialog(this);
	        progressDialog.setIcon(R.drawable.ic_launcher);
	        progressDialog.setTitle("Downloading files..");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Hide",
	            new DialogInterface.OnClickListener() {
	              @Override
	              public void onClick(DialogInterface dialog, int which) {
	              }
	              });
	         
	        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
	            new DialogInterface.OnClickListener() {
	              @Override
	              public void onClick(DialogInterface dialog, int which) {
	                cancelLoad();
	              }
	            });
	      }
	      return progressDialog;
	  }

}
