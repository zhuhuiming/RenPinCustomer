package com.renpin.mysqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import com.renpin.domin.CommentInfo;
import com.renpin.domin.TaskIcon;
import com.renpin.location.MyApplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OperaDatabase {

	SdCardDBHelper mdbHelper = null;
	private Context mContext = null;

	// 判断存储版本号的文件是否存在,如果不存在那么就将数据库文件也删除掉
	private void DealFile() {
		// 判断是否存在sd卡
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// 如果不存在,
			Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
			return;
		} else {// 如果存在
				// 获取sd卡路径
			String dbDir = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			dbDir += "/";
			dbDir += MyApplication.downloadDir;// 数据库所在目录
			String dbPath = dbDir + SdCardDBHelper.DATABASE_NAME;// 数据库路径
			String txtPath = dbDir + SdCardDBHelper.strVersionFileName;
			// 判断目录是否存在，不存在则创建该目录
			File dbFile = new File(dbPath);
			File txtFile = new File(txtPath);
			// 判断目录是否存在，不存在则创建该目录
			File dirFile = new File(dbDir);
			if (!dirFile.exists())
				dirFile.mkdirs();

			// 如果txt文件不存在,数据库文件存在,那么就将数据库文件删除
			if (dbFile.exists() && !txtFile.exists()) {
				dbFile.delete();
			}

			if (!dbFile.exists()) {
				try {
					dbFile.createNewFile();// 创建数据库文件
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 如果txt文件不存在,那么就创建并赋初始值
			if (!txtFile.exists()) {
				try {
					txtFile.createNewFile();// 创建txt文件
					// 开始往txt中写入初始版本号
					FileWriter fw = new FileWriter(txtPath);
					fw.flush();
					fw.write("1");
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			String strVersion = "";
			// 如果txt文件存在,那么就从该文件中获取版本号
			try {
				FileInputStream fin = new FileInputStream(txtPath);
				int length = fin.available();
				byte[] buffer = new byte[length];
				fin.read(buffer);
				strVersion = EncodingUtils.getString(buffer, "UTF-8");
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!strVersion.equals("")) {
				// 将读出来的版本号转换成整数同时赋值到数据库版本变量中
				int VersionValue = Integer.parseInt(strVersion);
				SdCardDBHelper.DATABASE_VERSION = VersionValue;
			}
		}
	}

	public static boolean WriteVersionValue(int version) {
		boolean bIsSuccess = false;
		// 判断是否存在sd卡
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// 如果不存在,
			Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
			return false;
		} else {// 如果存在
				// 获取sd卡路径
			String dbDir = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			dbDir += "/";
			dbDir += MyApplication.downloadDir;// 数据库所在目录
			String txtPath = dbDir + SdCardDBHelper.strVersionFileName;
			File txtFile = new File(txtPath);
			if (txtFile.exists()) {
				try {
					// 开始往txt中写入初始版本号
					FileWriter fw = new FileWriter(txtPath);
					fw.flush();
					fw.write("" + version);
					fw.close();
					bIsSuccess = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bIsSuccess;
	}

	public OperaDatabase(Context context) {
		if (mdbHelper != null) {
			mdbHelper.close();
			mdbHelper = null;
		}
		mContext = context;
		DatabaseContext dbContext = new DatabaseContext(context);
		DealFile();
		mdbHelper = new SdCardDBHelper(dbContext);
	}

	public void CloseDatabase() {
		if (mdbHelper != null) {
			mdbHelper.close();
			mdbHelper = null;
		}
	}

	// 插入任务或分享的大小图片
	public Boolean InsertImage(int id, int ntype, String smallicon1,
			String smallicon2, String smallicon3, String smallicon4,
			String smallicon5, String smallicon6, String largeicon1,
			String largeicon2, String largeicon3, String largeicon4,
			String largeicon5, String largeicon6) {

		Boolean bIsSucceed = false;
		if (mdbHelper != null) {
			SQLiteDatabase db = mdbHelper.getWritableDatabase();

			ContentValues cv = new ContentValues();
			cv.put("taskid", id);
			cv.put("tasktype", ntype);
			cv.put("SmallIcon1", smallicon1);
			cv.put("SmallIcon2", smallicon2);
			cv.put("SmallIcon3", smallicon3);
			cv.put("SmallIcon4", smallicon4);
			cv.put("SmallIcon5", smallicon5);
			cv.put("SmallIcon6", smallicon6);
			cv.put("LargeIcon1", largeicon1);
			cv.put("LargeIcon2", largeicon2);
			cv.put("LargeIcon3", largeicon3);
			cv.put("LargeIcon4", largeicon4);
			cv.put("LargeIcon5", largeicon5);
			cv.put("LargeIcon6", largeicon6);

			db.insert(SdCardDBHelper.TABLE_NAME, null, cv);
			db.close();
		}
		return bIsSucceed;
	}

	// 获取小图片
	public TaskIcon GetSmallImage(String strTaskId, String strTaskType) {

		TaskIcon taskicon = null;
		if (mdbHelper != null) {
			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				int nId = Integer.parseInt(strTaskId);
				int nType = Integer.parseInt(strTaskType);
				String strSQL = "SELECT * FROM ";
				strSQL += SdCardDBHelper.TABLE_NAME;
				strSQL += " where taskid = '";
				strSQL += nId;
				strSQL += "' and tasktype = '";
				strSQL += nType;
				strSQL += "'";
				Cursor c = db.rawQuery(strSQL, null);
				while (c.moveToNext()) {
					taskicon = new TaskIcon(c.getString(3), c.getString(4),
							c.getString(5), c.getString(6), c.getString(7),
							c.getString(8));
					break;
				}
				c.close();
				db.close();
			}
		}
		return taskicon;
	}

	// 获取大图片
	public TaskIcon GetLargeImage(String strTaskId, String strTaskType) {
		CreateCommentTable(1, 1);
		TaskIcon taskicon = null;
		if (mdbHelper != null) {
			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				int nId = Integer.parseInt(strTaskId);
				int nType = Integer.parseInt(strTaskType);
				String strSQL = "SELECT * FROM ";
				strSQL += SdCardDBHelper.TABLE_NAME;
				strSQL += " where taskid = '";
				strSQL += nId;
				strSQL += "' and tasktype = '";
				strSQL += nType;
				strSQL += "'";
				Cursor c = db.rawQuery(strSQL, null);
				while (c.moveToNext()) {
					taskicon = new TaskIcon(c.getString(9), c.getString(10),
							c.getString(11), c.getString(12), c.getString(13),
							c.getString(14));
					break;
				}
				c.close();
				db.close();
			}
		}
		return taskicon;
	}

	// 判断表是否存在
	public boolean IsTableExist(int id, int ntype) {
		boolean bIsExist = true;
		String strTableName = "";
		if (1 == ntype) {
			strTableName = "h" + id;
		} else if (2 == ntype) {
			strTableName = "s" + id;
		}
		if (mdbHelper != null) {

			SQLiteDatabase tab = mdbHelper.getWritableDatabase();
			if (tab != null) {
				// 先判断表是否存在
				String strSQL = "SELECT COUNT(*) as count FROM sqlite_master WHERE type='table' "
						+ "and name = '";
				strSQL += strTableName;
				strSQL += "'";
				Cursor c = tab.rawQuery(strSQL, null);
				int nCount = -1;
				while (c.moveToNext()) {
					nCount = c.getInt(0);
					break;
				}
				c.close();
				tab.close();
				// 如果不存在
				if (0 == nCount) {
					bIsExist = false;
					SdCardDBHelper.ChangeVersionAndCreateTable(strTableName);
				}
			}
		}
		return bIsExist;
	}

	private String CreateCommentTable(int id, int ntype) {
		String strTableName = "";
		if (1 == ntype) {
			strTableName = "h" + id;
		} else if (2 == ntype) {
			strTableName = "s" + id;
		}
		if (mdbHelper != null) {

			SQLiteDatabase tab = mdbHelper.getWritableDatabase();
			if (tab != null) {
				// 先判断表是否存在
				String strSQL = "SELECT COUNT(*) as count FROM sqlite_master WHERE type='table' "
						+ "and name = '";
				strSQL += strTableName;
				strSQL += "'";
				Cursor c = tab.rawQuery(strSQL, null);
				int nCount = -1;
				while (c.moveToNext()) {
					nCount = c.getInt(0);
					break;
				}
				c.close();
				tab.close();
				// 如果不存在
				if (0 == nCount) {
					//SdCardDBHelper.ChangeVersionAndCreateTable(strTableName);
				}
			}
		}
		return strTableName;
	}

	// 获取本地数据库中的所有的评论数据
	public List<CommentInfo> GetCommentData(int id, int ntype) {

		List<CommentInfo> comments = new ArrayList<CommentInfo>();
		if (mdbHelper != null) {
			// 判断有没有相应的表,如果没有就创建
			String strTable = CreateCommentTable(id, ntype);

			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				String strSQL = "SELECT * FROM ";
				strSQL += strTable;
				Cursor c = db.rawQuery(strSQL, null);
				while (c.moveToNext()) {
					CommentInfo comment = new CommentInfo(c.getString(6),
							c.getString(3), c.getString(4), c.getString(2),
							c.getString(5), c.getString(7), c.getInt(9),
							c.getString(10), c.getString(11));
					comments.add(comment);
				}
				c.close();
				db.close();
			}
		}
		return comments;
	}

	// 插入评论数据
	public void InsertCommentData(int id, int ntype, int nIndex,
			String strPersonImage, String strPersonName,
			String ReceivePersonName, String strCommentTime,
			String strCommentContent, String strTruePersonName,
			String strTrueReceivePersonName, String strSmallImage,
			int nSecretType) {

		if (mdbHelper != null) {
			// 判断有没有相应的表,如果没有就创建
			String strTable = CreateCommentTable(id, ntype);

			SQLiteDatabase db = mdbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("TaskKey", id + "");
			cv.put("CommentIndex", nIndex);
			cv.put("TaskAccountImage", strPersonImage);
			cv.put("TaskTalkPersonName", strPersonName);
			cv.put("TaskTalkAcceptPeraonName", ReceivePersonName);
			cv.put("TaskTalkTime", strCommentTime);
			cv.put("TaskTalkContent", strCommentContent);
			cv.put("TaskTalkPersonTrueName", strTruePersonName);
			cv.put("TaskTalkAcceptPeraonTrueName", strTrueReceivePersonName);
			cv.put("TaskTalkPersonToAcceptPersonImage", strSmallImage);
			cv.put("TaskTalkPersonToAcceptPersonLargeImage", "");
			cv.put("SecretType", nSecretType);
			db.insert(strTable, null, cv);
			db.close();
		}
	}

	// 获取评论中的大图片
	public String GetCommentLargeImage(int id, int ntype, int nCommentIndex) {

		String strLargeImage = "";
		if (mdbHelper != null) {
			// 判断有没有相应的表,如果没有就创建
			String strTable = CreateCommentTable(id, ntype);

			String strId = id + "";
			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				String strSQL = "SELECT TaskTalkPersonToAcceptPersonLargeImage FROM ";
				strSQL += strTable;
				strSQL += " where TaskKey = '";
				strSQL += strId;
				strSQL += "' and _id = '";
				strSQL += nCommentIndex;
				strSQL += "'";
				Cursor c = db.rawQuery(strSQL, null);
				while (c.moveToNext()) {
					strLargeImage = c.getString(0);
					break;
				}
				c.close();
				db.close();
			}
		}
		return strLargeImage;
	}

	// 获取评论中最新的时间
	public String GetCommentTime(int id, int ntype) {

		String strTime = "";
		if (mdbHelper != null) {
			// 判断有没有相应的表,如果没有就创建
			String strTable = CreateCommentTable(id, ntype);

			String strId = id + "";
			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				String strSQL = "SELECT TaskTalkTime FROM ";
				strSQL += strTable;
				strSQL += " where TaskKey = '";
				strSQL += strId;
				strSQL += "'";
				Cursor c = db.rawQuery(strSQL, null);
				while (c.moveToNext()) {
					c.moveToLast();
					strTime = c.getString(0);
					break;
				}
				c.close();
				db.close();
			}
		}
		return strTime;
	}

	// 设置评论中的大图片
	public String SetCommentLargeImage(int id, int ntype, int nIndex,
			String strLargeImage) {

		if (mdbHelper != null) {
			// 判断有没有相应的表,如果没有就创建
			String strTable = CreateCommentTable(id, ntype);

			String strId = id + "";
			SQLiteDatabase db = mdbHelper.getReadableDatabase();
			if (db != null) {
				String strSQL = "update ";
				strSQL += strTable;
				strSQL += " set TaskTalkPersonToAcceptPersonLargeImage = '";
				strSQL += strLargeImage;
				strSQL += "' where TaskKey = '";
				strSQL += strId;
				strSQL += "' and _id = '";
				strSQL += nIndex;
				strSQL += "'";
				db.execSQL(strSQL);
				db.close();
			}
		}
		return strLargeImage;
	}
}
