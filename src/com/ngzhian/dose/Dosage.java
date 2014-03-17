package com.ngzhian.dose;

import android.os.Bundle;

;

public class Dosage implements Comparable<Dosage> {
  private int id;
  private String name;
  private int quantity;
  private String unit;
  private long time;
  private int hour;
  private int minute;

  public Dosage() {
  }

  public Dosage(Bundle data) {
    this(data.getString("name"), data.getInt("quantity", 1), data
        .getString("unit"), data.getInt("hour", 1), data.getInt("minute", 0),
        data.getLong("time", System.currentTimeMillis()));
  }

  public Dosage(String name, int quantity, String unit, int hour, int minute,
      long time) {
    this.name = name;
    this.quantity = quantity;
    this.unit = unit;
    this.hour = hour;
    this.minute = minute;
    this.time = time;
    String idString = new String(name + id + unit + hour + minute);
    this.id = idString.hashCode();
  }

  public Dosage(String string) {
    String lines[] = string.split("\\r?\\n");
    this.id = Integer.parseInt(lines[0]);
    this.name = lines[1];
    this.quantity = Integer.parseInt(lines[2]);
    this.unit = lines[3];
    this.time = Long.parseLong(lines[4]);
    this.hour = Integer.parseInt(lines[5]);
    this.minute = Integer.parseInt(lines[6]);
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public void updateTime() {
    // long current = System.currentTimeMillis();
    // if (time <= current) {
    // time += (DateUtils.DAY_IN_MILLIS);
    // }
  }

  public String writeToString() {
    return "" + id + "\n" + name + "\n" + quantity + "\n" + unit + "\n" + time
        + "\n" + hour + "\n" + minute + "\n\n";
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int compareTo(Dosage c) {
    return (int) (this.getTime() - c.getTime());
  }

}
