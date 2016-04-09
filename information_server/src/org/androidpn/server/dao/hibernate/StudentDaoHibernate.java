package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.StudentDao;
import org.androidpn.server.model.Student;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class StudentDaoHibernate extends HibernateDaoSupport implements
		StudentDao {

	public void saveStudent(Student student) {
		getHibernateTemplate().saveOrUpdate(student);
		getHibernateTemplate().flush() ;
	}

	public List<Student> getStduents() {
		@SuppressWarnings("unchecked")
		List<Student> list = getHibernateTemplate()
				.find("from Student") ;
		return list;
	}

	public Student getStudent(String id) {
		@SuppressWarnings("unchecked")
		List<Student> list = getHibernateTemplate()
				.find("from Student where ID=?",id) ;
		return list.get(0);
	}

	public Student getStudent(String username, String password) {
		@SuppressWarnings("unchecked")
		List<Student> list = getHibernateTemplate()
				.find("from Student where name=?", username);
		System.out.println(list);
		if(list == null || list.size() == 0){ System.out.println("List为空");return null;}					//数据库中没有数据
		if(list.get(0).getPassword().equals(password)) return list.get(0);
		return null;
	}

}
