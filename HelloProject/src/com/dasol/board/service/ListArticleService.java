package com.dasol.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dasol.board.dao.ArticleDAO;
import com.dasol.board.model.Article;
import com.dasol.jdbc.ConnectionProvider;

public class ListArticleService {
	private ArticleDAO articleDAO = new ArticleDAO();
	private static final int PAGE_SIZE = 10; // 각 페이지당 글의 개수
	private static final int PAGE_GROUP_SIZE = 9; // 페이지 그룹 사이즈

	public ArticlePage getArticlePage(int pageNum) {
		try (Connection conn = ConnectionProvider.getConnection()) {
			int total = articleDAO.selectCount(conn);
			List<Article> articleList = articleDAO.select(conn, (pageNum - 1) * PAGE_SIZE, PAGE_SIZE);
			return new ArticlePage(total, pageNum, articleList, PAGE_SIZE, PAGE_GROUP_SIZE);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}