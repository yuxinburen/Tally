package model;

import android.widget.DatePicker;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.Date;

/**
 * User: davie
 * Date: 15-4-12
 */
@Table(name = "details")
public class Detail {

    @Id(column = "_id")
    private long id;

    @Column(column = "money",defaultValue = "0.0")
    private float money;

    @Column(column = "dt")
    private String date;

    @Column(column = "note")
    private String note;

    @Foreign(column = "tid", foreign = "_id")
    private Type type;

    @Foreign(column = "cid", foreign = "_id")
    private Category category;

    @Column(column = "remark")
    private int remark;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getRemark() {
        return remark;
    }

    public void setRemark(int remark) {
        this.remark = remark;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
