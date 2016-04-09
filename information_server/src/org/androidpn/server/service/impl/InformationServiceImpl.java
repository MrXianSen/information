package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.InformationDao;
import org.androidpn.server.model.Information;
import org.androidpn.server.service.InformationService;

/**
 * Service实现类
 * 
 * 实现服务层的业务逻辑
 * 
 * @author Administrator
 *
 */
public class InformationServiceImpl implements InformationService {
	
	private InformationDao informationDao ;

	public InformationDao getInformationDao() {
		return informationDao;
	}

	public void setInformationDao(InformationDao informationDao) {
		this.informationDao = informationDao;
	}

	public void saveInformation(Information information) {
		informationDao.saveInformation(information);
	}

	public List<Information> findInformationByType(String type) {
		return informationDao.findInformationByType(type);
	}

	public List<Information> findInformationByTitle(String title) {
		return informationDao.findInformationByTitle(title);
	}

	public List<Information> getInformations() {
		return informationDao.getInformations();
	}

	public void deleteInformation(Information information) {
		informationDao.deleteInformation(information);
	}

	public Information findInformationById(String id) {
		return informationDao.findInformationById(id);
	}

	public List<Information> getInformations(int start,int count) {
		return informationDao.getInformations(start, count);
	}

	public int getTotalRecord() {
		return informationDao.getTotalRecord();
	}

}
