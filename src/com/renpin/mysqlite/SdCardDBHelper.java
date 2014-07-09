package com.renpin.mysqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SdCardDBHelper extends SQLiteOpenHelper {

	public static final String TAG = "SdCardDBHelper";
	// 存储相应照片的表
	public static final String TABLE_NAME = "mytaskpicture";
	// 存储相应照片的表
	public static String TABLE_NAME1 = "";
	// 新建表的sql语句
	public static String strNewCreateTableSQL = "";
	/**
	 * 数据库名称
	 **/
	public static String DATABASE_NAME = "renpin.db";
	// 存储当前数据库版本号的文件名称
	public static final String strVersionFileName = "version.txt";

	/**
	 * 数据库版本
	 **/
	public static int DATABASE_VERSION = 1;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文环境
	 **/
	public SdCardDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 修改数据库版本,同时生成新建表语句
	public static void ChangeVersionAndCreateTable(String strTableName) {
		DATABASE_VERSION += 1;
		TABLE_NAME1 = strTableName;
		//更新数据库版本号
		OperaDatabase.WriteVersionValue(DATABASE_VERSION);
	}

	/**
	 * 创建数据库时触发，创建离线存储所需要的数据库表
	 * 
	 * @param db
	 **/
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// 创建图片的表
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
	 * 更新数据库时触发，
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
