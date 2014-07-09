package com.renpin.service.Impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.renpin.domin.CommentInfo;
import com.renpin.domin.CreditAndCharmForTask;
import com.renpin.domin.CustomerInfo;
import com.renpin.domin.DistanceDetail;
import com.renpin.domin.TaskIcon;
import com.renpin.domin.TaskInfo;
import com.renpin.domin.TaskInfoDetail;
import com.renpin.domin.UpdateData;
import com.renpin.service.GoodService;

public class GoodServiceImpl implements GoodService {

	private static final String strBasePath = // "http://localhost:8080/RenPinServerTest/";
	"http://www.meiliangshare.cn:8000/RenPinServer/";

	// "http://59.60.9.202:8000/RenPinServerTest/";

	@Override
	public int SendAdviceText(String strAdvice) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendadvicetext.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strAdvice", strAdvice));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendAdviceText(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendAdviceText(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendadviceret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<TaskInfo> GetTaskInfo(int nType) {

		// 创建请求的url
		String url = strBasePath + "renpin/getTaskInfo.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskType", nType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseHotelsInfoObjJosn(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfo> parseHotelsInfoObjJosn(String json) {
		List<TaskInfo> tasks = new ArrayList<TaskInfo>();
		try {
			JSONArray array = new JSONObject(json).getJSONArray("taskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfo task = new TaskInfo(obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public int RegisterCustomer(String strBase64CustomerIcon, String strName,
			String strPassword, String strCreditValue, String strCharmValue,
			String strNickName, String strSex) {
		int nRetType = 1;// 为0表示成功,1表示失败,2表示账号重名
		// 创建请求的url
		String url = strBasePath + "renpin/registercustomer.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("PersonIcon", strBase64CustomerIcon));
		paramas.add(new BasicNameValuePair("PersonName", strName));
		paramas.add(new BasicNameValuePair("Password", strPassword));
		paramas.add(new BasicNameValuePair("CreditValue", strCreditValue));
		paramas.add(new BasicNameValuePair("CharmValue", strCharmValue));
		paramas.add(new BasicNameValuePair("strNickName", strNickName));
		paramas.add(new BasicNameValuePair("strSex", strSex));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseRegisterType(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseRegisterType(String json) {
		int nTicketNum = 1;// 为0表示成功,1表示失败,2表示账号重名
		try {
			nTicketNum = new JSONObject(json).getInt("bregeditret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public CustomerInfo LogIn(String strName, String strPassword) {
		CustomerInfo customer = null;
		// 创建请求的url
		String url = strBasePath + "renpin/login.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("PersonName", strName));
		paramas.add(new BasicNameValuePair("Password", strPassword));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					customer = parseLogInType(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customer;
	}

	// 解析json的单个对象
	private CustomerInfo parseLogInType(String json) {
		CustomerInfo customer = null;
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("customerinfo");
			customer = new CustomerInfo(obj.getString("sex"),
					obj.getString("detail"), obj.getInt("age"),
					obj.getInt("score"), obj.getString("strIcon"),
					obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
					obj.getString("strCustomerName"),
					obj.getString("strNickName"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return customer;
	}

	@Override
	public int AddTaskInfo(int nTaskType, String strRegionName,
			String strTaskTitle, String strTaskDeatial,
			String strTaskAnnounceTime, String strTaskImpleTime,
			String strTaskAccountName, String strImplementAccountName,
			String strTaskAccountTrueName, String strImplementAccountTrueName,
			double dLongitude, double dLatidude, String strTaskAccountIcon,
			String strTimeLimit,
			String strIcon1,// 第一张压缩图片(下面类似)
			String strIcon2, String strIcon3, String strIcon4, String strIcon5,
			String strIcon6) {
		int nRetType = 1;// 为0表示成功,1表示失败
		// 创建请求的url
		String url = strBasePath + "renpin/addtaskinfo.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nTaskType", nTaskType + ""));
		paramas.add(new BasicNameValuePair("strRegionName", strRegionName));
		paramas.add(new BasicNameValuePair("strTaskTitle", strTaskTitle));
		paramas.add(new BasicNameValuePair("strTaskDeatial", strTaskDeatial));
		paramas.add(new BasicNameValuePair("strTaskAnnounceTime",
				strTaskAnnounceTime));
		paramas.add(new BasicNameValuePair("strTaskImpleTime", strTaskImpleTime));
		paramas.add(new BasicNameValuePair("strTaskAccountName",
				strTaskAccountName));
		paramas.add(new BasicNameValuePair("strImplementAccountName",
				strImplementAccountName));
		paramas.add(new BasicNameValuePair("strTaskAccountTrueName",
				strTaskAccountTrueName));
		paramas.add(new BasicNameValuePair("strImplementAccountTrueName",
				strImplementAccountTrueName));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatidude", dLatidude + ""));
		paramas.add(new BasicNameValuePair("strTaskAccountIcon",
				strTaskAccountIcon));
		paramas.add(new BasicNameValuePair("strTimeLimit", strTimeLimit));
		paramas.add(new BasicNameValuePair("strIcon1", strIcon1));
		paramas.add(new BasicNameValuePair("strIcon2", strIcon2));
		paramas.add(new BasicNameValuePair("strIcon3", strIcon3));
		paramas.add(new BasicNameValuePair("strIcon4", strIcon4));
		paramas.add(new BasicNameValuePair("strIcon5", strIcon5));
		paramas.add(new BasicNameValuePair("strIcon6", strIcon6));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseAddTaskInfoType(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseAddTaskInfoType(String json) {
		int nTicketNum = 1;// 为0表示成功,1表示失败
		try {
			nTicketNum = new JSONObject(json).getInt("baddtaskret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public int SelectTask(String strTaskId, String strPersonName,
			String strType, String strPersonNickName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/selecttask.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("PersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strPersonNickName",
				strPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseTaskSelectType(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseTaskSelectType(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nselecttaskret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<TaskInfo> GetMsgInfoNum(String strAnnounceName) {
		List<TaskInfo> DynamicNews = new ArrayList<TaskInfo>();
		// 创建请求的url
		String url = strBasePath + "renpin/getmsgnum.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					DynamicNews = parseDynameicMsgNum(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	// 解析json的单个对象
	private List<TaskInfo> parseDynameicMsgNum(String json) {
		List<TaskInfo> DynamicNews = new ArrayList<TaskInfo>();
		try {
			JSONArray array = new JSONObject(json).getJSONArray("taskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfo TaskNew = new TaskInfo(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"));
				DynamicNews.add(TaskNew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	@Override
	public int UpdateTaskSelectType(String strAnnounceName, int nValue,
			String strTaskId, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskselecttype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseupdatetaskselecttype(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseupdatetaskselecttype(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("announceret1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UpdateTaskFinishType(int nTaskType, String strAnnounceName,
			int nValue, String strTaskId, String strBase64Image,
			String strCommentContent) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskfinishtype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("Base64Image", strBase64Image));
		paramas.add(new BasicNameValuePair("CommentContent", strCommentContent));
		paramas.add(new BasicNameValuePair("nTaskType", nTaskType + ""));

		try {
			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseupdatetaskfinishtype(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseupdatetaskfinishtype(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("announceret2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UpdateTaskImplementCommentType(String strAnnounceName,
			int nValue, String strTaskId, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath
				+ "renpin/updatetaskimplementcommenttype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseupdatetaskimplementcommenttype(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseupdatetaskimplementcommenttype(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("announceret3");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UpdateTaskVerifiType(int nTaskType, String strAnnounceName,
			int nValue, String strTaskId, String strBase64Image,
			String strCommentContent) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskverifitype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("Base64Image", strBase64Image));
		paramas.add(new BasicNameValuePair("CommentContent", strCommentContent));
		paramas.add(new BasicNameValuePair("nTaskType", nTaskType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseupdatetaskverifitype(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseupdatetaskverifitype(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("implementret1");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UpdateTaskAnnounceCommentType(String strAnnounceName,
			int nValue, String strTaskId, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath
				+ "renpin/updatetaskannouncecommenttype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseupdatetaskannouncecommenttype(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseupdatetaskannouncecommenttype(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("implementret2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<CommentInfo> GetCommentsForTask(String strTaskId, String strType) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		// 创建请求的url
		String url = strBasePath + "renpin/getcomments.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					Comments = parseComments(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	// 解析json的单个对象
	private List<CommentInfo> parseComments(String json) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		try {
			JSONArray array = new JSONObject(json).getJSONArray("commentinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				CommentInfo comment = new CommentInfo(
						obj.getString("strCommentPersonImage"),
						obj.getString("strCommentPersonName"),
						obj.getString("strCommentReceivePersonName"),
						obj.getString("strCommentTime"),
						obj.getString("strCommentContent"),
						obj.getString("strSmallImage"),
						obj.getInt("nCommentIndex"),
						obj.getString("strCommentPersonTrueName"),
						obj.getString("strCommentReceivePersonTrueName"));
				Comments.add(comment);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	@Override
	public String GetCommentsLargeImage(String strTaskId, String strType,
			int nCommentIndex) {
		String strLargeImage = "";
		// 创建请求的url
		String url = strBasePath + "renpin/getcommentslargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("nCommentIndex", nCommentIndex + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strLargeImage = parseGetCommentsLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strLargeImage;
	}

	// 解析json的单个对象
	private String parseGetCommentsLargeImage(String json) {
		String strIcon = "";
		try {
			strIcon = new JSONObject(json).getString("strcommentlargeimage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	@Override
	public int SendCommentContent(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strCommentContent, String strCommentPersonImage,
			String strType, String strCommentPersonNickName,
			String strReceiveCommentPersonNickName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentcontent.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strCommentContent",
				strCommentContent));
		paramas.add(new BasicNameValuePair("strCommentImage",
				strCommentPersonImage));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendCommentContent(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendCommentContent(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendcommentret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int SendCommentImage(String strTaskId, String strCommentPersonName,
			String strReceiveCommentPersonName, String strCommentPersonImage,
			String strSmallImage, String strLargeImage, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName) {

		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strCommentImage",
				strCommentPersonImage));
		paramas.add(new BasicNameValuePair("strSmallImage", strSmallImage));
		paramas.add(new BasicNameValuePair("strLargeImage", strLargeImage));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendCommentImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendCommentImage(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendcommentimageret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public UpdateData GetUpdateSignal(String strCurrentAccountName) {

		UpdateData update = null;
		// 创建请求的url
		String url = strBasePath + "renpin/getupdatasignl.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strCurrentAccountName",
				strCurrentAccountName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					update = parseGetUpdateSignal(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == update) {
			update = new UpdateData(-1, "");
		}
		return update;
	}

	// 解析json的单个对象
	private UpdateData parseGetUpdateSignal(String json) {
		UpdateData update = new UpdateData(-1, "");
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("nupdatevalue");
			update.setnUpdateSignal(obj.getInt("nUpdateSignal"));
			update.setstrUpdateDescribe(obj.getString("strUpdateDescribe"));
			/*
			 * = new CustomerInfo(obj.getString("sex"), obj.getString("detail"),
			 * obj.getInt("age"), obj.getInt("score"), obj.getString("strIcon"),
			 * obj.getInt("nCreditValue"), obj.getInt("nCharmValue"));
			 */
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return update;
	}

	@Override
	public int SetUpdateSignal(String strCurrentAccountName, int nSignal) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/setupdatasignl.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strCurrentAccountName",
				strCurrentAccountName));
		paramas.add(new BasicNameValuePair("strUpdateValue", nSignal + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSetUpdateSignal(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSetUpdateSignal(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsetupdatevalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int SetCreditValue(String strPersonName, int nCreditValue) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/setcreditvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("nCreditValue", nCreditValue + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSetCreditValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSetCreditValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsetcreditvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int SetCharmValue(String strPersonName, int nCharmValue) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/setcharmvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("nCharmValue", nCharmValue + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSetCharmValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSetCharmValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsetcharmvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int GetCreditValue(String strPersonName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/getcreditvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseGetCreditValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseGetCreditValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("ngetcreditvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int GetCharmValue(String strPersonName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/getcharmvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseGetCharmValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseGetCharmValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("ngetcharmvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int AddCreditValue(String strPersonName, int nIncrValue) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/addcreditvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("nIncrValue", nIncrValue + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseAddCreditValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseAddCreditValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("naddcreditvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int AddCharmValue(String strPersonName, int nIncrValue) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/addcharmvalue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("nIncrValue", nIncrValue + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseAddCharmValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseAddCharmValue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("naddcharmvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public long GetTaskRemainTime(String strTaskId, String strType) {
		long lTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/gettaskremaintime.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					lTypeRet = parseGetRemainTimeValue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lTypeRet;
	}

	// 解析json的单个对象
	private long parseGetRemainTimeValue(String json) {
		long lRet = 0;
		try {
			lRet = new JSONObject(json).getLong("ltaskremaintime");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lRet;
	}

	@Override
	public List<TaskInfo> LoadTaskData(int nLimit, int nType, int nMaxTaskId) {
		// 创建请求的url
		String url = strBasePath + "renpin/loadtaskdata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("nMaxTaskId", nMaxTaskId + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseLoadTaskData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfo> parseLoadTaskData(String json) {
		List<TaskInfo> tasks = new ArrayList<TaskInfo>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("loadtaskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfo task = new TaskInfo(obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public List<TaskInfo> UpdateTaskData(int nLimit, int nType, int nMaxTaskId) {
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskdata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("nMaxTaskId", nMaxTaskId + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseUpdateTaskData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfo> parseUpdateTaskData(String json) {
		List<TaskInfo> tasks = new ArrayList<TaskInfo>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("updatetaskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfo task = new TaskInfo(obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public int UploadLargeImage(String strTaskId, String strTaskType,
			String strIcon1, String strIcon2, String strIcon3, String strIcon4,
			String strIcon5, String strIcon6) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/uploadlargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		paramas.add(new BasicNameValuePair("strIcon1", strIcon1));
		paramas.add(new BasicNameValuePair("strIcon2", strIcon2));
		paramas.add(new BasicNameValuePair("strIcon3", strIcon3));
		paramas.add(new BasicNameValuePair("strIcon4", strIcon4));
		paramas.add(new BasicNameValuePair("strIcon5", strIcon5));
		paramas.add(new BasicNameValuePair("strIcon6", strIcon6));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUploadLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUploadLargeImage(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nuploadlargeimageret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UploadTaskFinishTypeForLargeImage(String strTaskId,
			String strTaskType, String strBase64Image) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath
				+ "renpin/uploadtaskfinishtypeforlargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		paramas.add(new BasicNameValuePair("strBase64Image", strBase64Image));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUploadTaskFinishTypeForLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUploadTaskFinishTypeForLargeImage(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nuploadfinishimageret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UploadTaskVerifiTypeForLargeImage(String strTaskId,
			String strTaskType, String strBase64Image) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath
				+ "renpin/uploadtaskverifitypeforlargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		paramas.add(new BasicNameValuePair("strBase64Image", strBase64Image));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUploadTaskVerifiTypeForLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUploadTaskVerifiTypeForLargeImage(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nuploadverifiimageret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public TaskIcon GetTaskSmallIcon(String strTaskId, String strTaskType) {
		TaskIcon taskicon = null;
		// 创建请求的url
		String url = strBasePath + "renpin/gettasksmallicon.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					taskicon = parseGetTaskSmallIcon(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taskicon;
	}

	// 解析json的单个对象
	private TaskIcon parseGetTaskSmallIcon(String json) {
		TaskIcon taskicon = null;
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("mtaskicon");
			taskicon = new TaskIcon(obj.getString("strIcon1"),
					obj.getString("strIcon2"), obj.getString("strIcon3"),
					obj.getString("strIcon4"), obj.getString("strIcon5"),
					obj.getString("strIcon6"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return taskicon;
	}

	@Override
	public String GetTaskLargeIcon(String strTaskId, String strTaskType,
			int nIconId) {
		String strIcon = "";
		// 创建请求的url
		String url = strBasePath + "renpin/gettasklargeicon.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		paramas.add(new BasicNameValuePair("nIconId", nIconId + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strIcon = parseGetTaskLargeIcon(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	// 解析json的单个对象
	private String parseGetTaskLargeIcon(String json) {
		String strIcon = "";
		try {
			strIcon = new JSONObject(json).getString("strtaskicon");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	@Override
	public String GetTaskFinishLargeImage(String strTaskId, String strTaskType) {
		String strIcon = "";
		// 创建请求的url
		String url = strBasePath + "renpin/gettaskfinishlargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strIcon = parseGetTaskFinishLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	// 解析json的单个对象
	private String parseGetTaskFinishLargeImage(String json) {
		String strIcon = "";
		try {
			strIcon = new JSONObject(json).getString("strtaskfinishlargeimage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	@Override
	public String GetTaskVerifiLargeImage(String strTaskId, String strTaskType) {
		String strIcon = "";
		// 创建请求的url
		String url = strBasePath + "renpin/gettaskverifilargeimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strIcon = parseGetTaskVerifiLargeImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	// 解析json的单个对象
	private String parseGetTaskVerifiLargeImage(String json) {
		String strIcon = "";
		try {
			strIcon = new JSONObject(json).getString("strtaskverifilargeimage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strIcon;
	}

	@Override
	public int ResetTaskStatue(String strTaskId, String strTaskType) {
		int nRet1 = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/resettaskstatue.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet1 = parseResetTaskStatue(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet1;
	}

	// 解析json的单个对象
	private int parseResetTaskStatue(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nretsettaskret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int RecordCreditAndCharmForTask(String strTaskId,
			String strTaskType, int nCredit, int nCharm) {
		int nRet1 = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/recordcreditandcharmfortask.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		paramas.add(new BasicNameValuePair("nCredit", nCredit + ""));
		paramas.add(new BasicNameValuePair("nCharm", nCharm + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet1 = parseRecordCreditAndCharmForTask(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet1;
	}

	// 解析json的单个对象
	private int parseRecordCreditAndCharmForTask(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nrecordvalueret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public CreditAndCharmForTask GetCreditAndCharmForTask(String strTaskId,
			String strTaskType) {
		CreditAndCharmForTask taskvalue = null;
		// 创建请求的url
		String url = strBasePath + "renpin/getcreditandcharmfortask.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					taskvalue = parseGetCreditAndCharmForTask(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taskvalue;
	}

	// 解析json的单个对象
	private CreditAndCharmForTask parseGetCreditAndCharmForTask(String json) {
		CreditAndCharmForTask taskvalue = null;
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("creditandcharm");
			taskvalue = new CreditAndCharmForTask(obj.getInt("nCreditValue"),
					obj.getInt("nCharmValue"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return taskvalue;
	}

	@Override
	public int JudgeCustomerValidity(String strName, String strPassword) {
		int nRet = 0;// 为0表示失败,1表示新账号,2表示账号重名
		// 创建请求的url
		String url = strBasePath + "renpin/judgecustomervalidity.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("PersonName", strName));
		paramas.add(new BasicNameValuePair("Password", strPassword));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseJudgeCustomerValidity(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseJudgeCustomerValidity(String json) {
		int nRet = 0;// 为0表示失败,1表示新账号,2表示账号重名
		try {
			nRet = new JSONObject(json).getInt("nJudgeValidityRet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int GetVersionCode() {
		int nRet = -1;
		// http://59.60.9.202:8000/Apk/download?fileName=RenPinCustomer
		// 创建请求的url
		String url = "http://localhost:8080/Apk/getversioncode.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			// httpPost.setEntity(new UrlEncodedFormEntity(paramas,
			// HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRet = parseGetVersionCode(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	// 解析json的单个对象
	private int parseGetVersionCode(String json) {
		int nRet = -1;
		try {
			nRet = new JSONObject(json).getInt("nversioncode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public String GetVersionName() {
		String strVersionName = "";
		// 创建请求的url
		String url = "http://localhost:8080/Apk/getversionname.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			// httpPost.setEntity(new UrlEncodedFormEntity(paramas,
			// HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strVersionName = parseGetVersionName(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strVersionName;
	}

	// 解析json的单个对象
	private String parseGetVersionName(String json) {
		String strVersionName = "";
		try {
			strVersionName = new JSONObject(json).getString("strversionname");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strVersionName;
	}

	@Override
	public int RegisterCustomerDetail(String strBase64CustomerIcon,
			String strName, String strPassword, String strCreditValue,
			String strCharmValue, String strNickName, String strSex,
			String strLargeImage) {
		int nRetType = 1;// 为0表示成功,1表示失败,2表示账号重名
		// 创建请求的url
		String url = strBasePath + "renpin/registercustomerdetail.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("PersonIcon", strBase64CustomerIcon));
		paramas.add(new BasicNameValuePair("PersonName", strName));
		paramas.add(new BasicNameValuePair("Password", strPassword));
		paramas.add(new BasicNameValuePair("CreditValue", strCreditValue));
		paramas.add(new BasicNameValuePair("CharmValue", strCharmValue));
		paramas.add(new BasicNameValuePair("strNickName", strNickName));
		paramas.add(new BasicNameValuePair("strSex", strSex));
		paramas.add(new BasicNameValuePair("strLargeImage", strLargeImage));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseRegisterCustomerDetail(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseRegisterCustomerDetail(String json) {
		int nTicketNum = 1;// 为0表示成功,1表示失败,2表示账号重名
		try {
			nTicketNum = new JSONObject(json)
					.getInt("bregeditretforlargeimage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public int AddTaskInfoForLimit(
			int nTaskType,// 任务类型,0表示求助,1表示分享
			String strRegionName,// 任务执行区域
			String strTaskTitle,// 任务标题
			String strTaskDeatial,// 任务描述
			String strTaskAnnounceTime,// 任务发布时间
			String strTaskImpleTime,// 任务完成时间
			String strTaskAccountName,// 任务发布人名称(昵称)
			String strImplementAccountName,// 任务执行人名称(昵称)
			String strTaskAccountTrueName, // 任务发布人名称
			String strImplementAccountTrueName,// 任务执行人名称
			double dLongitude,// 任务执行区域的经度
			double dLatidude,// 任务执行区域的纬度
			String strTaskAccountIcon,// 任务发布者的图标
			String strTimeLimit,
			String strIcon1,// 第一张压缩图片(下面类似)
			String strIcon2, String strIcon3, String strIcon4, String strIcon5,
			String strIcon6, int nCreditValue, int nCharmValue) {
		int nRetType = 1;// 为0表示成功,1表示失败
		// 创建请求的url
		String url = strBasePath + "renpin/addtaskinfoforlimit.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nTaskType", nTaskType + ""));
		paramas.add(new BasicNameValuePair("strRegionName", strRegionName));
		paramas.add(new BasicNameValuePair("strTaskTitle", strTaskTitle));
		paramas.add(new BasicNameValuePair("strTaskDeatial", strTaskDeatial));
		paramas.add(new BasicNameValuePair("strTaskAnnounceTime",
				strTaskAnnounceTime));
		paramas.add(new BasicNameValuePair("strTaskImpleTime", strTaskImpleTime));
		paramas.add(new BasicNameValuePair("strTaskAccountName",
				strTaskAccountName));
		paramas.add(new BasicNameValuePair("strImplementAccountName",
				strImplementAccountName));
		paramas.add(new BasicNameValuePair("strTaskAccountTrueName",
				strTaskAccountTrueName));
		paramas.add(new BasicNameValuePair("strImplementAccountTrueName",
				strImplementAccountTrueName));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatidude", dLatidude + ""));
		paramas.add(new BasicNameValuePair("strTaskAccountIcon",
				strTaskAccountIcon));
		paramas.add(new BasicNameValuePair("strTimeLimit", strTimeLimit));
		paramas.add(new BasicNameValuePair("strIcon1", strIcon1));
		paramas.add(new BasicNameValuePair("strIcon2", strIcon2));
		paramas.add(new BasicNameValuePair("strIcon3", strIcon3));
		paramas.add(new BasicNameValuePair("strIcon4", strIcon4));
		paramas.add(new BasicNameValuePair("strIcon5", strIcon5));
		paramas.add(new BasicNameValuePair("strIcon6", strIcon6));
		paramas.add(new BasicNameValuePair("nCreditValue", nCreditValue + ""));
		paramas.add(new BasicNameValuePair("nCharmValue", nCharmValue + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseAddTaskInfoForLimit(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseAddTaskInfoForLimit(String json) {
		int nTicketNum = 1;// 为0表示成功,1表示失败
		try {
			nTicketNum = new JSONObject(json).getInt("baddtaskforlimitret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public List<TaskInfoDetail> GetTaskInfoForLimit(int nType) {
		// 创建请求的url
		String url = strBasePath + "renpin/gettaskinfoforlimit.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskType", nType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseGetTaskInfoForLimit(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseGetTaskInfoForLimit(String json) {
		List<TaskInfoDetail> tasks = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("taskdetailinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail task = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public List<TaskInfoDetail> LoadTaskDataForLimit(int nLimit, int nType,
			int nMaxTaskId) {
		// 创建请求的url
		String url = strBasePath + "renpin/loadtaskdataforlimit.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("nMaxTaskId", nMaxTaskId + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseLoadTaskDataForLimit(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseLoadTaskDataForLimit(String json) {
		List<TaskInfoDetail> tasks = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("loaddetailtaskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail task = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public List<TaskInfoDetail> UpdateTaskDataForLimit(int nLimit, int nType,
			int nMaxTaskId) {
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskdataforlimit.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("nMaxTaskId", nMaxTaskId + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseUpdateTaskDataForLimit(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseUpdateTaskDataForLimit(String json) {
		List<TaskInfoDetail> tasks = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("updatedetailtaskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail task = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public int ChangePersonImage(String strPersonName, String strSmallImage,
			String strLargeImage) {
		int nRetType = 0;// 为0表示失败,1表示失败,2表示账号重名
		// 创建请求的url
		String url = strBasePath + "renpin/changepersonimage.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strSmallImage", strSmallImage));
		paramas.add(new BasicNameValuePair("strLargeImage", strLargeImage));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseChangePersonImage(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseChangePersonImage(String json) {
		int nTicketNum = 1;// 为0表示失败,1表示成功,2表示账号重名
		try {
			nTicketNum = new JSONObject(json).getInt("nchangepersonimageret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public List<DistanceDetail> getHelpNearData(Double longitude,
			Double latitude, Double dist, String strDistrictName) {

		List<DistanceDetail> distancelist = new ArrayList<DistanceDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/gethelpneardata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("longitude", longitude + ""));
		paramas.add(new BasicNameValuePair("latitude", latitude + ""));
		paramas.add(new BasicNameValuePair("dist", dist + ""));
		paramas.add(new BasicNameValuePair("name", strDistrictName));

		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					distancelist = parsegetHelpNearData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distancelist;
	}

	// 解析json的单个对象
	private List<DistanceDetail> parsegetHelpNearData(String json) {
		List<DistanceDetail> datas = new ArrayList<DistanceDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("distancehelpdata");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				DistanceDetail task = new DistanceDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nDistance"), obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				datas.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public List<DistanceDetail> getShareNearData(Double longitude,
			Double latitude, Double dist, String strDistrictName) {

		List<DistanceDetail> distancelist = new ArrayList<DistanceDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/getshareneardata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("longitude", longitude + ""));
		paramas.add(new BasicNameValuePair("latitude", latitude + ""));
		paramas.add(new BasicNameValuePair("dist", dist + ""));
		paramas.add(new BasicNameValuePair("name", strDistrictName));

		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					distancelist = parsegetShareNearData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distancelist;
	}

	// 解析json的单个对象
	private List<DistanceDetail> parsegetShareNearData(String json) {
		List<DistanceDetail> datas = new ArrayList<DistanceDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("distancesharedata");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				DistanceDetail task = new DistanceDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nDistance"), obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				datas.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public List<DistanceDetail> LoadHelpNearData(Double longitude,
			Double latitude, Double dist, String strDistrictName, String strTime) {
		List<DistanceDetail> distancelist = new ArrayList<DistanceDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/loadhelpneardata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("longitude", longitude + ""));
		paramas.add(new BasicNameValuePair("latitude", latitude + ""));
		paramas.add(new BasicNameValuePair("dist", dist + ""));
		paramas.add(new BasicNameValuePair("name", strDistrictName));
		paramas.add(new BasicNameValuePair("time", strTime));

		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					distancelist = parseLoadHelpNearData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distancelist;
	}

	// 解析json的单个对象
	private List<DistanceDetail> parseLoadHelpNearData(String json) {
		List<DistanceDetail> datas = new ArrayList<DistanceDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("distancehelpdata1");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				DistanceDetail task = new DistanceDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nDistance"), obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				datas.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public List<DistanceDetail> LoadShareNearData(Double longitude,
			Double latitude, Double dist, String strDistrictName, String strTime) {
		List<DistanceDetail> distancelist = new ArrayList<DistanceDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/loadshareneardata.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("longitude", longitude + ""));
		paramas.add(new BasicNameValuePair("latitude", latitude + ""));
		paramas.add(new BasicNameValuePair("dist", dist + ""));
		paramas.add(new BasicNameValuePair("name", strDistrictName));
		paramas.add(new BasicNameValuePair("time", strTime));

		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					distancelist = parseLoadShareNearData(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return distancelist;
	}

	// 解析json的单个对象
	private List<DistanceDetail> parseLoadShareNearData(String json) {
		List<DistanceDetail> datas = new ArrayList<DistanceDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("distancesharedata1");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				DistanceDetail task = new DistanceDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nDistance"), obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				datas.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public List<TaskInfo> GetMsgInfoNumNew(String strAnnounceName) {
		List<TaskInfo> DynamicNews = new ArrayList<TaskInfo>();
		// 创建请求的url
		String url = strBasePath + "renpin/getmsgnumnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					DynamicNews = parseGetMsgInfoNumNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	// 解析json的单个对象
	private List<TaskInfo> parseGetMsgInfoNumNew(String json) {
		List<TaskInfo> DynamicNews = new ArrayList<TaskInfo>();
		try {
			JSONArray array = new JSONObject(json).getJSONArray("taskinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfo TaskNew = new TaskInfo(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"));
				DynamicNews.add(TaskNew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	@Override
	public int UpdateCommentType(String strAnnounceName, int nValue,
			String strTaskId, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updatecommenttype.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("nValue", nValue + ""));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUpdateCommentType(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUpdateCommentType(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("commentstatusret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int SendCommentContentNew(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strCommentContent, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentcontentnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strCommentContent",
				strCommentContent));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendCommentContentNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendCommentContentNew(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendcommentnewret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int SendCommentImageNew(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strSmallImage, String strLargeImage, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentimagenew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strSmallImage", strSmallImage));
		paramas.add(new BasicNameValuePair("strLargeImage", strLargeImage));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendCommentImageNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendCommentImageNew(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendcommentimagenewret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public UpdateData GetUpdateSignalNew(String strCurrentAccountName) {
		UpdateData update = null;
		// 创建请求的url
		String url = strBasePath + "renpin/getupdatasignlnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strCurrentAccountName",
				strCurrentAccountName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					update = parseGetUpdateSignalNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == update) {
			update = new UpdateData(-1, "");
		}
		return update;
	}

	// 解析json的单个对象
	private UpdateData parseGetUpdateSignalNew(String json) {
		UpdateData update = new UpdateData(-1, "");
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("nupdatevalue");
			update.setnUpdateSignal(obj.getInt("nUpdateSignal"));
			update.setstrUpdateDescribe(obj.getString("strUpdateDescribe"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return update;
	}

	@Override
	public int PraiseToTaskOrShare(String strTaskId, String strPersonName,
			String strReceivePersonName, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/praisetotaskorshare.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		paramas.add(new BasicNameValuePair("strReceivePersonName",
				strReceivePersonName));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parsePraiseToTaskOrShare(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parsePraiseToTaskOrShare(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nPraiseRet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int AddBrowseTimes(String strTaskId, String strType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/addbrowsetimes.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseAddBrowseTimes(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseAddBrowseTimes(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nBrowseRet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<TaskInfoDetail> GetMsgInfoNumNewDetail(String strAnnounceName) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/getmsgnumnewdetail.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					DynamicNews = parseGetMsgInfoNumNewDetail(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseGetMsgInfoNumNewDetail(String json) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("taskinfodetails");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail TaskNew = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				DynamicNews.add(TaskNew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	@Override
	public int AddTaskInfoForLimitNew(
			int nTaskType,// 任务类型,0表示求助,1表示分享
			String strRegionName,// 任务执行区域
			String strTaskTitle,// 任务标题
			String strTaskDeatial,// 任务描述
			String strTaskAnnounceTime,// 任务发布时间
			String strTaskImpleTime,// 任务完成时间
			String strTaskAccountName,// 任务发布人名称(昵称)
			String strImplementAccountName,// 任务执行人名称(昵称)
			String strTaskAccountTrueName, // 任务发布人名称
			String strImplementAccountTrueName,// 任务执行人名称
			double dLongitude,// 任务执行区域的经度
			double dLatidude,// 任务执行区域的纬度
			String strTaskAccountIcon,// 任务发布者的图标
			String strTimeLimit,
			String strIcon1,// 第一张压缩图片(下面类似)
			String strIcon2, String strIcon3, String strIcon4, String strIcon5,
			String strIcon6, int nCreditValue, int nCharmValue,
			String strDistrictName, String strCityName) {
		int nRetType = 1;// 为0表示成功,1表示失败
		// 创建请求的url
		String url = strBasePath + "renpin/addtaskinfoforlimitnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nTaskType", nTaskType + ""));
		paramas.add(new BasicNameValuePair("strRegionName", strRegionName));
		paramas.add(new BasicNameValuePair("strTaskTitle", strTaskTitle));
		paramas.add(new BasicNameValuePair("strTaskDeatial", strTaskDeatial));
		paramas.add(new BasicNameValuePair("strTaskAnnounceTime",
				strTaskAnnounceTime));
		paramas.add(new BasicNameValuePair("strTaskImpleTime", strTaskImpleTime));
		paramas.add(new BasicNameValuePair("strTaskAccountName",
				strTaskAccountName));
		paramas.add(new BasicNameValuePair("strImplementAccountName",
				strImplementAccountName));
		paramas.add(new BasicNameValuePair("strTaskAccountTrueName",
				strTaskAccountTrueName));
		paramas.add(new BasicNameValuePair("strImplementAccountTrueName",
				strImplementAccountTrueName));
		paramas.add(new BasicNameValuePair("dLongitude", dLongitude + ""));
		paramas.add(new BasicNameValuePair("dLatidude", dLatidude + ""));
		paramas.add(new BasicNameValuePair("strTaskAccountIcon",
				strTaskAccountIcon));
		paramas.add(new BasicNameValuePair("strTimeLimit", strTimeLimit));
		paramas.add(new BasicNameValuePair("strIcon1", strIcon1));
		paramas.add(new BasicNameValuePair("strIcon2", strIcon2));
		paramas.add(new BasicNameValuePair("strIcon3", strIcon3));
		paramas.add(new BasicNameValuePair("strIcon4", strIcon4));
		paramas.add(new BasicNameValuePair("strIcon5", strIcon5));
		paramas.add(new BasicNameValuePair("strIcon6", strIcon6));
		paramas.add(new BasicNameValuePair("nCreditValue", nCreditValue + ""));
		paramas.add(new BasicNameValuePair("nCharmValue", nCharmValue + ""));
		paramas.add(new BasicNameValuePair("strDistrictName", strDistrictName));
		paramas.add(new BasicNameValuePair("strCityName", strCityName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetType = parseAddTaskInfoForLimitNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetType;
	}

	// 解析json的单个对象
	private int parseAddTaskInfoForLimitNew(String json) {
		int nTicketNum = 1;// 为0表示成功,1表示失败
		try {
			nTicketNum = new JSONObject(json).getInt("baddtaskforlimitnewret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nTicketNum;
	}

	@Override
	public int UpdateItemDetailText(String strTaskId, String strType,
			String strText) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updateitemdetailtext.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strText", strText));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUpdateItemDetailText(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUpdateItemDetailText(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nupdateitemdetailtextRet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public int UpdateItemTitleText(String strTaskId, String strType,
			String strText) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/updateitemtitletext.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strText", strText));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseUpdateItemTitleText(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseUpdateItemTitleText(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nupdateitemtitletextRet");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<CommentInfo> GetCommentsForTaskNew(String strTaskId,
			String strType, String strCustomerName) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		// 创建请求的url
		String url = strBasePath + "renpin/getnewcomments.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCustomerName", strCustomerName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					Comments = parseGetCommentsForTaskNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	// 解析json的单个对象
	private List<CommentInfo> parseGetCommentsForTaskNew(String json) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("newcommentinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				CommentInfo comment = new CommentInfo(
						obj.getString("strCommentPersonImage"),
						obj.getString("strCommentPersonName"),
						obj.getString("strCommentReceivePersonName"),
						obj.getString("strCommentTime"),
						obj.getString("strCommentContent"),
						obj.getString("strSmallImage"),
						obj.getInt("nCommentIndex"),
						obj.getString("strCommentPersonTrueName"),
						obj.getString("strCommentReceivePersonTrueName"));
				Comments.add(comment);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	@Override
	public List<CommentInfo> GetCommentsForTaskNew1(String strTaskId,
			String strType, String strCustomerName, String strTime) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		// 创建请求的url
		String url = strBasePath + "renpin/getnewcomments1.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("TaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCustomerName", strCustomerName));
		paramas.add(new BasicNameValuePair("strTime", strTime));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					Comments = parseGetCommentsForTaskNew1(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	// 解析json的单个对象
	private List<CommentInfo> parseGetCommentsForTaskNew1(String json) {
		List<CommentInfo> Comments = new ArrayList<CommentInfo>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("newcommentinfos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				CommentInfo comment = new CommentInfo(
						obj.getString("strCommentPersonImage"),
						obj.getString("strCommentPersonName"),
						obj.getString("strCommentReceivePersonName"),
						obj.getString("strCommentTime"),
						obj.getString("strCommentContent"),
						obj.getString("strSmallImage"),
						obj.getInt("nCommentIndex"),
						obj.getString("strCommentPersonTrueName"),
						obj.getString("strCommentReceivePersonTrueName"));
				Comments.add(comment);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Comments;
	}

	@Override
	public int SendCommentContentNewSecret(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strCommentContent, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName, int nSecretType) {
		int nTypeRet = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentcontentnewsecret.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strCommentContent",
				strCommentContent));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		paramas.add(new BasicNameValuePair("nSecretType", nSecretType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nTypeRet = parseSendCommentContentNewSecret(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTypeRet;
	}

	// 解析json的单个对象
	private int parseSendCommentContentNewSecret(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("nsendcommentnewsecretret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public String SendCommentImageNewTime(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strSmallImage, String strLargeImage, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName) {
		String strTime = "";
		// 创建请求的url
		String url = strBasePath + "renpin/sendcommentimagenewtime.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strSmallImage", strSmallImage));
		paramas.add(new BasicNameValuePair("strLargeImage", strLargeImage));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strTime = parseSendCommentImageNewTime(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	// 解析json的单个对象
	private String parseSendCommentImageNewTime(String json) {
		String strTime = "";
		try {
			strTime = new JSONObject(json)
					.getString("strsendcommentimagenewret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	@Override
	public String SendCommentContentNewSecretTime(String strTaskId,
			String strCommentPersonName, String strReceiveCommentPersonName,
			String strCommentContent, String strType,
			String strCommentPersonNickName,
			String strReceiveCommentPersonNickName, int nSecretType) {
		String strTime = "";
		// 创建请求的url
		String url = strBasePath
				+ "renpin/sendcommentcontentnewsecrettime.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strCommentPersonName",
				strCommentPersonName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonName",
				strReceiveCommentPersonName));
		paramas.add(new BasicNameValuePair("strCommentContent",
				strCommentContent));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strCommentPersonNickName",
				strCommentPersonNickName));
		paramas.add(new BasicNameValuePair("strReceiveCommentPersonNickName",
				strReceiveCommentPersonNickName));
		paramas.add(new BasicNameValuePair("nSecretType", nSecretType + ""));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strTime = parseSendCommentContentNewSecretTime(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	// 解析json的单个对象
	private String parseSendCommentContentNewSecretTime(String json) {
		String strTime = "";
		try {
			strTime = new JSONObject(json)
					.getString("strsendcommentnewsecretret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strTime;
	}

	@Override
	public String GetFirstSmallIcon(String strTaskId, String strTaskType) {
		String strImage = "";
		// 创建请求的url
		String url = strBasePath + "renpin/getfirstsmallicon.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					strImage = parseGetFirstSmallIcon(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strImage;
	}

	// 解析json的单个对象
	private String parseGetFirstSmallIcon(String json) {
		String strImage = "";
		try {
			strImage = new JSONObject(json).getString("strfirstimage");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strImage;
	}

	@Override
	public TaskIcon GetOtherSmallIcon(String strTaskId, String strTaskType) {
		TaskIcon taskicon = null;
		// 创建请求的url
		String url = strBasePath + "renpin/getothersmallicon.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strTaskType", strTaskType));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					taskicon = parseGetOtherSmallIcon(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taskicon;
	}

	// 解析json的单个对象
	private TaskIcon parseGetOtherSmallIcon(String json) {
		TaskIcon taskicon = null;
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("mothersmallicon");
			taskicon = new TaskIcon(obj.getString("strIcon1"),
					obj.getString("strIcon2"), obj.getString("strIcon3"),
					obj.getString("strIcon4"), obj.getString("strIcon5"),
					obj.getString("strIcon6"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return taskicon;
	}

	@Override
	public List<TaskInfoDetail> GetMsgInfoNumNewDetail1(String strAnnounceName) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/getmsgnumnewdetail1.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					DynamicNews = parseGetMsgInfoNumNewDetail1(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseGetMsgInfoNumNewDetail1(String json) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("taskinfodetails");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail TaskNew = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				DynamicNews.add(TaskNew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	@Override
	public UpdateData GetUpdateSignalNew1(String strCurrentAccountName) {
		UpdateData update = null;
		// 创建请求的url
		String url = strBasePath + "renpin/getupdatasignlnew1.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strCurrentAccountName",
				strCurrentAccountName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					update = parseGetUpdateSignalNew1(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == update) {
			update = new UpdateData(-1, "");
		}
		return update;
	}

	// 解析json的单个对象
	private UpdateData parseGetUpdateSignalNew1(String json) {
		UpdateData update = new UpdateData(-1, "");
		JSONObject obj = null;
		try {
			obj = new JSONObject(json).getJSONObject("nupdatevalue");
			update.setnUpdateSignal(obj.getInt("nUpdateSignal"));
			update.setstrUpdateDescribe(obj.getString("strUpdateDescribe"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return update;
	}

	@Override
	public List<TaskInfoDetail> GetMsgInfoNumNewDetail2(String strAnnounceName,
			String strID) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		// 创建请求的url
		String url = strBasePath + "renpin/getmsgnumnewdetail2.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("AnnounceName", strAnnounceName));
		paramas.add(new BasicNameValuePair("strID", strID));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					DynamicNews = parseGetMsgInfoNumNewDetail2(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseGetMsgInfoNumNewDetail2(String json) {
		List<TaskInfoDetail> DynamicNews = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("taskinfodetails");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail TaskNew = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				DynamicNews.add(TaskNew);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return DynamicNews;
	}

	@Override
	public int DeleteMineDynamicInfo(String strTaskId, String strType,
			String strPersonName) {
		int nRetValue = 0;
		// 创建请求的url
		String url = strBasePath + "renpin/deleteminedynamicinfo.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("strTaskId", strTaskId));
		paramas.add(new BasicNameValuePair("strType", strType));
		paramas.add(new BasicNameValuePair("strPersonName", strPersonName));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			int nRet = httpResponse.getStatusLine().getStatusCode();
			if (nRet == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					nRetValue = parseDeleteMineDynamicInfo(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRetValue;
	}

	// 解析json的单个对象
	private int parseDeleteMineDynamicInfo(String json) {
		int nRet = 0;
		try {
			nRet = new JSONObject(json).getInt("deletedynamicret");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return nRet;
	}

	@Override
	public List<TaskInfoDetail> UpdateTaskDataForLimitNew(int nLimit,
			int nType, String strMaxTime) {
		// 创建请求的url
		String url = strBasePath + "renpin/updatetaskdataforlimitnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("strMaxTime", strMaxTime));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseUpdateTaskDataForLimitNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseUpdateTaskDataForLimitNew(String json) {
		List<TaskInfoDetail> tasks = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("updatedetailtaskinfosnew");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail task = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public List<TaskInfoDetail> LoadTaskDataForLimitNew(int nLimit, int nType,
			String strMaxTime) {
		// 创建请求的url
		String url = strBasePath + "renpin/loadtaskdataforlimitnew.action";
		// 使用HttpPost发送请求
		HttpPost httpPost = new HttpPost(url);
		// 使用NameValuePaira保存请求中所需要传入的参数
		List<NameValuePair> paramas = new ArrayList<NameValuePair>();
		paramas.add(new BasicNameValuePair("nLimit", nLimit + ""));
		paramas.add(new BasicNameValuePair("nType", nType + ""));
		paramas.add(new BasicNameValuePair("strMaxTime", strMaxTime));
		try {

			HttpResponse httpResponse;
			// 将NameValuePair放入HttpPost请求体
			httpPost.setEntity(new UrlEncodedFormEntity(paramas, HTTP.UTF_8));
			// 执行HttpPost请求
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// 如果响应码为200则表示获取成功，否则为发生错误
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					// 获取服务器响应的json字符串
					String json = EntityUtils.toString(entity, "UTF-8");
					return parseLoadTaskDataForLimitNew(json);
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 解析json的单个对象
	private List<TaskInfoDetail> parseLoadTaskDataForLimitNew(String json) {
		List<TaskInfoDetail> tasks = new ArrayList<TaskInfoDetail>();
		try {
			JSONArray array = new JSONObject(json)
					.getJSONArray("loaddetailtaskinfosnew");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				TaskInfoDetail task = new TaskInfoDetail(
						obj.getString("mstrTaskRegion"),
						obj.getString("mTaskAskPersonIcon"),
						obj.getString("mPersonName"),
						obj.getString("mTaskTitle"), obj.getString("mstrId"),
						obj.getDouble("mdLongitude"),
						obj.getDouble("mdLatidude"),
						obj.getString("mTaskAnnounceTime"),
						obj.getString("mTimeLimit"),
						obj.getString("mTaskDetail"),
						obj.getString("mRunSeconds"),
						obj.getString("mTaskImplementName"),
						obj.getInt("mnValiableStatus"),
						obj.getInt("mnImplementStatus"),
						obj.getInt("mnTaskType"),
						obj.getInt("nTaskSelectType"),
						obj.getInt("nTaskFinishType"),
						obj.getInt("nTaskVerifiType"),
						obj.getInt("nTaskAnnounceCommentType"),
						obj.getInt("nTaskImplementCommentType"),
						obj.getInt("nDynamicNewsNum"),
						obj.getString("strTaskAccountCommentContent"),
						obj.getString("strTaskAccountImage"),
						obj.getString("strTaskImplementCommentContent"),
						obj.getString("strTaskImplementImage"),
						obj.getString("strTaskPersonTrueName"),
						obj.getString("strTaskImplementTrueName"),
						obj.getInt("nCreditValue"), obj.getInt("nCharmValue"),
						obj.getInt("nTaskCharmValue"),
						obj.getInt("nCommentRecordNum"),
						obj.getInt("nBrowseTimes"));
				tasks.add(task);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tasks;
	}
}
