package com.gyuone.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gyuone.common.DuplicateMemberException;
import com.gyuone.common.MemberNotFoundException;
import com.gyuone.common.WrongIdPasswordException;
import com.gyuone.config.AppConf1;
import com.gyuone.config.AppConf2;
import com.gyuone.model.RegisterRequest;
import com.gyuone.service.ChangePasswordService;
import com.gyuone.service.MemberInfoPrinter;
import com.gyuone.service.MemberListPrinter;
import com.gyuone.service.MemberRegisterService;
import com.gyuone.service.VersionPrinter;

public class MainForSpring2 {
	private static ApplicationContext ctx = null;
	
	public static void main(String[] args) throws Exception{
		ctx = new AnnotationConfigApplicationContext(AppConf1.class, AppConf2.class);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.print("명령어 입력: ");
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
			}else if(command.startsWith("list")){
				processListCommand();
				continue;
			}else if(command.startsWith("info")) {
				processInfoCommand(command.split(" "));
				continue;
			}else if(command.startsWith("version")) {
				processVersionCommand();
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
		MemberRegisterService regSvc = ctx.getBean("memberRegSvc", MemberRegisterService.class);
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
		} catch (DuplicateMemberException e) {
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
		ChangePasswordService changePwdSvc = ctx.getBean("changePwdSvc", ChangePasswordService.class);
		try {
			changePwdSvc.changePassword(arg[1], arg[2], arg[3]);
			System.out.println("암호 변경 완료");
			System.out.println();
		} catch (MemberNotFoundException e) {
			// TODO: handle exception
			System.out.println("존재하지 않는 이메일입니다.");
			System.out.println();
		} catch (WrongIdPasswordException e) {
			System.out.println("이메일과 암호 불일치합니다.");
			System.out.println();
		}
	}
	
	private static void processListCommand() {
		MemberListPrinter listPrinter = ctx.getBean("listPrinter", MemberListPrinter.class); // "listPrinter", MemberListPrinter.class가 식별자, 타입
		listPrinter.printAll();
	}
	
	private static void processInfoCommand(String[] arg) {
		if(arg.length != 2) {
			printHelp();
			return;
		}
		MemberInfoPrinter infoPrinter = ctx.getBean("infoPrinter", MemberInfoPrinter.class);
		infoPrinter.printMemberInfo(arg[1]);
	}
	
	private static void processVersionCommand() {
		VersionPrinter versionPrinter = ctx.getBean("versionPrinter", VersionPrinter.class);
		versionPrinter.print();
	}
}