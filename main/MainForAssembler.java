package com.gyuone.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.DuplicateFormatFlagsException;

import com.gyuone.common.MemberNotFoundException;
import com.gyuone.common.WrongIdPasswordException;
import com.gyuone.model.RegisterRequest;
import com.gyuone.service.Assembler;
import com.gyuone.service.ChangePasswordService;
import com.gyuone.service.MemberRegisterService;

public class MainForAssembler {
	private static Assembler assembler = new Assembler();
	
	public static void main(String[] args) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.print("명령어 입력 : ");
			String command = reader.readLine();
			if(command.equalsIgnoreCase("exit")) {
				System.out.println("프로그램 종료");
				break;
			}
			if(command.startsWith("new ")) {
				processNewCommand(command.split(" "));
				continue;
			}else if(command.startsWith("change ")) {
				processChangeCommand(command.split(" "));
				continue;
			}
			printHelp();
		}
	}
	
	private static void printHelp() {
		System.out.println();
		System.out.println("잘못된 명령입니다. 아래 명령어 사용법을 확인하세요.");
		System.out.println("명령어 사용법:");
		System.out.println("new 이메일 이름 암호 암호확인");
		System.out.println("change 이메일 현재비번 변경비번");
		System.out.println();
	}
	
	private static void processNewCommand(String[] arg) {
		if(arg.length != 5) {
			printHelp();
			return;
		}
		MemberRegisterService regSvc = assembler.getMemberRegisterService();
		RegisterRequest req = new RegisterRequest();
		req.setEmail(arg[1]);
		req.setName(arg[2]);
		req.setPassword(arg[3]);
		req.setConfirmPassword(arg[4]);
		
		if(!req.isPasswordEqualToConfirmPassword()) {
			System.out.println("암호 확인이 맞지 않습니다.");
			System.out.println();
			return;
		}
		try {
			regSvc.register(req);
			System.out.println("등록 완료");
			System.out.println();
		} catch (DuplicateFormatFlagsException e) {
			// TODO: handle exception
			System.out.println("이미 존재하는 이메일입니다.");
			System.out.println();
		}
	}
	
	private static void processChangeCommand(String[] arg) {
		if(arg.length != 4) {
			printHelp();
			return;
		}
		ChangePasswordService changePwdSvc = assembler.getChangePasswordService();
		try {
			changePwdSvc.changePassword(arg[1], arg[2], arg[3]);
			System.out.println("암호 변경 완료");
		} catch (MemberNotFoundException e) {
			// TODO: handle exception
			System.out.println("존재하지 않는 이메일입니다.");
			System.out.println();
		} catch (WrongIdPasswordException e) {
			System.out.println("이메일과 암호 불일치");
			System.out.println();
		}
	}
}
