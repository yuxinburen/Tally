package model;

import android.animation.TypeEvaluator;
import android.widget.BaseAdapter;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * User: davie
 * Date: 15-4-12
 */
@Table(name = "categories")
public class Category extends Base{

    @Id(column = "_id")
    private long id;

    @Foreign(column = "tid", foreign = "_id")
    private Type type;

    @Column(column = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static int getIdByName(String name){
        if(name.equals("自己穿")){
            return 1;
        }else if(name.equals("礼物")){
            return 2;
        }else if(name.equals("三餐")){
            return 3;
        }else if(name.equals("请客")){
            return 4;
        }else if(name.equals("房租")){
            return 5;
        }else if(name.equals("水电")){
            return 6;
        }else if(name.equals("公共")){
            return 7;
        }else if(name.equals("出租")){
            return 8;
        }else if(name.equals("娱乐")){
            return 9;
        }else if(name.equals("书籍")){
            return 10;
        }else if(name.equals("其他")){
            return 11;
        }
        return -1;
    }
}
