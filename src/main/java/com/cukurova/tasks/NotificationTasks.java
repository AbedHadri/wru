package com.cukurova.tasks;
 
import com.cukurova.model.Notification;
import com.cukurova.utils.Conn;
import com.cukurova.utils.DateOps;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationTasks extends Notification  {

    private int notificationCount;

    public List<Notification> pullUserNotifications(String useId, int limit) throws SQLException {
        this.notificationCount = 0;
        Notification notData = new Notification();
        Conn conn = new Conn();
        List<Notification> rsltList = new ArrayList<>();

        ResultSet rs = conn.sqlExecuteSelect("SELECT * FROM NOTIFICATIONS WHERE KULL_ID =  ? ORDER BY DATE_CREATED DESC LIMIT ?", useId, limit);

        while (rs.next()) {
            this.notificationCount++;

            rsltList.add(extractFromResultSet(rs));
        }

        return rsltList;
    }

    public void setUserNotificationsSeen(String userId) throws SQLException {
        Conn conn = new Conn();
        conn.sqlExecuteUpdate("UPDATE NOTIFICATIONS SET WAS_SEEN = 1 WHERE KULL_ID = ?", userId);

    }

    public List<Notification> liveNotifications(String userId, Date lastUpdateDate) throws SQLException, InterruptedException, ParseException {
        List<Notification> rsltList = new ArrayList<>();
        this.userId = userId;
        ResultSet rs;
        int count = 0;
        do {
            Thread.sleep(3000);
            rs = new Conn().sqlExecuteSelect("SELECT * FROM NOTIFICATIONS WHERE KULL_ID = ? AND DATE_CREATED > ? ORDER BY DATE_CREATED DESC LIMIT 10", userId, lastUpdateDate);
//            System.out.println(DateOps.stringToDate(lastUpdateDate));
            if (rs.next()) {
                break;
            }
            count++;
        } while (count < 5);

        if (count < 5) {
            setAllAsSeen();
            do {
                rsltList.add(extractFromResultSet(rs));
            } while (rs.next());
        }
        return rsltList;
    }

    private Notification extractFromResultSet(ResultSet rs) throws SQLException {
        Notification notData = new Notification();

        notData.content = rs.getString("LINK");
        notData.content = rs.getString("CONTENT");
        notData.title = rs.getString("TITLE");
        notData.wasSeen = rs.getInt("WAS_SEEN");
        notData.createDateFormatted = DateOps.dateToStringFormatted(DateOps.getDateFromTimeStamp(rs.getTimestamp("DATE_CREATED")));
        notData.link = rs.getString("LINK");

        return notData;
    }

    public void setAllAsSeen() throws SQLException {
        new Conn().sqlExecuteUpdate("UPDATE NOTIFICATIONS SET WAS_SEEN = 1 WHERE KULL_ID  = ? AND WAS_SEEN = 0 ", this.userId);
    }

}
