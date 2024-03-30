package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据统计业务层
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获取指定数据内的营业额统计报表
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
}
