package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.InformationComment;

public interface InformationCommentService {

	// 保存评论内容
	void saveInformationComment(InformationComment informationComment);

	// 获取评论内容
	List<InformationComment> getInformationCommentsByInformationID(
			String informationID);

	// 删除评论
	void deleteInformationCommentByID(String id);

	// 通过id获取信心评论
	InformationComment getInformationCommentByID(String id) ;
}
