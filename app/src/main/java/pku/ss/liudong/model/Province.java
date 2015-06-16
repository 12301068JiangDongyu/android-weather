package pku.ss.liudong.model;

import java.io.Serializable;

/**
 * Created by liudong on 2015/6/12.
 */
public class Province  implements Serializable {
    private String id;
    private String name;
    private String code;
    public Province(){}

    public Province(String id, String name,String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName() + " - " + this.getCode();
    }
}
