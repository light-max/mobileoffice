package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.model.entity.SignIn;
import com.lfq.mobileoffice.model.entity.SignInTime;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * 员工的考勤
 *
 * @author 李凤强
 */
@Getter
public class EmployeeAttendance {
    /*** 哪一天的考勤，年 */
    private int year;

    /*** 哪一天的考勤，月 */
    private int month;

    /*** 哪一天的考勤，日 */
    private int day;

    /*** 上班签到时间 8:00*/
    private String toWork;

    /*** 下班签到时间 17:00*/
    private String offWork;

    /*** 考勤时间，在这个时间之前签到上班 */
    private String before;

    /*** 考勤时间，在这个时间之后签到下班 */
    private String after;

    /**
     * 构造函数
     *
     * @param date       考勤日期
     * @param signInTime 考勤时间
     * @param toWork     上班签到时间
     * @param offWork    下班签到时间
     */
    public EmployeeAttendance(
            @NonNull LocalDate date,
            @Nullable SignInTime signInTime,
            @Nullable SignIn toWork,
            @Nullable SignIn offWork
    ) {
        year = date.getYear();
        month = date.getMonthValue();
        day = date.getDayOfMonth();

        if (signInTime != null) {
            this.before = signInTime.getStart();
            this.after = signInTime.getEnd();
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if (toWork != null) {
            this.toWork = format.format(new Date(toWork.getCreateTime()));
        }
        if (offWork != null) {
            this.offWork = format.format(new Date(offWork.getCreateTime()));
        }

        if (toWork == null && offWork == null) {
            status = 2;
        } else if (toWork == null) {
            status = 3;
        } else if (offWork == null) {
            status = 4;
        } else {
            if (before == null || after == null) {
                status = 0;
            } else if (before.compareTo(this.toWork) < 0) {
                status = 5;
            } else if (after.compareTo(this.offWork) > 0) {
                status = 6;
            } else {
                status = 1;
            }
        }
    }

    /**
     * 状态
     * <ul>
     *     <li>0.没有考勤信息</li>
     *     <li>1.正常</li>
     *     <li>2.缺勤</li>
     *     <li>3.上班未签到</li>
     *     <li>4.下班未签到</li>
     *     <li>5.迟到</li>
     *     <li>6.早退</li>
     * </ul>
     */
    private int status;
}
