package com.ngzhian.dose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
  private static final int ADD_CODE = 1;
  private static final int EDIT_CODE = 2;
  private static final int UPDATE_INTERVAL = 3000;

  private DosagesAdapter adapter;
  private DosageComparator comparator;
  private Handler uiUpdateHandler;
  private Runnable uiUpdateRunnable;
  private View selectedDosageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setup();
  }

  private void setup() {
    adapter = new DosagesAdapter(this);
    setListAdapter(adapter);
    setAutoRefresh();
    loadDosagesFromFile();
  }

  private void setAutoRefresh() {
    uiUpdateHandler = new Handler();
    uiUpdateRunnable = new Runnable() {
      @Override
      public void run() {
        refresh();
        uiUpdateHandler.postDelayed(uiUpdateRunnable, UPDATE_INTERVAL);
      }
    };
    uiUpdateRunnable.run();
  }

  private void loadDosagesFromFile() {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          openFileInput("dose")));
      String line;
      StringBuilder sb = new StringBuilder();
      while ((line = br.readLine()) != null) {
        Log.d("dose", "read: " + line);
        if (line.equals("")) {
          adapter.add(new Dosage(sb.toString()));
          sb = new StringBuilder();
        } else {
          sb.append(line);
          sb.append("\n");
        }
      }
      br.close();
    } catch (Exception e) {
      Log.e("dose", e.getMessage());
    }

  }

  public void add(View view) {
    Intent i = new Intent(this, AddActivity.class);
    startActivityForResult(i, ADD_CODE);
  }

  public void edit(int position) {
    Dosage dosage = adapter.getItem(position);
    Intent i = new Intent(this, EditActivity.class);
    i.putExtra("name", dosage.getName());
    i.putExtra("quantity", dosage.getQuantity());
    i.putExtra("unit", dosage.getUnit());
    i.putExtra("id", dosage.getId());
    i.putExtra("hour", dosage.getHour());
    i.putExtra("minute", dosage.getMinute());
    i.putExtra("position", position);
    startActivityForResult(i, EDIT_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    Bundle data = intent.getExtras();
    if (data == null) {
      return;
    }
    if (resultCode == RESULT_OK) {
      super.onActivityResult(requestCode, resultCode, intent);
      switch (requestCode) {
        case EDIT_CODE :
          deleteOrUpdateDosage(data.getBoolean("delete", false), data);
          break;
        case ADD_CODE :
          addDosage(new Dosage(data));
          break;
        default :
          Log.e("dose", "invalid result code");
          break;
      }
      adapter.sort(comparator);
      adapter.notifyDataSetChanged();
      saveData();
    }
  }

  private void deleteOrUpdateDosage(boolean isDelete, Bundle data) {
    if (isDelete) {
      deleteDosage(data.getInt("position", -1));
    } else {
      updateDosage(data.getInt("position", -1), data);
    }

  }

  private void deleteDosage(final int position) {
    if (position < 0 || position >= adapter.getCount()) {
      return;
    }
    getListView().getChildAt(position);
    final View v = selectedDosageView;
    ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
    anim.setDuration(1000);
    anim.start();
    anim.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        adapter.remove(adapter.getItem(position));
        v.setAlpha(1f);
      }

      @Override
      public void onAnimationCancel(Animator animation) {
      }
    });
  }

  private void updateDosage(int position, Bundle data) {
    if (position < 0) {
      return;
    }
    adapter.remove(adapter.getItem(position));
    adapter.add(new Dosage(data));
  }

  private void addDosage(Dosage dosage) {
    adapter.add(dosage);
    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this, AlarmReceiver.class);
    intent.putExtra("name", dosage.getName());
    intent.putExtra("quantity", dosage.getQuantity());
    intent.putExtra("unit", dosage.getUnit());
    intent.putExtra("id", dosage.getId());
    PendingIntent pi = PendingIntent.getBroadcast(this, dosage.getId(), intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    am.set(AlarmManager.RTC_WAKEUP, dosage.getTime(), pi);
  }

  private void saveData() {
    try {
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          openFileOutput("dose", MODE_PRIVATE)));
      for (int i = 0; i < adapter.getCount(); i++) {
        bw.write(adapter.getItem(i).writeToString());
      }
      bw.close();
      Log.d("dose", "Saved");
    } catch (Exception e) {
      Log.e("dose", e.getMessage());
    }
  }

  @Override
  protected void onListItemClick(ListView l, View v, final int position, long id) {
    selectedDosageView = v;
    super.onListItemClick(l, v, position, id);
    edit(position);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sync :
        refresh();
        break;
      default :
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void refresh() {
    adapter.notifyDataSetChanged();
  }
}

// ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
// anim.setDuration(1000);
// anim.start();
// anim.addListener(new Animator.AnimatorListener() {
// @Override
// public void onAnimationStart(Animator animation) {
// }
//
// @Override
// public void onAnimationRepeat(Animator animation) {
// }
//
// @Override
// public void onAnimationEnd(Animator animation) {
// }
//
// @Override
// public void onAnimationCancel(Animator animation) {
// }
// });