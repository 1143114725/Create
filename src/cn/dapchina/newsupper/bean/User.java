package cn.dapchina.newsupper.bean;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1582281324939351310L;

	/**
	 * 数据库中的id号
	 */
	public Integer id;
	
	/**
	 * 用户帐号
	 */
	public String userId;
	
	/**
	 * 用户名称
	 */
	public String userName;
	
	/**
	 * 用户密码
	 */
	public String userPass;
	
	/**
	 * 用户类型
	 */
	public Integer userType;
	
	/**
	 * 可用还是冻结
	 */
	public int isEnable;
	
	/**
	 * 部门或上级的id号(所属部门)
	 */
	public String parentId;
	
	/**
	 * 帐号的创建时间
	 */
	public String createTime;
	
	/**
	 * 是否需要修改密码
	 */
	public int isReset;
	
	public int isFree = 1;
}
