package com.github.linwoodcloud.bot.template.entity;


/**
 * @author CodeDoctorDE
 */
public class TemplateEntity {
    String name;
    String content;
    private int id;
    private int guildId;

    public TemplateEntity() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
