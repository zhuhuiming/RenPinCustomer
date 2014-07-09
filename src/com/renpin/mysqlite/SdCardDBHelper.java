package com.renpin.mysqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SdCardDBHelper extends SQLiteOpenHelper {

	public static final String TAG = "SdCardDBHelper";
	// �洢��Ӧ��Ƭ�ı�
	public static final String TABLE_NAME = "mytaskpicture";
	// �洢��Ӧ��Ƭ�ı�
	public static String TABLE_NAME1 = "";
	// �½����sql���
	public static String strNewCreateTableSQL = "";
	/**
	 * ���ݿ�����
	 **/
	public static String DATABASE_NAME = "renpin.db";
	// �洢��ǰ���ݿ�汾�ŵ��ļ�����
	public static final String strVersionFileName = "version.txt";

	/**
	 * ���ݿ�汾
	 **/
	public static int DATABASE_VERSION = 1;

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            �����Ļ���
	 **/
	public SdCardDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// �޸����ݿ�汾,ͬʱ�����½������
	public static void ChangeVersionAndCreateTable(String strTableName) {
		DATABASE_VERSION += 1;
		TABLE_NAME1 = strTableName;
		//�������ݿ�汾��
		OperaDatabase.WriteVersionValue(DATABASE_VERSION);
	}

	/**
	 * �������ݿ�ʱ�������������ߴ洢����Ҫ�����ݿ��
	 * 
	 * @param db
	 **/
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// ����ͼƬ�ı�
			db.execSQL("create table if not exists "
					+ TABLE_NAME
					+ "(_id integer primary key autoincrement,taskid integer(10),"
					+ "tasktype integer(3),SmallIcon1 MEDIUMTEXT,"
					+ "SmallIcon2 MEDIUMTEXT,SmallIcon3 MEDIUMTEXT,SmallIcon4 MEDIUMTEXT,"
					+ "SmallIcon5 MEDIUMTEXT,SmallIcon6 MEDIUMTEXT,LargeIcon1 MEDIUMTEXT,"
					+ "LargeIcon2 MEDIUMTEXT,LargeIcon3 MEDIUMTEXT,LargeIcon4 MEDIUMTEXT,"
					+ "LargeIcon5 MEDIUMTEXT,LargeIcon6 MEDIUMTEXT)");

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	/**
	 * �������ݿ�ʱ������
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 **/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
		db.execSQL("create table if not exists " + TABLE_NAME1
				+ "(_id integer primary key autoincrement,"
				+ "TaskKey varchar(20)," + "TaskTalkTime varchar(20),"
				+ "TaskTalkPersonName varchar(20),"
				+ "TaskTalkAcceptPeraonName varchar(20),"
				+ "TaskTalkContent varchar(140),"
				+ "TaskAccountImage MEDIUMTEXT,"
				+ "TaskTalkPersonToAcceptPersonImage MEDIUMTEXT,"
				+ "TaskTalkPersonToAcceptPersonLargeImage MEDIUMTEXT,"
				+ "CommentIndex Integer(20),"
				+ "TaskTalkPersonTrueName varchar(20),"
				+ "TaskTalkAcceptPeraonTrueName varchar(20),"
				+ "SecretType Integer(1))");
		onCreate(db);
	}
}
