package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.InformationComment;

/**
 * Dao层接口
 * 
 * @author Albery
 *
 */
public interface InformationCommentDao {
	//保存评论内容
	void saveInformationComment(InformationComment informationComment) ;
	
	//获取评论内容
	List<InformationComment> getInformationCommentsByInformationID(String informationID) ;
	
	//通过id获取信心评论
	InformationComment getInformationCommentByID(String id) ;
	
	//删除评论
	void deleteInformationCommentByID(String id) ;
}
