package com.ekfans.pub.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.sun.mail.iap.ByteArray;



public class ExcelUtil {
    private POIFSFileSystem fs;      
    private Workbook wb;      
    private Sheet sheet;      
    private Row row;    
    /**
    * @Title: readExcel
    * @Description: TODO(根据SheetIndex读取Sheet内容)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param sheetIndex Sheet下标
    * @return List<String[]> 返回类型
    * @throws
     */
    public static List<String[]> readExcel(InputStream is,int sheetIndex){
        List<String[]> dataList = new ArrayList<String[]>();  
        int columnNum = 0;
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new POIFSFileSystem(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(sheetIndex);  
        //获取总列数
        if(sheet.getRow(0)!=null){  
            columnNum = sheet.getRow(1).getLastCellNum()-sheet.getRow(1).getFirstCellNum();  
        }
        if(columnNum > 0){
            for (Row row : sheet) {
                //设置数组长度
                String[] singleRow = new String[columnNum];
                for(int i=0;i<columnNum;i++){  
                    //获取列的数据
                    Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK); 
                    //如果列为空继续下一次循环
                    if(cell == null || "".equals(singleRow[i])) continue;
                    //设置列的值
                    singleRow[i] = getStringCellValue(cell).trim();
                }
                dataList.add(singleRow);
            }
        }
        return dataList;
    }
    /**    
     * 获取单元格数据内容为字符串类型的数据    
     * @param cell Excel单元格    
     * @return String 单元格数据内容    
     */     
    private static String getStringCellValue(Cell cell) {      
        String strCell = "";  
        if(cell == null) return "";
        switch (cell.getCellType()) {      
        case Cell.CELL_TYPE_STRING:      
            strCell = cell.getStringCellValue();      
            break;      
        case Cell.CELL_TYPE_NUMERIC:     
            DecimalFormat df = new DecimalFormat("0");  
            strCell = df.format(cell.getNumericCellValue()); 
            break;      
        case Cell.CELL_TYPE_BOOLEAN:      
            strCell = String.valueOf(cell.getBooleanCellValue());      
            break;      
        case Cell.CELL_TYPE_BLANK:      
            strCell = "";      
            break;      
        default:      
            strCell = "";      
            break;      
        }      
        if (strCell.equals("") || strCell == null) {      
            return "";      
        }           
        return strCell;      
    }   
    /**    
     * 获取单元格数据内容为日期类型的数据    
     * @param cell Excel单元格    
     * @return String 单元格数据内容    
     */     
    private String getDateCellValue(Cell cell) {      
        String result = ""; 
        if(cell == null) return "";
        try {      
            int cellType = cell.getCellType();      
            if (cellType == Cell.CELL_TYPE_NUMERIC) {      
                Date date = cell.getDateCellValue();      
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)       
                + "-" + date.getDate();      
            } else if (cellType == Cell.CELL_TYPE_STRING) {      
                String date = getStringCellValue(cell);      
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();      
            } else if (cellType == Cell.CELL_TYPE_BLANK) {      
                result = "";      
            }      
        } catch (Exception e) {      
            System.out.println("日期格式不正确!");      
            e.printStackTrace();      
        }      
        return result;      
    }    
    
    /**
    * @Title: write_Excel
    * @Description: TODO(写入excel)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param url 到处路径 
    * @param title 表头
    * @param productId 商品分类ID
    * @param categoryId 模板分类ID
    * @throws IOException void 返回类型
    * @throws
     */
    public static byte[]  write_Excel(String[] title,String productId,String categoryId,String sheetName) throws IOException  {  
            HSSFWorkbook wb = new HSSFWorkbook();  
            HSSFSheet sheet = wb.createSheet("模板");  
            //第一行写入商品分类ID,模板分类ID
            HSSFRow rowOne = sheet.createRow(0); 
            HSSFCell cellOne = rowOne.createCell(0);
            //设置文本格式
            cellOne.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellOne.setCellValue("商品分类");
            HSSFCell cellTwo = rowOne.createCell(1);
            //设置文本格式
            cellTwo.setCellType(HSSFCell.CELL_TYPE_STRING);
            HSSFCellStyle cellTwoStyle = wb.createCellStyle();
            //设置不能编辑
            cellTwoStyle.setLocked(true);
            cellTwo.setCellStyle(cellTwoStyle);
            cellTwo.setCellValue(productId);
            HSSFCell cellThree = rowOne.createCell(2);
            //设置文本格式
            cellThree.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellThree.setCellValue("模板分类");
            HSSFCell cellFour = rowOne.createCell(3);
            //设置文本格式
            cellFour.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellFour.setCellValue(categoryId);
            cellFour.setCellStyle(cellTwoStyle);
            wb.setSheetName(0, sheetName);   
            for (int i = 0; i < title.length; i++) {
                HSSFRow row = sheet.createRow(1);  
                for(int cols=0;cols<title.length;cols++){  
                    HSSFCell cell = row.createCell(cols);                     
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);//文本格式  
                    cell.setCellValue(title[cols]);//写入内容  
                }  
            }
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          wb.write(os);
          byte[] bytes = os.toByteArray();
          System.out.println("生成完成");
          return bytes;
        
    }  
    public static void main(String[] args) {
        String path = "C:\\Users\\Jin\\Desktop\\a.xls";
        String [] title =null;
        InputStream is = null;
        try {
            ExcelUtil ex = new ExcelUtil();
            //得到商品名称分类ID,模板ID
           // title= ex.readExcelTitle(is,0);
            //得到表头
            //String [] title1= ex.readExcelTitle(new FileInputStream(new File(path)),1);
            //System.out.println("得到id:"+title[0]+","+title[1]+","+title[2]+","+title[3]);
            //System.out.println("得到标题:"+title1[0]+","+title1[1]+","+title1[2]);
            //得到数据
            List<String[]> lis = readExcel(new FileInputStream(new File(path)),0);
            for (int i = 0; i < lis.size(); i++) {
                String [] a = lis.get(i);
                System.out.println(a[0]+","+a[1]+","+a[2]+","+a[3]);
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        title =new String []{"商品名称","商品ID","商品"};
//        try {
//            write_Excel(path,title,"123","456","测试EXCEL");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//       }
    }
}
