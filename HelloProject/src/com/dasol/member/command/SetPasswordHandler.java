package com.dasol.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dasol.auth.service.User;
import com.dasol.member.service.ChangePasswordService;
import com.dasol.member.service.MemberNotFoundException;
import com.dasol.mvc.command.CommandHandler;

public class SetPasswordHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
	private ChangePasswordService changePasswordService = new ChangePasswordService();
	
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getMethod().equalsIgnoreCase("GET")) {
			return processForm(request, response);
		} else if (request.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest request, HttpServletResponse response) {
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = (String)request.getSession().getAttribute("email");
		String newPwd = request.getParameter("newPwd");
		
		if(email == null) {
			User authUser = (User)request.getSession().getAttribute("authUser");
			email = authUser.getEmail();
		}
		
		try {
			changePasswordService.setPwd(email, newPwd);
			response.sendRedirect(request.getContextPath() + "myinfo.do");
			return null;
		} catch (MemberNotFoundException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

}