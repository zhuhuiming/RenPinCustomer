package com.renpin.mysqlite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.renpin.location.MyApplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseContext extends ContextWrapper {

	/**
	 * ���캯��
	 * 
	 * @param base
	 *            �����Ļ���
	 */
	public DatabaseContext(Context base) {
		super(base);
	}

	/**
	 * ������ݿ�·������������ڣ��򴴽��������
	 * 
	 * @param name
	 * @param mode
	 * @param factory
	 */
	@Override
	public File getDatabasePath(String name) {
		// �ж��Ƿ����sd��
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// ���������,
			Log.e("SD��������", "SD�������ڣ������SD��");
			return null;
		} else {// �������
				// ��ȡsd��·��
			String dbDir = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			dbDir += "/";
			dbDir += MyApplication.downloadDir;// ���ݿ�����Ŀ¼
			String dbPath = dbDir + name;// ���ݿ�·��
			String txtPath = dbDir + SdCardDBHelper.strVersionFileName;
			// �ж�Ŀ¼�Ƿ���ڣ��������򴴽���Ŀ¼
			File dirFile = new File(dbDir);
			if (!dirFile.exists())
				dirFile.mkdirs();

			// ���ݿ��ļ��Ƿ񴴽��ɹ�
			boolean isFileCreateSuccess = false;
			// �ж��ļ��Ƿ���ڣ��������򴴽����ļ�
			File dbFile = new File(dbPath);
			File txtFile = new File(txtPath);
			if (!dbFile.exists()) {
				try {
					isFileCreateSuccess = dbFile.createNewFile();// �������ݿ��ļ�
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				isFileCreateSuccess = true;
			
			if (!txtFile.exists()) {
				try {
					txtFile.createNewFile();// ����txt�ļ�
					// ��ʼ��txt��д���ʼ�汾��
					FileWriter fw = new FileWriter(txtPath);
					fw.flush();
					fw.write("1");
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// �������ݿ��ļ�����
			if (isFileCreateSuccess)
				return dbFile;
			else
				return null;
		}
	}

	/**
	 * ���������������������SD���ϵ����ݿ�ģ�android 2.3�����»�������������
	 * 
	 * @param name
	 * @param mode
	 * @param factory
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			SQLiteDatabase.CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(name), null);
		return result;
	}

	/**
	 * Android 4.0����ô˷�����ȡ���ݿ⡣
	 * 
	 * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
	 *      int, android.database.sqlite.SQLiteDatabase.CursorFactory,
	 *      android.database.DatabaseErrorHandler)
	 * @param name
	 * @param mode
	 * @param factory
	 * @param errorHandler
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(name), null);
		return result;
	}

}