package com.ekfans.base.loan.service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankClientDao;
import com.ekfans.base.finance.model.BankClient;
import com.ekfans.base.finance.model.BankClientLog;
import com.ekfans.base.loan.dao.ILoanAppDao;
import com.ekfans.base.loan.dao.ILoanAppDetailDao;
import com.ekfans.base.loan.dao.ILoanAppLogDao;
import com.ekfans.base.loan.model.LoanApp;
import com.ekfans.base.loan.model.LoanAppDetail;
import com.ekfans.base.loan.model.LoanAppLog;
import com.ekfans.base.loan.util.LoanAppUtil;
import com.ekfans.base.loan.util.LoanTypeRoleUtil;
import com.ekfans.base.order.dao.IOrderDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.wfOrder.dao.IWfOrderDao;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款申请实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class LoanAppService implements ILoanAppService {
	public static Logger log = LoggerFactory.getLogger(LoanAppService.class);

	@Autowired
	private IOrderDao orderDao;

	@Autowired
	private IWfOrderDao wfOrderDao;

	@Autowired
	private ILoanAppDao loanAppDao;

	@Autowired
	private ILoanAppLogDao loanAppLogDao;

	@Autowired
	private ILoanAppDetailDao appDetailDao;

	@Autowired
	private IBankClientDao bankClientDao;

	/**
	 * 查询所有可加载的订单
	 * 
	 * @param orderId
	 * @param orderType
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	@Override
	public List<Object[]> getLoadOrders(String storeId, String orderId, String orderType, String startTime, String endTime, Pager pager) {
		// 如果传过来的企业ID为空，则返回NULL
		if (StringUtil.isEmpty(storeId)) {
			return null;
		}
		try {
			/*
			 * 所有正在融资流程中的订单ID集合
			 */
			Object[] idArr = appDetailDao.getAllLoaningOrderIds();
			
			// 定义返回List
			List<Object[]> rList = null;
			// 查询普通订单和绿色商城订单
			if (StringUtil.isEmpty(orderType) || !"3".equals(orderType)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer(
						"select o.id,o.storeId,store1.storeName,o.userId,store2.storeName,o.status,o.payType,o.payCert,o.totalPrice,o.createTime from Order as o,Store as store1,Store as store2 where 1=1");
				sql.append(" and o.storeId = store1.id");
				sql.append(" and o.userId = store2.id");
				sql.append(" and (o.storeId = :storeId or o.userId = :userId)");
				paramMap.put("storeId", storeId);
				paramMap.put("userId", storeId);
				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(orderType)) {
					sql.append(" and o.type = :orderType");
					paramMap.put("orderType", Integer.parseInt(orderType));
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				// 排除融资中的订单id
				if (idArr != null && idArr.length > 0) {
					sql.append(" and o.id not in :idArr");
					paramMap.put("idArr", idArr);
				}
				// 以下状态的订单可融资
				String[] statusArr = { "2" };
				sql.append(" and o.status in :statusArr");
				paramMap.put("statusArr", statusArr);
				sql.append(" order by o.createTime desc");

				if (pager != null) {
					rList = orderDao.list(pager, sql.toString(), paramMap);
				} else {
					rList = orderDao.list(sql.toString(), paramMap);
				}
			}

			// 查询危废订单
			if (StringUtil.isEmpty(orderType) || "3".equals(orderType)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("select o.id,o.saleId,o.saleName,o.buyId,o.buyName,o.status,o.wfpName,o.wfpTotal,o.totalPrice,o.createTime from WfOrder as o where 1=1");
				sql.append(" and (o.saleId = :saleId or o.buyId = :buyId)");
				paramMap.put("saleId", storeId);
				paramMap.put("buyId", storeId);
				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				// 排除融资中的订单id
				if (idArr != null && idArr.length > 0) {
					sql.append(" and o.id not in :idArr");
					paramMap.put("idArr", idArr);
				}
				// 以下状态的危废订单可融资
				String[] statusArr = { WfOrderHelper.WFORDER_STATUS_WAIT_PAY_YF, WfOrderHelper.WFORDER_STATUS_WAIT_FH, WfOrderHelper.WFORDER_STATUS_WAIT_SH, WfOrderHelper.WFORDER_STATUS_WAIT_PW, WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PW, WfOrderHelper.WFORDER_STATUS_WAIT_SURE_PRICE, WfOrderHelper.WFORDER_STATUS_WAIT_PAY};
				sql.append(" and o.status in :statusArr");
				paramMap.put("statusArr", statusArr);
				sql.append(" order by o.createTime desc");

				List<Object[]> sList = null;
				if (pager != null) {
					sList = wfOrderDao.list(pager, sql.toString(), paramMap);
				} else {
					sList = wfOrderDao.list(sql.toString(), paramMap);
				}

				if (rList == null) {
					rList = sList;
				} else if (sList != null && sList.size() > 0) {
					for (Object[] obj : sList) {
						rList.add(obj);
					}
				}
			}
			
			return rList;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Object[]> getWllsOrders(String storeId, String wlqyId, String orderId, String orderType, String startTime, String endTime, Pager pager) {
		// 如果传过来的企业ID或者往来企业id为空，则返回NULL
		if (StringUtil.isEmpty(storeId) || StringUtil.isEmpty(wlqyId)) {
			return null;
		}
		try {
			// 定义返回List
			List<Object[]> rList = null;
			if (StringUtil.isEmpty(orderType) || !"3".equals(orderType)) { // 查普通订单0和绿色商城订单2
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer(
						"select o.id,o.storeId,store1.storeName,o.userId,store2.storeName,o.status,o.payType,o.payCert,o.totalPrice,o.createTime,o.shippingStatus from Order as o,Store as store1,Store as store2 where 1=1");
				sql.append(" and o.storeId = store1.id");
				sql.append(" and o.userId = store2.id");
				sql.append(" and o.status not in ('0','1','2')"); // 订单状态不为‘取消’、'关闭'、‘待付款’
				sql.append(" and (o.storeId = :storeId and o.userId = :userId or o.storeId = :storeId1 and o.userId = :userId1)");
				paramMap.put("storeId", storeId);
				paramMap.put("userId", wlqyId);
				paramMap.put("storeId1", wlqyId);
				paramMap.put("userId1", storeId);
				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(orderType)) {
					sql.append(" and o.type = :orderType");
					paramMap.put("orderType", Integer.parseInt(orderType));
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				sql.append(" order by o.createTime desc");

				if (pager != null) {
					rList = orderDao.list(pager, sql.toString(), paramMap);
				} else {
					rList = orderDao.list(sql.toString(), paramMap);
				}

			}

			if (StringUtil.isEmpty(orderType) || "3".equals(orderType)) { // 查危废订单3
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("select o.id,o.saleId,o.saleName,o.buyId,o.buyName,o.status,o.wfpName,o.wfpTotal,o.totalPrice,o.createTime,o.payStatus from WfOrder as o where 1=1");
				sql.append(" and o.status not in ('00','01','02')"); // 订单状态不为00新下单待确认
																		// 01已确认订单
																		// 02待支付预付金
				sql.append(" and o.payStatus != '0'"); // 支付状态不为待支付 （ 0:待支付
														// 1:已支付预付金 2:已全额支付）
				sql.append(" and (o.saleId = :saleId and o.buyId = :buyId or o.saleId = :saleId1 and o.buyId = :buyId1)");
				paramMap.put("saleId", storeId);
				paramMap.put("buyId", wlqyId);
				paramMap.put("saleId1", wlqyId);
				paramMap.put("buyId1", storeId);
				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				sql.append(" order by o.createTime desc");

				List<Object[]> sList = null;
				if (pager != null) {
					sList = wfOrderDao.list(pager, sql.toString(), paramMap);
				} else {
					sList = wfOrderDao.list(sql.toString(), paramMap);
				}

				if (rList == null) {
					rList = sList;
				} else if (sList != null && sList.size() > 0) {
					for (Object[] obj : sList) {
						rList.add(obj);
					}
				}
			}
			return rList;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// 订单导出
	@Override
	public boolean exportOrderExcel(String orderType, List<Object[]> orders, HttpServletResponse response) throws Exception {
		String excelName = null;
		if ("0".equals(orderType)) {
			excelName = "普通订单列表" + DateUtil.getSysDateString();
		} else if ("2".equals(orderType)) {
			excelName = "绿色商城订单列表" + DateUtil.getSysDateString();
		} else {
			excelName = "订单列表" + DateUtil.getSysDateString();
		}

		// 设置Excel文件名
		String excelFileName = excelName + ".xls";

		OutputStream op = null;
		op = response.getOutputStream();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单表");
		// 第三步，在sheet中添加表头第0行
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFont(font);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("卖家");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("买家");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("订单金额");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("订单状态");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("发货状态");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);

		// 第五步，写入实体数据这些数据从数据库得到
		// 拼装excel内容（orders不为空）
		if (orders != null) {
			for (int i = 0; i < orders.size(); i++) {

				Object[] order = orders.get(i);
				row = sheet.createRow((int) i + 1);
				// 订单状态
				String status = null;
				if ("0".equals(order[5])) {
					status = "取消";
				} else if ("1".equals(order[5])) {
					status = "关闭";
				} else if ("2".equals(order[5])) {
					status = "待付款";
				} else if ("3".equals(order[5])) {
					status = "已付款";
				} else if ("4".equals(order[5])) {
					status = "确认收货";
				}
				// 发货状态
				String shippingStatus = null;
				if ("true".equals(order[10])) {
					shippingStatus = "发货";
				} else {
					shippingStatus = "未发货";
				}

				// 创建单元格，并设置值
				int j = 0;
				insertCell(row, j++, order[0]);// 订单号
				insertCell(row, j++, order[2]); // 卖家
				insertCell(row, j++, order[4]); // 买家
				insertCell(row, j++, order[8]);// 总金额
				insertCell(row, j++, status);// 订单状态0：取消，1：关闭，2：待付款，3：已付款，4：确认收货(确认收货后,买家进行评价)；5：完成(卖家对买家评价完毕,算完成))
				insertCell(row, j++, shippingStatus); // 发货状态
				insertCell(row, j++, order[9]);// 订单日期
			}
		}

		// 调整列宽
		sheet.autoSizeColumn((short) 0); // 调整第一列宽度
		sheet.autoSizeColumn((short) 1); // 调整第二列宽度
		sheet.autoSizeColumn((short) 2); // 调整第三列宽度
		sheet.autoSizeColumn((short) 3); // 调整第四列宽度
		sheet.autoSizeColumn((short) 4); // 调整第五列宽度
		sheet.autoSizeColumn((short) 5); // 调整第六列宽度
		sheet.autoSizeColumn((short) 6); // 调整第七列宽度

		// //测试输出
		// File file = new File("e:/订单表.xls");
		// file.createNewFile();
		// FileOutputStream stream = FileUtils.openOutputStream(file);
		// wb.write(stream);
		// stream.close();

		try {
			// 输出信息，导出excel
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			// response.setHeader("Content-Type", "application/octet-stream");
			excelFileName = new String(excelFileName.getBytes("UTF-8"), "iso-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=\"" + excelFileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (op != null) {
				try {
					op.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 危废订单导出
	@Override
	public boolean exportWfOrderExcel(List<Object[]> orders, HttpServletResponse response) throws Exception {
		String excelName = null;
		excelName = "危废订单列表" + DateUtil.getSysDateString();

		// 设置Excel文件名
		String excelFileName = excelName + ".xls";

		OutputStream op = null;
		op = response.getOutputStream();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单表");
		// 第三步，在sheet中添加表头第0行
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFont(font);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("危废名称");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("产生方");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("处置方");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("订单状态");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);

		// 第五步，写入实体数据这些数据从数据库得到
		// 拼装excel内容（orders不为空）
		if (orders != null) {
			for (int i = 0; i < orders.size(); i++) {

				Object[] order = orders.get(i);
				row = sheet.createRow((int) i + 1);
				// 订单状态
				String status = null;
				status = WfOrderHelper.orderStatusMap.get(order[5]);

				// 创建单元格，并设置值
				int j = 0;
				insertCell(row, j++, order[0]);// 订单号
				insertCell(row, j++, order[6]);// 危废名称
				insertCell(row, j++, order[2]); // 卖家
				insertCell(row, j++, order[4]); // 买家
				insertCell(row, j++, status);// 订单状态
												// WfOrderHelper.orderStatusMap.get(key)
				insertCell(row, j++, order[9]);// 订单日期
			}
		}

		// 调整列宽
		sheet.autoSizeColumn((short) 0); // 调整第一列宽度
		sheet.autoSizeColumn((short) 1); // 调整第二列宽度
		sheet.autoSizeColumn((short) 2); // 调整第三列宽度
		sheet.autoSizeColumn((short) 3); // 调整第四列宽度
		sheet.autoSizeColumn((short) 4); // 调整第五列宽度
		sheet.autoSizeColumn((short) 5); // 调整第六列宽度

		// //测试输出
		// File file = new File("e:/订单表.xls");
		// file.createNewFile();
		// FileOutputStream stream = FileUtils.openOutputStream(file);
		// wb.write(stream);
		// stream.close();

		try {
			// 输出信息，导出excel
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			// response.setHeader("Content-Type", "application/octet-stream");
			excelFileName = new String(excelFileName.getBytes("UTF-8"), "iso-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=\"" + excelFileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (op != null) {
				try {
					op.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	// 历史订单导出
	@Override
	public boolean exportHistoryOrderExcel(List<Object[]> orders, HttpServletResponse response) throws Exception {
		String excelName = null;
		excelName = "历史订单列表" + DateUtil.getSysDateString();

		// 设置Excel文件名
		String excelFileName = excelName + ".xls";

		OutputStream op = null;
		op = response.getOutputStream();
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("订单表");
		// 第三步，在sheet中添加表头第0行
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setFont(font);
		// 订单类型 订单号 甲方 乙方 订单金额 下单时间
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单类型");
		cell.setCellStyle(style);

		cell = row.createCell(1);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);

		cell = row.createCell(2);
		cell.setCellValue("甲方");
		cell.setCellStyle(style);

		cell = row.createCell(3);
		cell.setCellValue("乙方");
		cell.setCellStyle(style);

		cell = row.createCell(4);
		cell.setCellValue("订单金额");
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellValue("下单时间");
		cell.setCellStyle(style);

		// 第五步，写入实体数据这些数据从数据库得到
		// 拼装excel内容（orders不为空）
		if (orders != null) {
			for (int i = 0; i < orders.size(); i++) {

				Object[] order = orders.get(i);
				row = sheet.createRow((int) i + 1);
				// 订单类型
				int type = -1;
				try {
					type = Integer.parseInt(order[0] + "");
				} catch (Exception e) {
				}
				String typeStr = "";

				if (type == 0) {
					typeStr = "普通订单";
				} else if (type == 1) {
					typeStr = "直付订单";
				} else if (type == 2) {
					typeStr = "绿色商城订单";
				} else if (type == 3) {
					typeStr = "危废处置订单";
				}

				// 创建单元格，并设置值
				int j = 0;
				insertCell(row, j++, typeStr);
				insertCell(row, j++, order[1]);
				insertCell(row, j++, order[2]);
				insertCell(row, j++, order[3]);
				insertCell(row, j++, order[4]);
				insertCell(row, j++, order[5]);
			}
		}

		// 调整列宽
		sheet.autoSizeColumn((short) 0); // 调整第一列宽度
		sheet.autoSizeColumn((short) 1); // 调整第二列宽度
		sheet.autoSizeColumn((short) 2); // 调整第三列宽度
		sheet.autoSizeColumn((short) 3); // 调整第四列宽度
		sheet.autoSizeColumn((short) 4); // 调整第五列宽度
		sheet.autoSizeColumn((short) 5); // 调整第六列宽度

		// //测试输出
		// File file = new File("e:/订单表.xls");
		// file.createNewFile();
		// FileOutputStream stream = FileUtils.openOutputStream(file);
		// wb.write(stream);
		// stream.close();

		try {
			// 输出信息，导出excel
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			// response.setHeader("Content-Type", "application/octet-stream");
			excelFileName = new String(excelFileName.getBytes("UTF-8"), "iso-8859-1");
			response.setHeader("Content-disposition", "attachment;filename=\"" + excelFileName + "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (op != null) {
				try {
					op.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 根据申请ID获取申请对象
	 * 
	 * @param loanAppId
	 *            申请对象ID
	 * @param type
	 *            false:单纯的对象，true:包含子
	 * @return
	 */
	@Override
	public LoanApp getAppById(String loanAppId, Boolean type) {
		if (StringUtil.isEmpty(loanAppId)) {
			return null;
		}
		try {
			// 获取申请对象
			LoanApp app = (LoanApp) loanAppDao.get(loanAppId);
			if (app == null || !type) {
				return app;
			}
			// 调用DAO查询明细集合
			List<LoanAppDetail> detailList = appDetailDao.getDetailsByAppId(loanAppId, null);
			app.setDetails(detailList);
			// 调用DAO查询日志集合
			List<LoanAppLog> logList = loanAppLogDao.getLogsByLoanAppId(loanAppId, null);
			app.setLogs(logList);

			return app;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 新增或更新融资申请
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public Boolean saveOrUpdateLoanApp(HttpServletRequest request, HttpServletResponse response) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		if (store == null) {
			return false;
		}
		// 从页面获取申请ID
		String appId = request.getParameter("loanAppId");
		// 定义是否更新的占位符
		Boolean isUpdate = false;
		// 定义申请日志
		LoanAppLog appLog = new LoanAppLog();

		// 设置融资申请的常规数据
		appLog.setCreateTime(DateUtil.getSysDateTimeString());
		appLog.setCreatorId(store.getId());
		appLog.setCreatorType("0");
		appLog.setCreator(store.getStoreName());
		appLog.setCompanyId(store.getId());

		LoanApp app = null;
		if (!StringUtil.isEmpty(appId)) {
			app = getAppById(appId, false);
			isUpdate = true;
			appLog.setNote("修改融资申请");
		} else {
			app = new LoanApp();
			app.setCreateTime(DateUtil.getSysDateTimeString());
			appLog.setNote("提交融资申请");
			// 设置处理状态：待处理
			app.setAppStatus(LoanAppUtil.APP_STATUS_APPLY);
		}

		String bankId = request.getParameter("bankId");
		String typeId = request.getParameter("typeId");
		String price = request.getParameter("price");

		// 设置更新时间
		app.setUpdateTime(DateUtil.getSysDateTimeString());
		// 设置银行ID
		app.setBankId(bankId);
		// 设置企业ID
		app.setCompanyId(store.getId());
		// 设置贷款类型ID
		app.setLoanTypeId(typeId);
		// 设置贷款金额
		app.setPrice(new BigDecimal(price));
		// 设置通过状态
		BankClient bankClient = bankClientDao.getByStoreIdAndBankId(store.getId(), bankId);
		if (bankClient != null) {
			boolean companyStatus = LoanTypeRoleUtil.qualifiedCreditList.contains(bankClient.getCreditRate());
			app.setCompanyStatus(companyStatus);
		}

		// 设置日志的银行ID
		appLog.setBankId(bankId);

		// ------新增或修改银行客户及日志信息-----
		// 定义银行会员日志
		BankClientLog bcLog = new BankClientLog();
		// 设置日志数据
		bcLog.setCreateTime(DateUtil.getSysDateTimeString());
		bcLog.setCreator(store.getStoreName());
		bcLog.setCreatorId(store.getId());
		bcLog.setBankId(bankId);
		bcLog.setStoreId(store.getId());

		Boolean isModify = false;
		BankClient bc = null;
		// 根据storeId和bankId查找客户是否存在

		List<BankClient> bcList = bankClientDao.getBankClientsByParams(store.getId(), bankId, null, null, null);
		// pre为空则新增银行客户
		if (bcList == null) {
			bc = new BankClient();
			bc.setStoreId(store.getId());
			bc.setBankId(bankId);
			bc.setStoreName(store.getStoreName());
			bc.setCreateTime(DateUtil.getSysDateTimeString());
			bc.setType("0"); // 默认为普通客户

			bcLog.setNote("新增银行客户");
		} else {
			bc = bcList.get(0);
			// bc.setType("0");
			isModify = true;

			bcLog.setNote("修改银行客户");
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = loanAppDao.createSession();
			transaction = session.beginTransaction();
			// 调用Dao新增或更新融资申请
			if (isUpdate) {
				loanAppDao.updateBean(app, session);
				// 删除明细
				appDetailDao.deleteByAppId(app.getId(), session);
			} else {
				loanAppDao.addBean(app, session);
			}

			// 更新或者新增银行用户
			if (isModify) {
				bankClientDao.updateBean(bc, session);
			} else {
				bankClientDao.addBean(bc, session);
			}
			ILoanAppDetailService detailService = SpringContextHolder.getBean(ILoanAppDetailService.class);
			detailService.addDetails(request, response, app.getId(), typeId, session);
			
			appLog.setAppId(app.getId());
			// 调用DAO新增日志
			bcLog.setAccountId(bc.getId());
			loanAppLogDao.addBean(appLog, session);
			// 新增银行用户日志
			bankClientDao.addBean(bcLog, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 列表查询
	 * 
	 * @param bankId
	 * @param loanTypeId
	 * @param appStatus
	 * @param pager
	 * @return
	 */
	@Override
	public List<LoanApp> listLoanApp(String storeId, String bankId, String loanTypeId, String appStatus, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select app,bank.bankName,loanType.loanName from LoanApp as app,Bank as bank,LoanType as loanType where 1=1");
		sql.append(" and app.bankId = bank.id");
		sql.append(" and app.loanTypeId = loanType.id");
		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and app.companyId = :storeId");
			paramMap.put("storeId", storeId);
		}
		if (!StringUtil.isEmpty(bankId)) {
			sql.append(" and app.bankId = :bankId");
			paramMap.put("bankId", bankId);
		}
		if (!StringUtil.isEmpty(loanTypeId)) {
			sql.append(" and app.loanTypeId = :loanTypeId");
			paramMap.put("loanTypeId", loanTypeId);
		}
		if (!StringUtil.isEmpty(appStatus)) {
			sql.append(" and app.appStatus = :appStatus");
			paramMap.put("appStatus", appStatus);
		}
		sql.append(" order by app.createTime desc");
		try {
			List<Object[]> objList = null;
			if (pager != null) {
				objList = loanAppDao.list(pager, sql.toString(), paramMap);
			} else {
				objList = loanAppDao.list(sql.toString(), paramMap);
			}
			List<LoanApp> rList = new LinkedList<LoanApp>();
			if (objList != null && objList.size() > 0) {
				for (int i = 0; i < objList.size(); i++) {
					Object[] obj = objList.get(i);
					LoanApp app = (LoanApp) obj[0];
					String bankName = obj[1].toString();
					String loanTypeName = obj[2].toString();
					if (app != null) {
						app.setBankName(bankName);
						app.setLoanTypeName(loanTypeName);
						rList.add(app);
					}
				}
			}
			return rList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 取消融资申请
	 * 
	 * @param app
	 * @return
	 */
	public Boolean changeAppStatus(LoanApp app, String appStatus, String creator, String creatorId, String creatorType) {
		// 如果传过来的对象为空，则返回失败
		if (app == null) {
			return false;
		}
		// 定义事务处理机制
		Session session = null;
		Transaction transaction = null;
		try {
			// 创建Session并开始事务处理
			session = loanAppDao.createSession();
			transaction = session.beginTransaction();
			// 设置申请的状态并更新
			app.setAppStatus(appStatus);
			app.setUpdateTime(DateUtil.getSysDateTimeString());
			loanAppDao.updateBean(app, session);

			// 新增融资申请的操作日志
			LoanAppLog appLog = new LoanAppLog();
			appLog.setAppId(app.getId());
			appLog.setBankId(app.getBankId());
			appLog.setCompanyId(app.getCompanyId());
			appLog.setCreateTime(DateUtil.getSysDateTimeString());
			appLog.setCreator(creator);
			appLog.setCreatorId(creatorId);
			appLog.setCreatorType(creatorType);
			if (LoanAppUtil.APP_STATUS_CANCEL.equals(appStatus)) {
				appLog.setNote("取消融资申请");
			} else {
				appLog.setNote("将融资申请状态修改成" + LoanAppUtil.appStatusMap.get(appStatus));
			}
			loanAppLogDao.addBean(appLog, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return false;
	}

	/**
	 * 查询所有历史订单
	 * 
	 * @param orderId
	 * @param orderType
	 * @param startTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	@Override
	public List<Object[]> getHistoreOrders(String storeId, String orderId, String orderType, String startTime, String endTime, Pager pager) {
		// 如果传过来的企业ID为空，则返回NULL
		if (StringUtil.isEmpty(storeId)) {
			return null;
		}

		// 在这里，Order类型用0,1,2表示 WfOrder用3表示
		String wfOrderType = "3";

		try {
			// 统一返回Object对象的字段
			// 订单类型 订单号 甲方 乙方 订单金额 下单时间
			// Order.java订单数据
			List<Object[]> orderList = new ArrayList<>();
			if (StringUtil.isEmpty(orderType) || (!StringUtil.isEmpty(orderType) && !wfOrderType.equals(orderType))) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("select o.type,o.id,store1.storeName,store2.storeName,o.totalPrice,o.createTime from Order as o,Store as store1,Store as store2 where 1=1");
				sql.append(" and o.storeId = store1.id");
				sql.append(" and o.userId = store2.id");
				sql.append(" and (o.storeId = :storeId or o.userId = :userId)");
				paramMap.put("storeId", storeId);
				paramMap.put("userId", storeId);

				// 只查询已完成
				String status = "5";
				sql.append(" and o.status = :status");
				paramMap.put("status", status);

				if (!StringUtil.isEmpty(orderType)) {
					sql.append(" and o.type = :orderType");
					paramMap.put("orderType", Integer.parseInt(orderType));
				}
				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				sql.append(" order by o.createTime desc");

				orderList = orderDao.list(pager, sql.toString(), paramMap);
			}

			// wfOrder.java订单数据
			List<Object[]> wfOrderList = new ArrayList<>();
			if (StringUtil.isEmpty(orderType) || wfOrderType.equals(orderType)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer sql = new StringBuffer("select ");
				sql.append(wfOrderType + ",");
				sql.append("o.id,o.saleName,o.buyName,o.totalPrice,o.createTime from WfOrder as o where 1=1");
				sql.append(" and (o.saleId = :saleId or o.buyId = :buyId)");
				paramMap.put("saleId", storeId);
				paramMap.put("buyId", storeId);

				// 只查询已完成
				String status = WfOrderHelper.WFORDER_STATUS_WAIT_FINISH;
				sql.append(" and o.status = :status");
				paramMap.put("status", status);

				if (!StringUtil.isEmpty(orderId)) {
					sql.append(" and o.id like :orderId");
					paramMap.put("orderId", "%" + orderId + "%");
				}
				if (!StringUtil.isEmpty(startTime)) {
					sql.append(" and o.createTime >= :startTime");
					paramMap.put("startTime", startTime);
				}
				if (!StringUtil.isEmpty(endTime)) {
					sql.append(" and o.createTime <= :endTime");
					paramMap.put("endTime", endTime);
				}
				sql.append(" order by o.createTime desc");

				wfOrderList = wfOrderDao.list(pager, sql.toString(), paramMap);
			}

			// 合并
			orderList.addAll(wfOrderList);

			// 按时间排序:降序排列，如果时间为空，将放在最后
			if (orderList != null) {
				Collections.sort(orderList, new Comparator<Object[]>() {
					@Override
					public int compare(Object[] o1, Object[] o2) {
						Long time1 = 0l;
						Long time2 = 0l;
						try {
							time1 = DateUtil.sdfDateTime.parse(o1[5] + "").getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						try {
							time2 = DateUtil.sdfDateTime.parse(o2[5] + "").getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}

						return time2.compareTo(time1);
					}
				});
			}

			return orderList;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	// 插入单元格
	private void insertCell(HSSFRow row, int i, Object object) {
		if (object == null) {
			row.createCell(i).setCellValue("");
		} else {
			row.createCell(i).setCellValue(object.toString());
		}

	}

}