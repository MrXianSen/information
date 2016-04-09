package com.albery.bean;
/**
 * 分页处理的bean
 * @author 张建国
 */
public class PageBean {
	
	//当前页码
	private int pc ;
	
	//总页数
	private int tp ;
	
	//数据库中的总记录数
	private int tr ;
	
	//单页大小
	private int ps ;

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getTp() {
		
		tp = tr % ps ;
		if(tp == 0)
		{
			tp = tr / ps ;
		}
		else
		{
			tp = tr / ps + 1 ;
		}
		
		return tp;
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}
	
}
