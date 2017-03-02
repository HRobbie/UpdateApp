package com.hrobbie.updateapp.bean;

/**
 * user:HRobbie
 * Date:2016/11/25
 * Time:13:53
 * 邮箱：hwwyouxiang@163.com
 * Description:Page Function.
 */
public class UpdateInfoBean {


    /**
     * id : 1
     * appName : 特瓦特充电
     * version : 1.21
     * url : http://192.168.1.54:8080/downloadTowatt
     * img : http://192.168.1.54:8080/download/icon_towatt.png
     * flag : 1
     * force : 0
     * information : 1.修改版本号为V1.2\n2.添加充值记录的筛选功能
     * size : 14.9
     */

    private DataBean data;
    /**
     * data : {"id":1,"appName":"特瓦特充电","version":1.21,"url":"http://192.168.1.54:8080/downloadTowatt","img":"http://192.168.1.54:8080/download/icon_towatt.png","flag":1,"force":0,"information":"1.修改版本号为V1.2\\n2.添加充值记录的筛选功能","size":14.9}
     * status : 1
     */

    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        private int id;
        private String appName;
        private double version;
        private String url;
        private String img;
        private int flag;
        private int force;
        private String information;
        private double size;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public double getVersion() {
            return version;
        }

        public void setVersion(double version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getForce() {
            return force;
        }

        public void setForce(int force) {
            this.force = force;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }
    }
}
