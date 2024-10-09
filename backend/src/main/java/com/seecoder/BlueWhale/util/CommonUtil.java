package com.seecoder.BlueWhale.util;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.Comment;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.CommentRepository;
import com.seecoder.BlueWhale.repository.CommodityRepository;
import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Component
public class CommonUtil {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    OssUtil ossUtil;
    static String[] columns = {"User ID", "Store ID", "Order ID", "Commodity Name", "Price", "Quantity", "Order Time"};

    public String getStoreNameById(Integer id) {
        if (id == null) return null;
        Optional<Store> res = storeRepository.findById(id);
        return res.map(Store::getName).orElse(null);
    }

    public String generateStatement() {
        User user =  securityUtil.getCurrentUser();
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Statement");
        for (int i = 0 ; i < columns.length; i++) {
            sheet.createRow(0).createCell(i).setCellValue(columns[i]);
        }
        List<Order> orders;
        if (user.getRole() == RoleEnum.CEO || user.getRole() == RoleEnum.MANAGER) {
            orders = orderRepository.findAll();

        } else if (user.getRole() == RoleEnum.STAFF) {
            orders = orderRepository.findByStoreId(user.getStoreId());
        } else {
            return null;
        }

        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            sheet.createRow(i + 1).createCell(0).setCellValue(order.getUserId());
            sheet.getRow(i + 1).createCell(1).setCellValue(order.getStoreId());
            sheet.getRow(i + 1).createCell(2).setCellValue(order.getId());
            sheet.getRow(i + 1).createCell(3).setCellValue(commodityRepository.findById(order.getCommodityId()).get().getName());
            sheet.getRow(i + 1).createCell(4).setCellValue(commodityRepository.findById(order.getCommodityId()).get().getPrice().doubleValue());
            sheet.getRow(i + 1).createCell(5).setCellValue(order.getCount());
            sheet.getRow(i + 1).createCell(6).setCellValue(order.getCreateTime().toString());
        }

        String originalInfo = user.getName();
        String timestamp = String.valueOf(new Date().getTime());

        String fileName = originalInfo + "_" + timestamp + ".xlsx";
        String url = null;
        try (FileOutputStream file = new FileOutputStream(fileName)) {
            workbook.write(file);
            InputStream inputStream = new FileInputStream(fileName);


            url =  ossUtil.upload(fileName, inputStream);

            File localFile = new File(fileName);

            localFile.delete();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


}
