package com.example.hoang.fitness.utils;

import android.content.Context;

import com.example.hoang.fitness.models.CustomWorkout;
import com.example.hoang.fitness.models.Medal;
import com.example.hoang.fitness.models.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<Target> docFileTarget(Context context,String fileName){
        List<Target> listDeThi = new ArrayList<>();
        try {
            File file = context.getFileStreamPath(fileName);
            if (file==null||!file.exists()){
                file = new File(fileName);
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listDeThi = (List<Target>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listDeThi;
    }

    public static void ghiFileTarget(Context context, List<Target> listDeThi){
        try {
            File file = context.getFileStreamPath("target.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listDeThi);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CustomWorkout> docFileCustomWorkout(Context context, String fileName){
        List<CustomWorkout> listDeThi = new ArrayList<>();
        try {
            File file = context.getFileStreamPath(fileName);
            if (file==null||!file.exists()){
                file = new File(fileName);
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listDeThi = (List<CustomWorkout>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listDeThi;
    }

    public static void ghiFileCustomWorkout(Context context, List<CustomWorkout> listDeThi){
        try {
            File file = context.getFileStreamPath("customworkout.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listDeThi);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Medal> docFileMedal(Context context, String fileName){
        List<Medal> listDeThi = new ArrayList<>();
        try {
            File file = context.getFileStreamPath(fileName);
            if (file==null||!file.exists()){
                file = new File(fileName);
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listDeThi = (List<Medal>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listDeThi;
    }

    public static void ghiFileMedal(Context context, List<Medal> listDeThi){
        try {
            File file = context.getFileStreamPath("medal.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listDeThi);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initMedal(Context context){
        List<Medal> list = new ArrayList<>();
        list.add(new Medal(0,"ic_bronze_medal",0, true));
        list.add(new Medal(1,"ic_silver_medal",1000, false));
        list.add(new Medal(2,"ic_gold_medal",2000, false));
        list.add(new Medal(3,"ic_ta_dong",500, false));
        list.add(new Medal(4,"ic_ta_bac",1000,false));
        list.add(new Medal(5,"ic_ta_vang",2000, false));
        ghiFileMedal(context,list);
    }
}
