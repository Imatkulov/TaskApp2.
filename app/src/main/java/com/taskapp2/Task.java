package com.taskapp2;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
//таблица
@Entity
public class Task implements Serializable {

    // автоматический генерирует ид
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String desc;


    public Task (){
    }

    @Ignore
    public Task(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
