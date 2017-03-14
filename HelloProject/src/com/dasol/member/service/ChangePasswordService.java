package com.dasol.member.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.dasol.jdbc.ConnectionProvider;
import com.dasol.jdbc.JdbcUtil;
import com.dasol.member.dao.MemberDAO;
import com.dasol.member.model.Member;

public class ChangePasswordService {
	MemberDAO memberDAO = new MemberDAO();
	
	public void changePwd(int memberId, String curPwd, String newPwd) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Member member = memberDAO.selectByMemberId(conn, memberId);

			if (member == null) {
				throw new MemberNotFoundException();
			}
			
			if(curPwd != null) {
				if (!member.matchPassword(curPwd)) {
					throw new InvalidPasswordException();
				}
			}
			
			memberDAO.changePassword(conn, newPwd, memberId);
			conn.commit();

		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);
		}
	}
}