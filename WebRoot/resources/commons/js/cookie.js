	/**
	*  func  getCookie()  ��ȡ����cookie��ֵ
	*  pram  cookieName  cookie������
	**/
	function getCookie(cookieName){
		var cookieObj={},
		cookieSplit=[],
		// �Էֺţ�;������
		cookieArr=document.cookie.split(";");
		for(var i=0,len=cookieArr.length;i<len;i++)
		if(cookieArr[i]) {
			// �ԵȺţ�=������
			cookieSplit=cookieArr[i].split("=");
			// Trim() ���Զ���ĺ���������ɾ���ַ������ߵĿո�
			cookieObj[$.trim(cookieSplit[0])]=$.trim(cookieSplit[1]);
		}
		return cookieObj[cookieName];
	}