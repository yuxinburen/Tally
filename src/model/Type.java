package model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * User: davie
 * Date: 15-4-12
 */
@Table(name="types")
public class Type extends Base{

    @Id(column = "_id")
    private long id;

    @Column(column = "name" )
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

    public static  int getIdByName(String name){
        if(name.equals("工资")){
            return 1;
        }else if(name.equals("外快")){
            return 2;
        }else if(name.equals("衣服")){
            return 3;
        }else if(name.equals("饮食")){
            return 4;
        }else if(name.equals("住宿")){
            return 5;
        }else if(name.equals("交通")){
            return 6;
        }else if(name.equals("生活")){
            return 7;
        }
        return -1;
    }
}
