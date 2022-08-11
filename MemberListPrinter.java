package com.gyuone.service;

import java.util.Collection;

import com.gyuone.dao.MemberDao;
import com.gyuone.model.Member;

public class MemberListPrinter {
	private MemberDao memberDao;
	private MemberPrinter printer;
	public MemberListPrinter(MemberDao memberDao, MemberPrinter printer) {
		this.memberDao = memberDao;
		this.printer = printer;
	}
	
	public void printMemberInfo(String email) {
		Member member = memberDao.selectByEmail(email);
		if(member == null) {
			System.out.println("데이터 없음\n");
			return;
		}
		printer.print(member);
		System.out.println();
	}


	public void printAll() {
		Collection<Member> members = memberDao.selectAll();
		members.forEach(m -> printer.print(m)); 
	}
	

	
}
