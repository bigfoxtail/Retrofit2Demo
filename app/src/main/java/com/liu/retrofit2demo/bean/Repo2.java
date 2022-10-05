package com.liu.retrofit2demo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/9/19 0019 18:10
 */
public class Repo2 {


    public int id;
    public String name;
    public String full_name;
    public String node_id;

    public OwnerEntity owner;

    public static class OwnerEntity {
        public String login;
        public int id;

        @Override
        public String toString() {
            return "OwnerEntity{" +
                    "login='" + login + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
