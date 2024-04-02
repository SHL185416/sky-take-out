package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据统计业务层
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    WorkspaceService workspaceService;

    /**
     * 获取指定时间内的营业额统计报表
     *
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Double> turnovers = new ArrayList<>();
        LocalDate current = begin;

        while (current.isBefore(end.plusDays(1))) {
            dates.add(current);
            LocalDateTime dateStart = LocalDateTime.of(current, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(current, LocalTime.MAX);
            // 频繁执行sql
            Double turnover = orderMapper.getTurnoverByDate(dateStart, dateEnd, Orders.COMPLETED);
            turnover = turnover == null ? 0.0d : turnover;
            turnovers.add(turnover);
            current = current.plusDays(1);
        }
        String dateList = StringUtils.join(dates, ",");
        String turnoverList = StringUtils.join(turnovers, ",");
        TurnoverReportVO reportVO = TurnoverReportVO.builder()
                .dateList(dateList)
                .turnoverList(turnoverList)
                .build();
        return reportVO;
    }

    /**
     * 获取指定时间内的用户额统计报表
     *
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Integer> totalUsers = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();

        LocalDate current = begin;
        while (current.isBefore(end.plusDays(1))) {
            dates.add(current);
            LocalDateTime dateStart = LocalDateTime.of(current, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(current, LocalTime.MAX);
            // 频繁执行sql
            Integer totalUser = userMapper.getUserByDate(null, dateEnd);
            Integer newUser = userMapper.getUserByDate(dateStart, dateEnd);
            totalUser = totalUser == null ? 0 : totalUser;
            newUser = newUser == null ? 0 : newUser;
            totalUsers.add(totalUser);
            newUsers.add(newUser);
            current = current.plusDays(1);
        }
        String dateList = StringUtils.join(dates, ",");
        String totalUsersList = StringUtils.join(totalUsers, ",");
        String newUsersList = StringUtils.join(newUsers, ",");
        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(dateList)
                .newUserList(newUsersList)
                .totalUserList(totalUsersList)
                .build();
        return userReportVO;
    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Integer> validOrders = new ArrayList<>();
        List<Integer> totalOrders = new ArrayList<>();

        LocalDate current = begin;
        Integer validOrderCount = 0;
        Integer totalOrderCount = 0;
        while (current.isBefore(end.plusDays(1))) {
            dates.add(current);
            LocalDateTime dateStart = LocalDateTime.of(current, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(current, LocalTime.MAX);
            // 频繁执行sql
            Integer totalOrder = orderMapper.getOrderByDate(dateStart, dateEnd, null);
            Integer validOrder = orderMapper.getOrderByDate(dateStart, dateEnd, Orders.COMPLETED);
            totalOrder = totalOrder == null ? 0 : totalOrder;
            validOrder = validOrder == null ? 0 : validOrder;
            //更新订单总数量
            totalOrderCount += totalOrder;
            validOrderCount += validOrder;

            totalOrders.add(totalOrder);
            validOrders.add(validOrder);
            current = current.plusDays(1);
        }

        double validOrderRate = totalOrderCount != 0 ? validOrderCount.doubleValue() / totalOrderCount : 0.0d;

        String dateList = StringUtils.join(dates, ",");
        String totalUserList = StringUtils.join(totalOrders, ",");
        String validOrderList = StringUtils.join(validOrders, ",");

        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(dateList)
                .validOrderCountList(validOrderList)
                .orderCountList(totalUserList)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(validOrderRate).build();


        return orderReportVO;
    }

    /**
     * top10统计
     *
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime dateStart = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime dateEnd = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> top10 = orderMapper.getTop10(dateStart, dateEnd);
        System.out.println("top10number is " + top10.get(0).getNumber());

        List<String> names = top10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = top10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        String nameList = StringUtils.join(names, ",");
        String numberList = StringUtils.join(numbers, ",");
        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
        return salesTop10ReportVO;
    }


    /**
     * 导出运营数据报表
     *
     * @param response
     */
    public void export(HttpServletResponse response) {

        // 查数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));


        // 写数据
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(stream);
            XSSFSheet sheet = excel.getSheetAt(0);
            sheet.getRow(1).getCell(1).setCellValue("time: " + begin + " -> " + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessDay = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessDay.getTurnover());
                row.getCell(3).setCellValue(businessDay.getValidOrderCount());
                row.getCell(4).setCellValue(businessDay.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDay.getUnitPrice());
                row.getCell(6).setCellValue(businessDay.getNewUsers());
            }
            // 下载文件到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
