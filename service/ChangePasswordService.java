package com.gyuone.service;

import com.gyuone.common.MemberNotFoundException;
import com.gyuone.dao.MemberDao;
import com.gyuone.model.Member;

public class ChangePasswordService {
	private MemberDao memberDao;

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	public void changePassword(String email, String oldPwd, String newPwd) {
		Member member = memberDao.selectByEmail(email);
		if(member == null) {
			throw new MemberNotFoundException();
		}
		member.changePassword(oldPwd, newPwd);
		memberDao.update(member);
	}
}
