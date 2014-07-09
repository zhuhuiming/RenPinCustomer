package com.renpin.domin;

import java.io.Serializable;

public class DistanceDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistanceDetail() {
	}
	
	public DistanceDetail(String strRegion, String strIcon,
			String strPersonName, String strTitle, String strId,
			double dLongitude, double dLatidude, String strTaskAnnouceTime,
			String strTimeLimit, String strTaskDetail, String strRunTime,
			String strTaskImplementName, int nTimeStatus, int nImpleStatus,
			int nTaskType, int nTaskSelectType, int nTaskFinishType,
			int nTaskVerifiType, int nTaskAnnounceCommentType,
			int nTaskImplementCommentType, int nDynamicNewsNum,
			String strTaskAccountCommentContent, String strTaskAccountImage,
			String strTaskImplementCommentContent,
			String strTaskImplementImage, String strTaskPersonTrueName,
			String strTaskImplementTrueName, int nCreditValue, int nCharmValue,
			int nDistance,int nTaskCharmValue,int nCommentRecordNum,
			int nBrowseTimes) {
		mstrTaskRegion = strRegion;
		mTaskAskPersonIcon = strIcon;
		mPersonName = strPersonName;
		mTaskTitle = strTitle;
		mstrId = strId;
		mdLongitude = dLongitude;
		mdLatidude = dLatidude;
		mTaskAnnounceTime = strTaskAnnouceTime;
		mTimeLimit = strTimeLimit;
		mTaskDetail = strTaskDetail;
		mRunSeconds = strRunTime;
		mTaskImplementName = strTaskImplementName;
		mnValiableStatus = nTimeStatus;
		mnImplementStatus = nImpleStatus;
		mnTaskType = nTaskType;
		this.nTaskSelectType = nTaskSelectType;
		this.nTaskFinishType = nTaskFinishType;
		this.nTaskVerifiType = nTaskVerifiType;
		this.nTaskAnnounceCommentType = nTaskAnnounceCommentType;
		this.nTaskImplementCommentType = nTaskImplementCommentType;
		this.nDynamicNewsNum = nDynamicNewsNum;
		this.strTaskAccountCommentContent = strTaskAccountCommentContent;
		this.strTaskAccountImage = strTaskAccountImage;
		this.strTaskImplementCommentContent = strTaskImplementCommentContent;
		this.strTaskImplementImage = strTaskImplementImage;
		this.strTaskPersonTrueName = strTaskPersonTrueName;
		this.strTaskImplementTrueName = strTaskImplementTrueName;
		this.nCreditValue = nCreditValue;
		this.nCharmValue = nCharmValue;
		this.nDistance = nDistance;
		this.nTaskCharmValue = nTaskCharmValue;
		this.nCommentRecordNum = nCommentRecordNum;
		this.nBrowseTimes = nBrowseTimes;
	}

	public String getmstrTaskRegion() {
		return mstrTaskRegion;
	}

	public String getmTaskAskPersonIcon() {
		return mTaskAskPersonIcon;
	}

	public String getmTaskTitle() {
		return mTaskTitle;
	}

	public String getmPersonName() {
		return mPersonName;
	}

	public String getmstrId() {
		return mstrId;
	}

	public double getmdLongitude() {
		return mdLongitude;
	}

	public double getmdLatidude() {
		return mdLatidude;
	}

	public String getmTaskAnnounceTime() {
		return mTaskAnnounceTime;
	}

	public String getmTimeLimit() {
		return mTimeLimit;
	}

	public String getmTaskDetail() {
		return mTaskDetail;
	}

	public String getmRunSeconds() {
		return mRunSeconds;
	}

	public String getmTaskImplementName() {
		return mTaskImplementName;
	}

	public int getmnValiableStatus() {
		return mnValiableStatus;
	}

	public int getmnImplementStatus() {
		return mnImplementStatus;
	}

	public int getmnTaskType() {
		return mnTaskType;
	}

	public int getnTaskSelectType() {
		return nTaskSelectType;
	}

	public int getnTaskFinishType() {
		return nTaskFinishType;
	}

	public int getnTaskVerifiType() {
		return nTaskVerifiType;
	}

	public int getnTaskAnnounceCommentType() {
		return nTaskAnnounceCommentType;
	}

	public int getnTaskImplementCommentType() {
		return nTaskImplementCommentType;
	}

	public int getnDynamicNewsNum() {
		return nDynamicNewsNum;
	}

	public String getstrTaskAccountCommentContent() {
		return strTaskAccountCommentContent;
	}

	public String getstrTaskAccountImage() {
		return strTaskAccountImage;
	}

	public String getstrTaskImplementCommentContent() {
		return strTaskImplementCommentContent;
	}

	public String getstrTaskImplementImage() {
		return strTaskImplementImage;
	}

	public String getstrTaskPersonTrueName() {
		return strTaskPersonTrueName;
	}

	public String getstrTaskImplementTrueName() {
		return strTaskImplementTrueName;
	}

	public int getnCreditValue() {
		return nCreditValue;
	}

	public int getnCharmValue() {
		return nCharmValue;
	}
	
	public int getnDistance() {
		return nDistance;
	}
	
	public int getnTaskCharmValue() {
		return nTaskCharmValue;
	}
	
	public int getnCommentRecordNum() {
		return nCommentRecordNum;
	}
	
	public int getnBrowseTimes() {
		return nBrowseTimes;
	}

	public void setnTaskSelectType(int nValue) {
		nTaskSelectType = nValue;
	}

	public void setnTaskFinishType(int nValue) {
		nTaskFinishType = nValue;
	}

	public void setnTaskVerifiType(int nValue) {
		nTaskVerifiType = nValue;
	}

	public void setnTaskAnnounceCommentType(int nValue) {
		nTaskAnnounceCommentType = nValue;
	}

	public void setnTaskImplementCommentType(int nValue) {
		nTaskImplementCommentType = nValue;
	}

	public void setnDynamicNewsNum(int nValue) {
		nDynamicNewsNum = nValue;
	}
	
	public void setnDistance(int nValue) {
		nDistance = nValue;
	}

	public void setmTaskAskPersonIcon(String strIcon) {
		mTaskAskPersonIcon = strIcon;
	}
	
	public void setnTaskCharmValue(int nValue) {
		nTaskCharmValue = nValue;
	}
	
	public void setnCommentRecordNum(int nValue) {
		nCommentRecordNum = nValue;
	}
	
	public void setnBrowseTimes(int nValue) {
		nBrowseTimes = nValue;
	}
	
	public void setmTaskTitle(String strTitle) {
		mTaskTitle = strTitle;
	}
	
	public void setmTaskDetail(String strDetail) {
		mTaskDetail = strDetail;
	}
	
	// ���񷢲�����
	private String mstrTaskRegion;
	// �����������ͷ��
	private String mTaskAskPersonIcon;
	// ��������������(�ǳ�)
	private String mPersonName;
	// ��������ı���
	private String mTaskTitle;
	// ����id��
	private String mstrId;
	// ���񷢲�����ľ���
	private double mdLongitude;
	// ���񷢲������γ��
	private double mdLatidude;
	// ���񷢲�ʱ��
	private String mTaskAnnounceTime;
	// ʱ������
	private String mTimeLimit;
	// ������ϸ����
	private String mTaskDetail;
	// ����ӷ��������ھ���������
	private String mRunSeconds;
	// �������������(�ǳ�)
	private String mTaskImplementName;
	// �������Ч��״̬,1��ʾ������Ч��,2��ʾ�������
	private int mnValiableStatus;
	// �����ִ��״̬,1��ʾ�ȴ��������,2��ʾ����ִ����,3��ʾ��������ɵȴ���֤,4��ʾ������֤�ɹ�,5��ʾ������֤ʧ��
	private int mnImplementStatus;
	// ��������,1��ʾ����,2��ʾ����
	private int mnTaskType;
	// ��������������
	private String strTaskPersonTrueName;
	// �������������
	private String strTaskImplementTrueName;
	// ���¸���������¸÷�����Ҫ����Ʒֵ
	private int nCreditValue;
	// ���¸���������¸÷�����Ҫ����ֵ
	private int nCharmValue;
	//�����뵱ǰ�û��ľ���
	private int nDistance;
	//��ֵ
    private int nTaskCharmValue;
    //��������
    private int nCommentRecordNum;
    //�������
    private int nBrowseTimes;
	
	int nTaskSelectType;// ����������,1��ʾû���˽���,2��ʾ���˽���,3��ʾ�����Ѿ�����ͬʱ���񷢲����Ѿ��鿴���������
	int nTaskFinishType;// �������״̬,1��ʾ����û�����,2��ʾ�������,3��ʾ�������ͬʱ�������Ѳ鿴�����״̬
	int nTaskVerifiType;// ��������״̬,1��ʾû������,2��ʾ������,3��ʾ������ͬʱ����ִ�����Ѿ��鿴��״̬
	int nTaskAnnounceCommentType;// ��������������,1��ʾû����������,2��ʾ����������,3��ʾ����������ͬʱ����ִ�����Ѳ鿴
	int nTaskImplementCommentType;// ִ�����������ۣ�1��ʾû����������,2��ʾ����������,3��ʾ����������ͬʱ���񷢲����Ѳ鿴
	int nDynamicNewsNum;// ��̬��Ϣ����
	String strTaskAccountCommentContent;// ���񷢲��߸�ִ���ߵ�����
	String strTaskAccountImage;// ���񷢲��߸�ִ���ߵ�ͼƬ
	String strTaskImplementCommentContent;// ����ִ���߸������ߵ�����
	String strTaskImplementImage;// ����ִ���߸������ߵ�ͼƬ
}