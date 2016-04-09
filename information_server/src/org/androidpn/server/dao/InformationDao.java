package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.Information;

/**
 * 信息的DAO层接口
 * 
 * 提供数据库操作的方法
 * @author Albery
 *
 */
public interface InformationDao {
	
	//保存信息
	void saveInformation(Information information) ;

	//通过信息的类型查询信息
	List<Information> findInformationByType(String type) ;
	
	//通过标题查询信息
	List<Information> findInformationByTitle(String title) ;
	
	//通过ID查询信息
	Information findInformationById(String id) ;
	
	//获取所有的信息
	List<Information> getInformations() ;
	
	//获取指定数目的信息
	List<Information> getInformations(int start, int count) ;
	
	//删除信息
	void deleteInformation(Information information) ;
	
	//获取数据库中数据总数
	int getTotalRecord() ;
}
